package com.clickpost.app.social.adapters

import com.clickpost.app.social.data.Platform
import com.clickpost.app.social.data.PostVisibility
import com.clickpost.app.social.engine.PlatformAdapter
import com.clickpost.app.social.engine.PostAttemptResult
import com.clickpost.app.social.engine.ValidationResult
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class TikTokAdapter @Inject constructor() : PlatformAdapter {
    override val platform = Platform.TIKTOK
    private val client = OkHttpClient()
    private val gson = Gson()

    override suspend fun validateCredential(token: String): ValidationResult = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("https://open.tiktokapis.com/v2/user/info/?fields=open_id,display_name")
                .header("Authorization", "Bearer $token")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    val data = gson.fromJson(body, TikTokUserResponse::class.java)
                    ValidationResult(isValid = true, accountDisplayName = data.data.user.display_name ?: "")
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

            // Step 1: Init upload
            val initBody = mapOf(
                "post_info" to mapOf(
                    "title" to "$caption ${hashtags.joinToString(" ")}",
                    "privacy_level" to mapVisibility(visibility),
                    "disable_duet" to false,
                    "disable_comment" to false,
                    "disable_stitch" to false
                ),
                "source_info" to mapOf(
                    "source" to "FILE_UPLOAD",
                    "video_size" to file.length(),
                    "chunk_size" to file.length(),
                    "total_chunk_count" to 1
                )
            )

            val initRequest = Request.Builder()
                .url("https://open.tiktokapis.com/v2/post/publish/video/init/")
                .header("Authorization", "Bearer $token")
                .header("Content-Type", "application/json; charset=UTF-8")
                .post(gson.toJson(initBody).toRequestBody("application/json".toMediaType()))
                .build()

            val initResponse = client.newCall(initRequest).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext PostAttemptResult.Failure(response.code.toString(), "Init failed: ${response.message}", response.code >= 500)
                }
                gson.fromJson(response.body?.string(), TikTokInitResponse::class.java)
            }

            val publishId = initResponse.data.publish_id
            val uploadUrl = initResponse.data.upload_url

            // Step 2: Upload binary
            val uploadRequest = Request.Builder()
                .url(uploadUrl)
                .header("Content-Type", "video/mp4")
                .header("Content-Range", "bytes 0-${file.length() - 1}/${file.length()}")
                .put(file.asRequestBody("video/mp4".toMediaType()))
                .build()

            client.newCall(uploadRequest).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext PostAttemptResult.Failure(response.code.toString(), "Upload failed: ${response.message}", response.code >= 500)
                }
            }

            // Step 3: Poll status
            var status = ""
            val statusBody = mapOf("publish_id" to publishId)
            repeat(150) { // 5 minutes max (2s * 150)
                delay(2000)
                val statusRequest = Request.Builder()
                    .url("https://open.tiktokapis.com/v2/post/publish/status/fetch/")
                    .header("Authorization", "Bearer $token")
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .post(gson.toJson(statusBody).toRequestBody("application/json".toMediaType()))
                    .build()

                client.newCall(statusRequest).execute().use { response ->
                    if (response.isSuccessful) {
                        val body = gson.fromJson(response.body?.string(), TikTokStatusResponse::class.java)
                        status = body.data.status
                    }
                }
                if (status == "PUBLISH_COMPLETE") return@withContext PostAttemptResult.Success(publishId, "https://www.tiktok.com/")
                if (status == "FAILED") return@withContext PostAttemptResult.Failure("PUBLISH_FAILED", "TikTok reported publish failure", false)
            }

            PostAttemptResult.Failure("TIMEOUT", "Polling timeout", true)

        } catch (e: Exception) {
            PostAttemptResult.Failure("EXCEPTION", e.message ?: "Unknown error", true)
        }
    }

    private fun mapVisibility(visibility: PostVisibility) = when (visibility) {
        PostVisibility.PUBLIC -> "PUBLIC_TO_EVERYONE"
        PostVisibility.FOLLOWERS -> "FOLLOWER_OF_CREATOR"
        PostVisibility.PRIVATE -> "SELF_ONLY"
    }

    private data class TikTokUserResponse(val data: UserData)
    private data class UserData(val user: User)
    private data class User(val open_id: String?, val display_name: String?)
    private data class TikTokInitResponse(val data: InitData)
    private data class InitData(val publish_id: String, val upload_url: String)
    private data class TikTokStatusResponse(val data: StatusData)
    private data class StatusData(val status: String)
}
