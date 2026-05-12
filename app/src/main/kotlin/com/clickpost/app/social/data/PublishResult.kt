package com.clickpost.app.social.data

import kotlinx.serialization.Serializable

@Serializable
data class PublishResult(
    val credentialId: String,
    val platform: Platform,
    val accountDisplayName: String,
    val status: ResultStatus,
    val platformPostId: String? = null,
    val postUrl: String? = null,
    val errorCode: String? = null,
    val errorMessage: String? = null,
    val retryCount: Int = 0,
    val completedAt: Long = System.currentTimeMillis()
)

enum class ResultStatus { SUCCESS, FAILED, SKIPPED, IN_PROGRESS }
