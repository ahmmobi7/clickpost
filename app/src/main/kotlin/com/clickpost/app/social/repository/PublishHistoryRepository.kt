package com.clickpost.app.social.repository

import com.clickpost.app.social.data.JobStatus
import com.clickpost.app.social.data.PublishJob
import com.clickpost.app.social.db.PublishJobDao
import com.clickpost.app.social.db.toDomain
import com.clickpost.app.social.db.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PublishHistoryRepository @Inject constructor(
    private val dao: PublishJobDao
) {
    val historyFlow: Flow<List<PublishJob>> = dao.getAllJobs().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun saveJob(job: PublishJob) {
        dao.insert(job.toEntity())
    }

    suspend fun updateJobStatus(jobId: String, status: JobStatus) {
        dao.updateStatus(jobId, status.name)
    }

    suspend fun getJob(jobId: String): PublishJob? {
        return dao.getById(jobId)?.toDomain()
    }
}
