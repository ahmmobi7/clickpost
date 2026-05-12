package com.clickpost.app.social.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clickpost.app.social.data.Platform
import com.clickpost.app.social.ui.components.PlatformBadge
import com.clickpost.app.social.viewmodel.AccountGroupViewModel
import com.clickpost.app.ui.components.GradientButton
import com.clickpost.app.ui.theme.Teal500

@Composable
fun CreateEditGroupScreen(
    groupId: String?,
    viewModel: AccountGroupViewModel,
    onBack: () -> Unit,
    onAddCredential: (String, Platform) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val group = uiState.groups.find { it.id == groupId }
    
    var groupName by remember { mutableStateOf(group?.name ?: "") }
    
    LaunchedEffect(group) {
        if (group != null && groupName.isEmpty()) {
            groupName = group.name
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (groupId == null) "Create group" else "Edit group") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (groupId == null) {
                                viewModel.createGroup(groupName)
                            } else {
                                viewModel.renameGroup(groupId, groupName)
                            }
                            onBack()
                        },
                        enabled = groupName.isNotBlank()
                    ) {
                        Text("Save", color = Teal500)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = groupName,
                onValueChange = { groupName = it },
                label = { Text("Group name") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(Modifier.height(24.dp))

            Text("Platforms", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))

            if (group != null) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(group.credentials) { cred ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            PlatformBadge(
                                platform = cred.platform,
                                accountName = cred.accountDisplayName,
                                status = cred.status
                            )
                            IconButton(onClick = { viewModel.deleteCredential(cred.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))

            if (group != null && group.credentials.size < 4) {
                Text("Add more platforms:", style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Platform.values().filter { p -> group.credentials.none { it.platform == p } }.forEach { platform ->
                        OutlinedButton(
                            onClick = { onAddCredential(group.id, platform) },
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            Text(platform.displayName, fontSize = 11.sp)
                        }
                    }
                }
            } else if (groupId == null) {
                Text(
                    "Save the group name first to add platforms.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (groupId != null) {
                Spacer(Modifier.height(16.dp))
                OutlinedButton(
                    onClick = {
                        viewModel.deleteGroup(groupId)
                        onBack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete Group")
                }
            }
        }
    }
}
