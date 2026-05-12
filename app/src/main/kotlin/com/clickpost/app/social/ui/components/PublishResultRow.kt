package com.clickpost.app.social.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clickpost.app.social.data.CredentialStatus
import com.clickpost.app.social.data.PublishResult
import com.clickpost.app.social.data.ResultStatus
import com.clickpost.app.ui.theme.Teal500

@Composable
fun PublishResultRow(
    result: PublishResult,
    onRetry: () -> Unit,
    onViewPost: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlatformBadge(
            platform = result.platform,
            accountName = result.accountDisplayName,
            status = if (result.status == ResultStatus.FAILED) CredentialStatus.ERROR else CredentialStatus.VALID,
            modifier = Modifier.size(width = 80.dp, height = 24.dp)
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = result.accountDisplayName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            if (result.status == ResultStatus.FAILED) {
                Text(
                    text = result.errorMessage ?: "Unknown error",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        when (result.status) {
            ResultStatus.SUCCESS -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Teal500,
                        modifier = Modifier.size(20.dp)
                    )
                    TextButton(onClick = onViewPost) {
                        Text("View post", color = Teal500, fontSize = 13.sp)
                    }
                }
            }
            ResultStatus.FAILED -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    OutlinedButton(
                        onClick = onRetry,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Retry", fontSize = 12.sp)
                    }
                }
            }
            ResultStatus.IN_PROGRESS -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = Teal500
                )
            }
            ResultStatus.SKIPPED -> {
                Text("Skipped", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
