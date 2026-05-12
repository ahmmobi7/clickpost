package com.clickpost.app.social.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clickpost.app.social.data.JobStatus
import com.clickpost.app.social.data.PostVisibility
import com.clickpost.app.social.data.PublishJob
import com.clickpost.app.social.data.ResultStatus
import com.clickpost.app.social.engine.PublishEngine
import com.clickpost.app.social.repository.AccountGroupRepository
import com.clickpost.app.social.repository.PublishHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PublishViewModel @Inject constructor(
    private val engine: PublishEngine,
    private val groupRepo: AccountGroupRepository,
    private val historyRepo: PublishHistoryRepository
) : ViewModel() {

    private val _selectedGroupIds = MutableStateFlow<Set<String>>(emptySet())
    private val _caption = MutableStateFlow("")
    private val _hashtags = MutableStateFlow<List<String>>(emptyList())
    private val _visibility = MutableStateFlow(PostVisibility.PUBLIC)

    val uiState: StateFlow<UiState> = combine(
        groupRepo.groupsFlow,
        _selectedGroupIds,
        _caption,
        _hashtags,
        _visibility,
        engine.publishState,
        historyRepo.historyFlow
    ) { array ->
        UiState(
            availableGroups = array[0] as List<com.clickpost.app.social.data.AccountGroup>,
            selectedGroupIds = array[1] as Set<String>,
            caption = array[2] as String,
            hashtags = array[3] as List<String>,
            visibility = array[4] as PostVisibility,
            publishState = array[5] as PublishEngine.PublishState,
            history = array[6] as List<PublishJob>
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState())

    fun toggleGroup(groupId: String) {
        _selectedGroupIds.update { current ->
            if (current.contains(groupId)) current - groupId else current + groupId
        }
    }

    fun setCaption(text: String) {
        _caption.value = text
    }

    fun addHashtag(tag: String) {
        if (tag.isNotBlank() && !_hashtags.value.contains(tag)) {
            _hashtags.update { it + tag.removePrefix("#") }
        }
    }

    fun removeHashtag(tag: String) {
        _hashtags.update { it - tag }
    }

    fun setVisibility(v: PostVisibility) {
        _visibility.value = v
    }

    fun startPublish(videoPath: String) {
        val state = uiState.value
        if (state.selectedGroupIds.isEmpty()) return
        if (state.caption.isBlank()) return
        if (!File(videoPath).exists()) return

        val job = PublishJob(
            videoPath = videoPath,
            caption = state.caption,
            hashtags = state.hashtags,
            visibility = state.visibility,
            selectedGroupIds = state.selectedGroupIds.toList()
        )

        viewModelScope.launch {
            engine.publish(job, viewModelScope)
        }
    }

    fun retryFailedPlatforms(jobId: String) {
        viewModelScope.launch {
            val job = historyRepo.getJob(jobId) ?: return@launch
            val failedCredentials = job.results
                .filter { it.status == ResultStatus.FAILED }
                .map { it.credentialId }
            
            if (failedCredentials.isEmpty()) return@launch

            // For V1, we just re-publish the whole job with a new ID
            // or we could implement partial retry in PublishEngine
            val retryJob = job.copy(
                id = java.util.UUID.randomUUID().toString(),
                status = JobStatus.QUEUED,
                results = emptyList()
            )
            engine.publish(retryJob, viewModelScope)
        }
    }

    fun resetPublishState() {
        engine.reset()
    }

    data class UiState(
        val availableGroups: List<com.clickpost.app.social.data.AccountGroup> = emptyList(),
        val selectedGroupIds: Set<String> = emptySet(),
        val caption: String = "",
        val hashtags: List<String> = emptyList(),
        val visibility: PostVisibility = PostVisibility.PUBLIC,
        val publishState: PublishEngine.PublishState = PublishEngine.PublishState.Idle,
        val history: List<PublishJob> = emptyList()
    )
}
