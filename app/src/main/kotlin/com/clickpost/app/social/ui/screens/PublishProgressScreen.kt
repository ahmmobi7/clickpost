package com.clickpost.app.social.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.clickpost.app.social.data.ResultStatus
import com.clickpost.app.social.engine.PublishEngine
import com.clickpost.app.social.ui.components.PublishResultRow
import com.clickpost.app.social.viewmodel.PublishViewModel
import com.clickpost.app.ui.theme.Teal500
import kotlinx.coroutines.delay

@Composable
fun PublishProgressScreen(
    jobId: String,
    viewModel: PublishViewModel,
    onNavigateToResult: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val publishState = uiState.publishState

    val results = when (publishState) {
        is PublishEngine.PublishState.Running -> publishState.results.values.toList()
        is PublishEngine.PublishState.Completed -> publishState.job.results
        else -> emptyList()
    }

    val successCount = results.count { it.status == ResultStatus.SUCCESS }
    val totalCount = results.size
    val progress = if (totalCount > 0) successCount.toFloat() / totalCount else 0f

    LaunchedEffect(publishState) {
        if (publishState is PublishEngine.PublishState.Completed) {
            delay(1500)
            onNavigateToResult(jobId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Publishing...", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = Teal500,
                trackColor = Teal500.copy(alpha = 0.1f),
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            
            Text(
                text = "$successCount of $totalCount completed",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(results) { result ->
                    PublishResultRow(
                        result = result,
                        onRetry = {}, // No retry in progress screen
                        onViewPost = {} // No view post in progress screen
                    )
                }
            }
        }
    }
}
