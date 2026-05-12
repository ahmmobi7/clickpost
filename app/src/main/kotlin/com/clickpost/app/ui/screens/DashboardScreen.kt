package com.clickpost.app.ui.screens

import android.net.Uri
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import androidx.compose.ui.platform.LocalContext
import com.clickpost.app.engine.ShareTarget
import com.clickpost.app.ui.components.ClickPostCard
import com.clickpost.app.ui.components.GradientButton
import com.clickpost.app.ui.theme.Teal400
import com.clickpost.app.ui.theme.Teal500
import com.clickpost.app.viewmodel.MainViewModel

@UnstableApi
@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    onVideoSelected: () -> Unit,
    onEditProfile: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val profile = uiState.brandProfile
    val brandedVideos = uiState.brandedVideos
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Video picker launcher
    val videoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.onVideoSelected(it)
            onVideoSelected()
        }
    }

    // Snackbar handling
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF5F6F8)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // ── Gradient header ──────────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Teal400.copy(alpha = 0.25f), Color(0xFFF5F6F8))
                            )
                        )
                        .padding(horizontal = 24.dp, vertical = 28.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "ClickPost",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = Teal500
                            )
                            if (profile != null) {
                                Text(
                                    text = profile.companyName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Profile avatar / edit button
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Teal500.copy(alpha = 0.15f))
                                .clickable(onClick = onEditProfile)
                        ) {
                            if (profile?.logoPath?.isNotBlank() == true) {
                                AsyncImage(
                                    model = profile.logoPath,
                                    contentDescription = "Brand logo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize().clip(CircleShape)
                                )
                            } else {
                                Icon(
                                    Icons.Default.EditNote,
                                    contentDescription = "Edit profile",
                                    tint = Teal500,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }

            // ── Main content ─────────────────────────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Spacer(Modifier.height(4.dp))

                    // Brand profile summary card
                    AnimatedVisibility(visible = profile != null, enter = fadeIn()) {
                        profile?.let { p ->
                            ClickPostCard {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Teal400.copy(alpha = 0.15f))
                                    ) {
                                        if (p.logoPath.isNotBlank()) {
                                            AsyncImage(
                                                model = p.logoPath,
                                                contentDescription = "Logo",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                                    .clip(RoundedCornerShape(10.dp))
                                            )
                                        }
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = p.companyName,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        if (p.contactInfo.isNotBlank()) {
                                            Text(
                                                text = p.contactInfo,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                    IconButton(onClick = onEditProfile) {
                                        Icon(
                                            Icons.Default.EditNote,
                                            contentDescription = "Edit profile",
                                            tint = Teal500
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Video upload zone
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically { 60 }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Teal400.copy(alpha = 0.08f), Teal500.copy(alpha = 0.05f))
                                    )
                                )
                                .border(2.dp, Teal400.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                                .clickable { videoPicker.launch("video/*") }
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(Teal400.copy(alpha = 0.15f), CircleShape)
                                ) {
                                    Icon(
                                        Icons.Default.VideoCall,
                                        contentDescription = null,
                                        tint = Teal500,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    "Tap to select a video",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Teal500
                                )
                            }
                        }
                    }

                    if (brandedVideos.isNotEmpty()) {
                        Text(
                            text = "Your Branded Videos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            items(brandedVideos) { video ->
                Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)) {
                    ClickPostCard {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.Black)
                            ) {
                                // Simple placeholder for video icon
                                Icon(
                                    Icons.Default.PlayCircle,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = video.fileName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1
                                )
                                Text(
                                    text = "${video.sizeBytes / (1024 * 1024)} MB • ${SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date(video.createdAt))}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            IconButton(onClick = { 
                                val intent = viewModel.shareVideo(video.path, ShareTarget.GENERAL_DOWNLOAD)
                                context.startActivity(intent)
                            }) {
                                Icon(
                                    Icons.Default.Share,
                                    contentDescription = "Share",
                                    tint = Teal500
                                )
                            }

                            IconButton(onClick = { viewModel.deleteBrandedVideo(video.path) }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Spacer(Modifier.height(8.dp))
                    
                    // How it works info cards
                    Text(
                        text = "How it works",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StepCard("1", "Pick a video", "From gallery", Modifier.weight(1f))
                        StepCard("2", "Set position", "Choose logo", Modifier.weight(1f))
                        StepCard("3", "Export", "Ready to share", Modifier.weight(1f))
                    }

                    Spacer(Modifier.height(8.dp))

                    GradientButton(
                        text = "Select Video",
                        onClick = { videoPicker.launch("video/*") },
                        enabled = uiState.hasProfile
                    )

                    if (!uiState.hasProfile) {
                        Text(
                            text = "Complete your profile setup first",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepCard(number: String, title: String, subtitle: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = number,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Teal500
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
