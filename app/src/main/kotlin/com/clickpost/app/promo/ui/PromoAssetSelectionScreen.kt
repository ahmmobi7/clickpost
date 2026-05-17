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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.clickpost.app.promo.viewmodel.PromoViewModel
import com.clickpost.app.ui.components.SectionHeader
import androidx.compose.material3.HorizontalDivider as Divider

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
        uri?.let {
            viewModel.selectHookVideo(it.toString())
            viewModel.setPreviewAsset(it.toString(), "Video")
        }
    }
    val productPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        if (uris.isNotEmpty()) {
            viewModel.addProductAssets(uris.map { it.toString() })
            viewModel.setPreviewAsset(uris.first().toString(), "Image")
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
                title = { Text("Enhance Promo Video") },
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
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Preview Section
            DynamicPreview(
                assetUri = uiState.previewAssetUri,
                assetType = uiState.previewAssetType,
                description = uiState.productDescription,
                font = uiState.selectedFont,
                colorHex = uiState.selectedColor
            )

            // Description Input
            OutlinedTextField(
                value = uiState.productDescription,
                onValueChange = { viewModel.updateProductDescription(it) },
                label = { Text("Product Description") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter text for your product") }
            )

            // Font & Color Customization
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Customize Text Appearance", style = MaterialTheme.typography.titleMedium)

                // Font Selector
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedCard(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Font: ${uiState.selectedFont}")
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        val fonts = listOf("Default", "Impact", "Bebas", "Modern")
                        fonts.forEach { font ->
                            DropdownMenuItem(
                                text = { Text(font) },
                                onClick = {
                                    viewModel.updateSelectedFont(font)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Color Picker
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val colors = listOf(
                        "#FFFFFF" to Color.White,
                        "#000000" to Color.Black,
                        "#FF0000" to Color.Red,
                        "#00FF00" to Color.Green,
                        "#0000FF" to Color.Blue,
                        "#FFFF00" to Color.Yellow,
                        "#FF00FF" to Color.Magenta,
                        "#00FFFF" to Color.Cyan
                    )
                    items(colors) { (hex, color) ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (uiState.selectedColor == hex) 3.dp else 1.dp,
                                    color = if (uiState.selectedColor == hex) MaterialTheme.colorScheme.primary else Color.Gray,
                                    shape = CircleShape
                                )
                                .clickable { viewModel.updateSelectedColor(hex) }
                        )
                    }
                }
            }

            Divider()

            AssetSection(
                title = "Hook Video",
                onAdd = { hookPicker.launch("video/*") },
                selectedUri = uiState.selectedHookVideo?.uri,
                placeholderIcon = Icons.Default.VideoCall,
                onClick = { uiState.selectedHookVideo?.let { viewModel.setPreviewAsset(it.uri, "Video") } }
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionHeader("Product Assets")
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(uiState.selectedProductAssets) { asset ->
                        Box(modifier = Modifier.size(100.dp).clickable { viewModel.setPreviewAsset(asset.uri, "Image") }) {
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

            AssetSection(
                title = "Model Image",
                onAdd = { modelPicker.launch("image/*") },
                selectedUri = uiState.selectedModelImage?.uri,
                placeholderIcon = Icons.Default.PhotoCamera,
                onClick = { uiState.selectedModelImage?.let { viewModel.setPreviewAsset(it.uri, "Image") } }
            )

            AssetSection(
                title = "Background Music (Optional)",
                onAdd = { musicPicker.launch("audio/mpeg") },
                selectedUri = uiState.selectedMusic?.uri,
                placeholderIcon = Icons.Default.MusicNote,
                displayText = uiState.selectedMusic?.name,
                onClear = if (uiState.selectedMusic != null) { { viewModel.clearMusic() } } else null
            )

            var settingsExpanded by remember { mutableStateOf(false) }
            ElevatedCard(
                onClick = { settingsExpanded = !settingsExpanded },
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
                            if (settingsExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null
                        )
                    }
                    if (settingsExpanded) {
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

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Export Resolution", style = MaterialTheme.typography.titleMedium)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    ResolutionOption("1080p", 1080, selectedResolution == 1080) { selectedResolution = 1080 }
                    ResolutionOption("2K", 1440, selectedResolution == 1440) { selectedResolution = 1440 }
                    ResolutionOption("4K", 2160, selectedResolution == 2160) { selectedResolution = 2160 }
                }
            }

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

@Composable
fun DynamicPreview(
    assetUri: String?,
    assetType: String?,
    description: String,
    font: String,
    colorHex: String
) {
    val composeColor = try { Color(android.graphics.Color.parseColor(colorHex)) } catch (e: Exception) { Color.White }
    val fontFamily = when (font.lowercase()) {
        "impact" -> FontFamily.SansSerif
        "bebas" -> FontFamily.Serif
        "modern" -> FontFamily.Monospace
        else -> FontFamily.Default
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Preview", style = MaterialTheme.typography.labelMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (assetType == "Image") {
            // Text positioned structurally ABOVE image
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = description,
                    color = composeColor,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    if (assetUri != null) {
                        AsyncImage(
                            model = assetUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Box(Modifier.fillMaxSize().background(Color.Gray.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                            Text("No Image Selected", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        } else {
            // Text OVERLAY 15% above bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.DarkGray)
            ) {
                if (assetUri != null) {
                    AsyncImage(
                        model = assetUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Box(Modifier.fillMaxSize().background(Color.Gray.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                        Text("No Video Selected", style = MaterialTheme.typography.bodySmall)
                    }
                }

                // 15% from bottom -> Alignment(0f, 0.7f)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment(0f, 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = description,
                        color = composeColor,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
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
    onClear: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
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
            onClick = onClick ?: onAdd,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                if (selectedUri != null) {
                    if (onClick != null) {
                        IconButton(
                            onClick = onAdd,
                            modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Change", tint = Color.White)
                        }
                    }
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
