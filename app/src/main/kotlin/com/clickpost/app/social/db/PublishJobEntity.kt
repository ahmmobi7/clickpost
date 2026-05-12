package com.clickpost.app.social.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.clickpost.app.social.data.JobStatus
import com.clickpost.app.social.data.PostVisibility
import com.clickpost.app.social.data.PublishJob
import com.clickpost.app.social.data.PublishResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "publish_jobs")
data class PublishJobEntity(
    @PrimaryKey val id: String,
    val videoPath: String,
    val caption: String,
    val hashtagsJson: String,        // JSON array
    val visibility: String,          // PostVisibility.name
    val selectedGroupIdsJson: String, // JSON array
    val resultsJson: String,          // JSON array of PublishResult
    val status: String,              // JobStatus.name
    val createdAt: Long
)

fun PublishJobEntity.toDomain(): PublishJob {
    val gson = Gson()
    val stringListType = object : TypeToken<List<String>>() {}.type
    val resultListType = object : TypeToken<List<PublishResult>>() {}.type
    
    return PublishJob(
        id = id,
        videoPath = videoPath,
        caption = caption,
        hashtags = gson.fromJson(hashtagsJson, stringListType),
        visibility = PostVisibility.valueOf(visibility),
        selectedGroupIds = gson.fromJson(selectedGroupIdsJson, stringListType),
        results = gson.fromJson(resultsJson, resultListType),
        status = JobStatus.valueOf(status),
        createdAt = createdAt
    )
}

fun PublishJob.toEntity(): PublishJobEntity {
    val gson = Gson()
    return PublishJobEntity(
        id = id,
        videoPath = videoPath,
        caption = caption,
        hashtagsJson = gson.toJson(hashtags),
        visibility = visibility.name,
        selectedGroupIdsJson = gson.toJson(selectedGroupIds),
        resultsJson = gson.toJson(results),
        status = status.name,
        createdAt = createdAt
    )
}
