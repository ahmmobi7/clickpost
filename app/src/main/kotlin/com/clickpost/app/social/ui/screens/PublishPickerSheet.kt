package com.clickpost.app.social.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.clickpost.app.social.data.PostVisibility
import com.clickpost.app.social.ui.components.PlatformBadge
import com.clickpost.app.social.viewmodel.PublishViewModel
import com.clickpost.app.ui.components.GradientButton
import com.clickpost.app.ui.theme.Teal500

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PublishPickerSheet(
    videoPath: String,
    viewModel: PublishViewModel,
    onPublishStarted: () -> Unit,
    onSkip: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var hashtagInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .padding(bottom = 32.dp)
    ) {
        Text("Publish to social", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.caption,
            onValueChange = { viewModel.setCaption(it) },
            label = { Text("Caption") },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            maxLines = 5,
            shape = MaterialTheme.shapes.medium
        )
        Text(
            "${uiState.caption.length}/2200",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.align(Alignment.End).padding(top = 4.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text("Hashtags", style = MaterialTheme.typography.labelLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = hashtagInput,
                onValueChange = { hashtagInput = it },
                label = { Text("Add tag") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )
            IconButton(onClick = {
                if (hashtagInput.isNotBlank()) {
                    viewModel.addHashtag(hashtagInput)
                    hashtagInput = ""
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
        
        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            uiState.hashtags.forEach { tag ->
                InputChip(
                    selected = true,
                    onClick = { viewModel.removeHashtag(tag) },
                    label = { Text("#$tag") },
                    trailingIcon = { Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp)) }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text("Visibility", style = MaterialTheme.typography.labelLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PostVisibility.values().forEach { visibility ->
                FilterChip(
                    selected = uiState.visibility == visibility,
                    onClick = { viewModel.setVisibility(visibility) },
                    label = { Text(visibility.name) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text("Account groups", style = MaterialTheme.typography.labelLarge)
        LazyColumn(
            modifier = Modifier.height(160.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.availableGroups) { group ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = uiState.selectedGroupIds.contains(group.id),
                        onCheckedChange = { viewModel.toggleGroup(group.id) }
                    )
                    Text(group.name, modifier = Modifier.weight(1f))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        group.credentials.forEach { cred ->
                            PlatformBadge(cred.platform, cred.accountDisplayName, cred.status)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        GradientButton(
            text = "Publish now",
            onClick = {
                viewModel.startPublish(videoPath)
                onPublishStarted()
            },
            enabled = uiState.selectedGroupIds.isNotEmpty() && uiState.caption.isNotBlank()
        )
        
        TextButton(
            onClick = onSkip,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Skip", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
