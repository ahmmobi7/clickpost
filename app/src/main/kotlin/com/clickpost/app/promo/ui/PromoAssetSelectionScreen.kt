package com.clickpost.app.promo.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.VideoCall
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
    val productPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.addProductAsset(it.toString(), "Image", "Product Image") }
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
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.selectedProductAssets) { asset ->
                        AsyncImage(
                            model = asset.uri,
                            contentDescription = null,
                            modifier = Modifier.size(100.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    item {
                        IconButton(
                            onClick = { productPicker.launch("image/*") },
                            modifier = Modifier.size(100.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Product")
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
                    title = "Background Music",
                    onAdd = { musicPicker.launch("audio/mpeg") },
                    selectedUri = uiState.selectedMusic?.uri,
                    placeholderIcon = Icons.Default.MusicNote,
                    displayText = uiState.selectedMusic?.name
                )
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
    displayText: String? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium)
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
