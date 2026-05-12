package com.clickpost.app.social.adapters

import com.clickpost.app.social.data.Platform
import com.clickpost.app.social.data.PostVisibility
import com.clickpost.app.social.engine.PlatformAdapter
import com.clickpost.app.social.engine.PostAttemptResult
import com.clickpost.app.social.engine.ValidationResult
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import javax.inject.Inject

class InstagramAdapter @Inject constructor() : PlatformAdapter {
    override val platform = Platform.INSTAGRAM
    private val client = OkHttpClient()
    private val gson = Gson()

    override suspend fun validateCredential(token: String): ValidationResult = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("https://graph.facebook.com/v20.0/me?fields=id,username")
                .header("Authorization", "Bearer $token")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    val data = gson.fromJson(body, IGUserResponse::class.java)
                    ValidationResult(isValid = true, accountDisplayName = data.username)
                } else {
                    ValidationResult(isValid = false, errorMessage = "HTTP ${response.code}: ${response.message}")
                }
            }
        } catch (e: Exception) {
            ValidationResult(isValid = false, errorMessage = e.message)
        }
    }

    override suspend fun post(
        videoPath: String,
        caption: String,
        hashtags: List<String>,
        visibility: PostVisibility,
        token: String
    ): PostAttemptResult = withContext(Dispatchers.IO) {
        // Instagram Graph API for Reels requires a public video URL.
        // For V1, we return failure explaining the requirement.
        PostAttemptResult.Failure(
            "IG_NOT_SUPPORTED",
            "Instagram publishing requires a publicly accessible video URL. Please upload the video first.",
            false
        )
    }

    private data class IGUserResponse(val id: String, val username: String)
}
