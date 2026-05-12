package com.clickpost.app.social.engine

import com.clickpost.app.social.data.Platform
import com.clickpost.app.social.data.PostVisibility

interface PlatformAdapter {
    val platform: Platform

    suspend fun validateCredential(token: String): ValidationResult

    suspend fun post(
        videoPath: String,
        caption: String,
        hashtags: List<String>,
        visibility: PostVisibility,
        token: String
    ): PostAttemptResult
}

data class ValidationResult(
    val isValid: Boolean,
    val accountDisplayName: String = "",
    val grantedScopes: List<String> = emptyList(),
    val errorMessage: String? = null
)

sealed class PostAttemptResult {
    data class Success(val platformPostId: String, val postUrl: String) : PostAttemptResult()
    data class Failure(val errorCode: String, val message: String, val isRetryable: Boolean) : PostAttemptResult()
}
