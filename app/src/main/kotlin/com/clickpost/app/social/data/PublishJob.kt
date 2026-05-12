package com.clickpost.app.social.data

import java.util.UUID

data class PublishJob(
    val id: String = UUID.randomUUID().toString(),
    val videoPath: String,
    val caption: String,
    val hashtags: List<String> = emptyList(),
    val visibility: PostVisibility = PostVisibility.PUBLIC,
    val selectedGroupIds: List<String>,
    val results: List<PublishResult> = emptyList(),
    val status: JobStatus = JobStatus.QUEUED,
    val createdAt: Long = System.currentTimeMillis()
)

enum class PostVisibility { PUBLIC, FOLLOWERS, PRIVATE }
enum class JobStatus { QUEUED, RUNNING, SUCCESS, PARTIAL_SUCCESS, FAILED }
