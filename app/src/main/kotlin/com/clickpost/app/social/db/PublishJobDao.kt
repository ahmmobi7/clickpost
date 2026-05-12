package com.clickpost.app.social.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PublishJobDao {
    @Query("SELECT * FROM publish_jobs ORDER BY createdAt DESC")
    fun getAllJobs(): Flow<List<PublishJobEntity>>

    @Query("SELECT * FROM publish_jobs WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): PublishJobEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(job: PublishJobEntity)

    @Query("UPDATE publish_jobs SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: String)

    @Query("DELETE FROM publish_jobs WHERE createdAt < :before")
    suspend fun deleteOlderThan(before: Long)
}
