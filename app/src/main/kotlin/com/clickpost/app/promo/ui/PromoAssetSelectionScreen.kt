package com.clickpost.app.promo.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.clickpost.app.promo.viewmodel.PromoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromoAssetSelectionScreen(
    viewModel: PromoViewModel,
    onNext: (Int) -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedResolution by remember { mutableStateOf(1080) }

    val hookPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.selectHookVideo(it.toString()) }
    }
    val productPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        if (uris.isNotEmpty()) {
            viewModel.addProductAssets(uris.map { it.toString() })
        }
    }
    val modelPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.selectModelImage(it.toString()) }
    }
    val musicPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.selectMusic(it.toString(), "Selected Music") }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Assets") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                AssetSection(
                    title = "Hook Video",
                    onAdd = { hookPicker.launch("video/*") },
                    selectedUri = uiState.selectedHookVideo?.uri,
                    placeholderIcon = Icons.Default.VideoCall
                )
            }

            item {
                Text("Product Assets", style = MaterialTheme.typography.titleMedium)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(uiState.selectedProductAssets) { asset ->
                        Box(modifier = Modifier.size(100.dp)) {
                            AsyncImage(
                                model = asset.uri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = { viewModel.removeProductAsset(asset) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(24.dp)
                                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                    item {
                        OutlinedCard(
                            onClick = { productPicker.launch("image/*") },
                            modifier = Modifier.size(100.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Icon(Icons.Default.Add, contentDescription = "Add Product")
                            }
                        }
                    }
                }
            }

            item {
                AssetSection(
                    title = "Model Image",
                    onAdd = { modelPicker.launch("image/*") },
                    selectedUri = uiState.selectedModelImage?.uri,
                    placeholderIcon = Icons.Default.PhotoCamera
                )
            }

            item {
                AssetSection(
                    title = "Background Music (Optional)",
                    onAdd = { musicPicker.launch("audio/mpeg") },
                    selectedUri = uiState.selectedMusic?.uri,
                    placeholderIcon = Icons.Default.MusicNote,
                    displayText = uiState.selectedMusic?.name,
                    onClear = if (uiState.selectedMusic != null) { { viewModel.clearMusic() } } else null
                )
            }

            item {
                var expanded by remember { mutableStateOf(false) }
                ElevatedCard(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Quality & Timing Settings", style = MaterialTheme.typography.titleMedium)
                            Icon(
                                if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null
                            )
                        }
                        if (expanded) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Slide Duration: ${uiState.slideDurationS}s", style = MaterialTheme.typography.bodyMedium)
                            Slider(
                                value = uiState.slideDurationS.toFloat(),
                                onValueChange = { viewModel.updateSlideDuration(it.toInt()) },
                                valueRange = 0f..15f,
                                steps = 14
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Contrast: ${"%.1f".format(uiState.contrast)}", style = MaterialTheme.typography.bodyMedium)
                            Slider(
                                value = uiState.contrast,
                                onValueChange = { viewModel.updateContrast(it) },
                                valueRange = 0.5f..1.5f
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Sharpness: ${"%.1f".format(uiState.sharpness)}", style = MaterialTheme.typography.bodyMedium)
                            Slider(
                                value = uiState.sharpness,
                                onValueChange = { viewModel.updateSharpness(it) },
                                valueRange = 0f..1f
                            )
                        }
                    }
                }
            }

            item {
                Text("Export Resolution", style = MaterialTheme.typography.titleMedium)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ResolutionOption("1080p", 1080, selectedResolution == 1080) { selectedResolution = 1080 }
                    ResolutionOption("2K", 1440, selectedResolution == 1440) { selectedResolution = 1440 }
                    ResolutionOption("4K", 2160, selectedResolution == 2160) { selectedResolution = 2160 }
                }
            }

            item {
                Button(
                    onClick = { onNext(selectedResolution) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.selectedHookVideo != null && uiState.selectedProductAssets.isNotEmpty() && uiState.selectedModelImage != null
                ) {
                    Text("Generate Promo")
                }
            }
        }
    }
}

@Composable
fun ResolutionOption(label: String, height: Int, isSelected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(label) }
    )
}

@Composable
fun AssetSection(
    title: String,
    onAdd: () -> Unit,
    selectedUri: String?,
    placeholderIcon: androidx.compose.ui.graphics.vector.ImageVector,
    displayText: String? = null,
    onClear: (() -> Unit)? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            if (onClear != null) {
                TextButton(onClick = onClear) {
                    Text("Clear")
                }
            }
        }
        Card(
            onClick = onAdd,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                if (selectedUri != null) {
                    if (displayText != null) {
                        Text(displayText)
                    } else {
                        AsyncImage(
                            model = selectedUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    Icon(placeholderIcon, contentDescription = null, modifier = Modifier.size(48.dp))
                }
            }
        }
    }
}
