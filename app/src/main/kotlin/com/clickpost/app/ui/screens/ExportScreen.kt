package com.clickpost.app.ui.screens

import android.content.Intent
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.clickpost.app.data.model.Resolution
import com.clickpost.app.engine.ExportState
import com.clickpost.app.engine.ShareTarget
import com.clickpost.app.social.ui.screens.PublishPickerSheet
import com.clickpost.app.social.viewmodel.PublishViewModel
import com.clickpost.app.ui.components.GradientButton
import com.clickpost.app.ui.components.OutlinedClickButton
import com.clickpost.app.ui.components.ResolutionChip
import com.clickpost.app.ui.components.SectionHeader
import com.clickpost.app.ui.theme.Teal400
import com.clickpost.app.ui.theme.Teal500
import com.clickpost.app.viewmodel.MainViewModel

@UnstableApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportScreen(
    viewModel: MainViewModel,
    exportState: ExportState,
    onBack: () -> Unit,
    onDone: () -> Unit,
    onPublishStarted: (String) -> Unit
) {
    val uiState  = viewModel.uiState.collectAsState().value
    val context  = LocalContext.current
    val caps     = uiState.deviceCaps

    val publishViewModel: PublishViewModel = hiltViewModel()
    var showPublishSheet by remember { mutableStateOf(false) }

    var selectedResolution by remember {
        mutableStateOf(
            caps.supportedResolutions.firstOrNull { it == Resolution.FHD_1080 }
                ?: caps.supportedResolutions.firstOrNull()
                ?: Resolution.FHD_1080
        )
    }

    val isProcessing = exportState is ExportState.Processing
    val isDone       = exportState is ExportState.Success
    val isError      = exportState is ExportState.Error
    val progress     = (exportState as? ExportState.Processing)?.progress ?: 0f
    val outputPath   = (exportState as? ExportState.Success)?.outputPath ?: ""
    val errorMsg     = (exportState as? ExportState.Error)?.message ?: ""

    // Rotation animation for the processing indicator
    val rotation by rememberInfiniteTransition(label = "rotate").animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing)),
        label = "spinner"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Export video", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack, enabled = !isProcessing) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color(0xFFF5F6F8)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))

            // ── Idle / Resolution Selection ──────────────────────────────────
            AnimatedVisibility(
                visible = !isProcessing && !isDone,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    SectionHeader("Select resolution")
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Resolution.entries.forEach { res ->
                            val supported = res in caps.supportedResolutions
                            ResolutionChip(
                                label = res.label,
                                selected = selectedResolution == res && supported,
                                enabled = supported,
                                onClick = { selectedResolution = res },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Device capability note
                    if (caps.supportedResolutions.size < Resolution.entries.size) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = "Greyed-out resolutions are not supported by this device's hardware encoder.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    // Encoding info row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ExportInfoChip("Codec", if (caps.supportsH265) "H.265" else "H.264")
                        ExportInfoChip("GPU", if (caps.hardwareEncoderAvailable) "Enabled" else "Software")
                        ExportInfoChip("Resolution", selectedResolution.label)
                    }

                    if (isError) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.errorContainer
                        ) {
                            Text(
                                text = "Export failed: $errorMsg",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    GradientButton(
                        text = if (isError) "Retry Export" else "Export & Brand",
                        onClick = { viewModel.startExport(selectedResolution) }
                    )
                }
            }

            // ── Processing State ─────────────────────────────────────────────
            AnimatedVisibility(
                visible = isProcessing,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxWidth().padding(top = 40.dp)
                ) {
                    // Spinning progress ring
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(140.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { progress },
                            strokeWidth = 8.dp,
                            color = Teal500,
                            trackColor = Teal400.copy(alpha = 0.15f),
                            modifier = Modifier.fillMaxSize()
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${(progress * 100).toInt()}%",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Teal500
                            )
                            Text(
                                text = selectedResolution.label,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Text(
                        "Branding your video…",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        "Applying logo, text overlays and end clip",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedClickButton(text = "Cancel", onClick = onBack)
                }
            }

            // ── Success State ────────────────────────────────────────────────
            AnimatedVisibility(
                visible = isDone,
                enter = fadeIn() + scaleIn(initialScale = 0.8f),
                exit = fadeOut()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
                ) {
                    // Success circle
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(100.dp)
                            .background(Teal500.copy(alpha = 0.12f), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Done",
                            tint = Teal500,
                            modifier = Modifier.size(56.dp)
                        )
                    }

                    Text(
                        "Video ready!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        "Saved to your ClickPost folder",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Share options
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Instagram
                        if (viewModel.isInstagramInstalled) {
                            GradientButton(
                                text = "Share to Instagram",
                                onClick = {
                                    val intent = viewModel.shareVideo(outputPath, ShareTarget.INSTAGRAM)
                                    context.startActivity(intent)
                                }
                            )
                        }

                        // WhatsApp
                        if (viewModel.isWhatsAppInstalled) {
                            OutlinedClickButton(
                                text = "Share to WhatsApp",
                                onClick = {
                                    val intent = viewModel.shareVideo(outputPath, ShareTarget.WHATSAPP)
                                    context.startActivity(intent)
                                }
                            )
                        }

                        // General share
                        OutlinedClickButton(
                            text = "Share via…",
                            onClick = {
                                val intent = viewModel.shareVideo(outputPath, ShareTarget.GENERAL_DOWNLOAD)
                                context.startActivity(intent)
                            }
                        )
                    }

                    TextButton(onClick = onDone) {
                        Text("Back to dashboard", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Spacer(Modifier.height(8.dp))
                    
                    Text(
                        "OR",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    
                    Spacer(Modifier.height(8.dp))

                    GradientButton(
                        text = "Publish to Social",
                        onClick = { showPublishSheet = true }
                    )
                }
            }
        }

        if (showPublishSheet && exportState is ExportState.Success) {
            ModalBottomSheet(
                onDismissRequest = { showPublishSheet = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                PublishPickerSheet(
                    videoPath = exportState.outputPath,
                    viewModel = publishViewModel,
                    onPublishStarted = {
                        showPublishSheet = false
                        onPublishStarted(publishViewModel.uiState.value.publishState.let { 
                            if (it is com.clickpost.app.social.engine.PublishEngine.PublishState.Running) it.jobId else ""
                        })
                    },
                    onSkip = { showPublishSheet = false }
                )
            }
        }
    }
}

@Composable
private fun ExportInfoChip(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = Teal500
            )
        }
    }
}
