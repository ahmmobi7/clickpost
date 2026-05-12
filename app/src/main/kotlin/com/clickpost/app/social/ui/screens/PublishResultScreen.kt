package com.clickpost.app.social.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.clickpost.app.social.data.JobStatus
import com.clickpost.app.social.data.ResultStatus
import com.clickpost.app.social.ui.components.PublishResultRow
import com.clickpost.app.social.viewmodel.PublishViewModel
import com.clickpost.app.ui.theme.Teal500

@Composable
fun PublishResultScreen(
    jobId: String,
    viewModel: PublishViewModel,
    onBackToDashboard: () -> Unit
) {
    val context = LocalContext.current
    val job = viewModel.uiState.value.history.find { it.id == jobId } ?: return

    val successCount = job.results.count { it.status == ResultStatus.SUCCESS }
    val totalCount = job.results.size

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val icon = when (job.status) {
                JobStatus.SUCCESS -> Icons.Default.CheckCircle
                JobStatus.PARTIAL_SUCCESS -> Icons.Default.Warning
                else -> Icons.Default.Error
            }
            val tint = when (job.status) {
                JobStatus.SUCCESS -> Teal500
                JobStatus.PARTIAL_SUCCESS -> Color(0xFFFFC107)
                else -> MaterialTheme.colorScheme.error
            }

            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = tint
            )

            Text(
                text = when (job.status) {
                    JobStatus.SUCCESS -> "Video published!"
                    JobStatus.PARTIAL_SUCCESS -> "Partially published"
                    else -> "Publish failed"
                },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )

            Text(
                text = "Published to $successCount of $totalCount platforms",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(job.results) { result ->
                    PublishResultRow(
                        result = result,
                        onRetry = { viewModel.retryFailedPlatforms(jobId) },
                        onViewPost = {
                            result.postUrl?.let { url ->
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                            }
                        }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = onBackToDashboard,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Back to dashboard")
            }
        }
    }
}
