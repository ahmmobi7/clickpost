package com.clickpost.app.social.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.clickpost.app.social.data.Platform
import com.clickpost.app.social.viewmodel.AccountGroupViewModel
import com.clickpost.app.ui.components.GradientButton
import com.clickpost.app.ui.theme.Teal500

@Composable
fun PlatformCredentialFormScreen(
    groupId: String,
    platform: Platform,
    viewModel: AccountGroupViewModel,
    onBack: () -> Unit
) {
    var apiInfo by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }
    var showToken by remember { mutableStateOf(false) }
    
    val uiState by viewModel.uiState.collectAsState()
    val validationState = uiState.validationState

    LaunchedEffect(validationState) {
        if (validationState is AccountGroupViewModel.ValidationState.Success) {
            onBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${platform.displayName} account") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            Text(
                text = "Enter your access token",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = when (platform) {
                    Platform.TIKTOK -> "Get your token from developers.tiktok.com → App → Keys"
                    Platform.FACEBOOK -> "Use a long-lived Page Access Token from developers.facebook.com"
                    Platform.INSTAGRAM -> "Use your Facebook Page Access Token (Instagram Business required)"
                    Platform.YOUTUBE -> "Use an OAuth access token from Google Cloud Console"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            if (platform == Platform.FACEBOOK || platform == Platform.INSTAGRAM || platform == Platform.TIKTOK) {
                OutlinedTextField(
                    value = apiInfo,
                    onValueChange = { apiInfo = it },
                    label = { 
                        Text(when(platform) {
                            Platform.FACEBOOK -> "Page ID"
                            Platform.INSTAGRAM -> "Instagram Business ID"
                            Platform.TIKTOK -> "Client ID (from App Console)"
                            else -> "API Info"
                        })
                    },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    shape = MaterialTheme.shapes.medium
                )
            }

            OutlinedTextField(
                value = token,
                onValueChange = { token = it },
                label = { Text("Access Token") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (showToken) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showToken = !showToken }) {
                        Icon(
                            if (showToken) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (showToken) "Hide" else "Show"
                        )
                    }
                },
                shape = MaterialTheme.shapes.medium
            )

            if (validationState is AccountGroupViewModel.ValidationState.Failure) {
                Text(
                    text = validationState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(Modifier.weight(1f))

            GradientButton(
                text = "Validate & save",
                onClick = { 
                    val finalToken = if (apiInfo.isNotBlank()) "$apiInfo:$token" else token
                    viewModel.validateAndAddCredential(groupId, platform, finalToken) 
                },
                isLoading = validationState is AccountGroupViewModel.ValidationState.Validating,
                enabled = token.isNotBlank()
            )
        }
    }
}
