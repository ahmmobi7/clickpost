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
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class YouTubeAdapter @Inject constructor() : PlatformAdapter {
    override val platform = Platform.YOUTUBE
    private val client = OkHttpClient()
    private val gson = Gson()

    override suspend fun validateCredential(token: String): ValidationResult = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("https://www.googleapis.com/youtube/v3/channels?part=snippet&mine=true")
                .header("Authorization", "Bearer $token")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    val data = gson.fromJson(body, YTChannelsResponse::class.java)
                    if (data.items.isEmpty()) return@withContext ValidationResult(isValid = false, errorMessage = "No YouTube channel found")
                    ValidationResult(isValid = true, accountDisplayName = data.items[0].snippet.title)
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
            val file = File(videoPath)
            if (!file.exists()) return@withContext PostAttemptResult.Failure("FILE_NOT_FOUND", "Video file not found", false)

            // Step 1: Init resumable upload
            val metadata = mapOf(
                "snippet" to mapOf(
                    "title" to caption,
                    "description" to hashtags.joinToString(" "),
                    "tags" to hashtags
                ),
                "status" to mapOf(
                    "privacyStatus" to mapVisibility(visibility)
                )
            )

            val initRequest = Request.Builder()
                .url("https://www.googleapis.com/upload/youtube/v3/videos?uploadType=resumable&part=snippet,status")
                .header("Authorization", "Bearer $token")
                .header("X-Upload-Content-Type", "video/mp4")
                .header("X-Upload-Content-Length", file.length().toString())
                .post(gson.toJson(metadata).toRequestBody("application/json".toMediaType()))
                .build()

            val uploadUrl = client.newCall(initRequest).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext PostAttemptResult.Failure(response.code.toString(), "Init failed: ${response.message}", response.code >= 500)
                }
                response.header("Location") ?: return@withContext PostAttemptResult.Failure("MISSING_LOCATION", "Missing upload URL", true)
            }

            // Step 2: Upload file (one-shot for V1, could be chunked for large files)
            val uploadRequest = Request.Builder()
                .url(uploadUrl)
                .put(file.asRequestBody("video/mp4".toMediaType()))
                .build()

            client.newCall(uploadRequest).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    val data = gson.fromJson(body, YTVideoResponse::class.java)
                    PostAttemptResult.Success(data.id, "https://youtube.com/watch?v=${data.id}")
                } else {
                    PostAttemptResult.Failure(response.code.toString(), "Upload failed: ${response.message}", response.code >= 500)
                }
            }
        } catch (e: Exception) {
            PostAttemptResult.Failure("EXCEPTION", e.message ?: "Unknown error", true)
        }
    }

    private fun mapVisibility(visibility: PostVisibility) = when (visibility) {
        PostVisibility.PUBLIC -> "public"
        PostVisibility.FOLLOWERS -> "unlisted" // YouTube doesn't have "followers only", using unlisted
        PostVisibility.PRIVATE -> "private"
    }

    private data class YTChannelsResponse(val items: List<YTChannel>)
    private data class YTChannel(val id: String, val snippet: YTSnippet)
    private data class YTSnippet(val title: String)
    private data class YTVideoResponse(val id: String)
}
