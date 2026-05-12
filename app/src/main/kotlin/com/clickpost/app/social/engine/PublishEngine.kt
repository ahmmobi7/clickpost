package com.clickpost.app.social.engine

import com.clickpost.app.social.data.*
import com.clickpost.app.social.repository.AccountGroupRepository
import com.clickpost.app.social.repository.PublishHistoryRepository
import com.clickpost.app.social.storage.CredentialVault
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PublishEngine @Inject constructor(
    private val adapters: Map<Platform, PlatformAdapter>,
    private val vault: CredentialVault,
    private val groupRepo: AccountGroupRepository,
    private val historyRepo: PublishHistoryRepository
) {
    private val _publishState = MutableStateFlow<PublishState>(PublishState.Idle)
    val publishState: StateFlow<PublishState> = _publishState.asStateFlow()

    sealed class PublishState {
        object Idle : PublishState()
        data class Running(val jobId: String, val results: Map<String, PublishResult>) : PublishState()
        data class Completed(val job: PublishJob) : PublishState()
    }

    suspend fun publish(job: PublishJob, scope: CoroutineScope) {
        val groups = groupRepo.getGroups()
        val credentials = job.selectedGroupIds.flatMap { groupId ->
            groups.find { it.id == groupId }?.credentials ?: emptyList()
        }.distinctBy { it.id }

        val resultsMap = mutableMapOf<String, PublishResult>()
        credentials.forEach { cred ->
            resultsMap[cred.id] = PublishResult(
                credentialId = cred.id,
                platform = cred.platform,
                accountDisplayName = cred.accountDisplayName,
                status = ResultStatus.IN_PROGRESS
            )
        }
        _publishState.value = PublishState.Running(job.id, resultsMap.toMap())

        supervisorScope {
            credentials.map { cred ->
                launch {
                    val result = performPublishWithRetry(job, cred)
                    synchronized(resultsMap) {
                        resultsMap[cred.id] = result
                        _publishState.value = PublishState.Running(job.id, resultsMap.toMap())
                    }
                }
            }
        }

        val finalResults = resultsMap.values.toList()
        val overallStatus = when {
            finalResults.all { it.status == ResultStatus.SUCCESS } -> JobStatus.SUCCESS
            finalResults.any { it.status == ResultStatus.SUCCESS } -> JobStatus.PARTIAL_SUCCESS
            else -> JobStatus.FAILED
        }

        val completedJob = job.copy(results = finalResults, status = overallStatus)
        historyRepo.saveJob(completedJob)
        _publishState.value = PublishState.Completed(completedJob)
    }

    private suspend fun performPublishWithRetry(job: PublishJob, cred: PlatformCredential): PublishResult {
        var decryptedToken = vault.retrieve(cred.keyAlias) ?: return PublishResult(
            credentialId = cred.id,
            platform = cred.platform,
            accountDisplayName = cred.accountDisplayName,
            status = ResultStatus.FAILED,
            errorMessage = "Token missing in vault"
        )

        val adapter = adapters[cred.platform] ?: return PublishResult(
            credentialId = cred.id,
            platform = cred.platform,
            accountDisplayName = cred.accountDisplayName,
            status = ResultStatus.FAILED,
            errorMessage = "No adapter for ${cred.platform}"
        )

        var lastResult: PublishResult? = null
        val delays = listOf(0L, 2000L, 4000L, 8000L)

        for (attempt in 0..3) {
            if (attempt > 0) delay(delays[attempt])

            try {
                val attemptResult = adapter.post(
                    videoPath = job.videoPath,
                    caption = job.caption,
                    hashtags = job.hashtags,
                    visibility = job.visibility,
                    token = decryptedToken
                )

                lastResult = when (attemptResult) {
                    is PostAttemptResult.Success -> {
                        PublishResult(
                            credentialId = cred.id,
                            platform = cred.platform,
                            accountDisplayName = cred.accountDisplayName,
                            status = ResultStatus.SUCCESS,
                            platformPostId = attemptResult.platformPostId,
                            postUrl = attemptResult.postUrl,
                            retryCount = attempt
                        )
                    }
                    is PostAttemptResult.Failure -> {
                        if (attemptResult.errorCode == "401" || attemptResult.errorCode == "403") {
                            groupRepo.markCredentialStatus(cred.id, CredentialStatus.EXPIRED)
                        }
                        
                        PublishResult(
                            credentialId = cred.id,
                            platform = cred.platform,
                            accountDisplayName = cred.accountDisplayName,
                            status = ResultStatus.FAILED,
                            errorCode = attemptResult.errorCode,
                            errorMessage = attemptResult.message,
                            retryCount = attempt
                        )
                    }
                }

                if (attemptResult is PostAttemptResult.Success) break
                if (attemptResult is PostAttemptResult.Failure && !attemptResult.isRetryable) break

            } catch (e: Exception) {
                lastResult = PublishResult(
                    credentialId = cred.id,
                    platform = cred.platform,
                    accountDisplayName = cred.accountDisplayName,
                    status = ResultStatus.FAILED,
                    errorMessage = e.message ?: "Unknown error",
                    retryCount = attempt
                )
                if (e !is IOException) break
            }
        }

        decryptedToken = "" // Clear token from memory
        return lastResult ?: PublishResult(
            credentialId = cred.id,
            platform = cred.platform,
            accountDisplayName = cred.accountDisplayName,
            status = ResultStatus.FAILED,
            errorMessage = "Failed after retries"
        )
    }

    fun reset() {
        _publishState.value = PublishState.Idle
    }
}
