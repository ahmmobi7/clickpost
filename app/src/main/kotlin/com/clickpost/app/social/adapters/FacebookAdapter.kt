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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class FacebookAdapter @Inject constructor() : PlatformAdapter {
    override val platform = Platform.FACEBOOK
    private val client = OkHttpClient()
    private val gson = Gson()

    override suspend fun validateCredential(token: String): ValidationResult = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("https://graph.facebook.com/v20.0/me?fields=id,name")
                .header("Authorization", "Bearer $token")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    val data = gson.fromJson(body, FBUserResponse::class.java)
                    ValidationResult(isValid = true, accountDisplayName = data.name)
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
        try {
            // Step 1: Get page id and page access token
            val pageRequest = Request.Builder()
                .url("https://graph.facebook.com/v20.0/me/accounts")
                .header("Authorization", "Bearer $token")
                .get()
                .build()

            val (pageId, pageToken) = client.newCall(pageRequest).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext PostAttemptResult.Failure(response.code.toString(), "Failed to get pages: ${response.message}", response.code >= 500)
                }
                val body = response.body?.string()
                val data = gson.fromJson(body, FBPagesResponse::class.java)
                if (data.data.isEmpty()) return@withContext PostAttemptResult.Failure("NO_PAGES", "No Facebook pages found for this account", false)
                data.data[0].id to data.data[0].access_token
            }

            // Step 2: Upload video
            val file = File(videoPath)
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("source", file.name, file.asRequestBody("video/mp4".toMediaType()))
                .addFormDataPart("description", "$caption ${hashtags.joinToString(" ")}")
                .addFormDataPart("published", (visibility == PostVisibility.PUBLIC).toString())
                .addFormDataPart("access_token", pageToken)
                .build()

            val uploadRequest = Request.Builder()
                .url("https://graph-video.facebook.com/v20.0/$pageId/videos")
                .post(requestBody)
                .build()

            client.newCall(uploadRequest).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    val data = gson.fromJson(body, FBVideoResponse::class.java)
                    PostAttemptResult.Success(data.id, "https://facebook.com/$pageId/videos/${data.id}")
                } else {
                    PostAttemptResult.Failure(response.code.toString(), "Upload failed: ${response.message}", response.code >= 500)
                }
            }
        } catch (e: Exception) {
            PostAttemptResult.Failure("EXCEPTION", e.message ?: "Unknown error", true)
        }
    }

    private data class FBUserResponse(val id: String, val name: String)
    private data class FBPagesResponse(val data: List<FBPage>)
    private data class FBPage(val id: String, val access_token: String)
    private data class FBVideoResponse(val id: String)
}
