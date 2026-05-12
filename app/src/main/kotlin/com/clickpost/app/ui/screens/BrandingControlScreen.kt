package com.clickpost.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatAlignRight
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
import com.clickpost.app.data.model.LogoPosition
import com.clickpost.app.ui.components.ClickPostCard
import com.clickpost.app.ui.components.GradientButton
import com.clickpost.app.ui.components.PositionChip
import com.clickpost.app.ui.components.SectionHeader
import com.clickpost.app.ui.theme.Teal400
import com.clickpost.app.ui.theme.Teal500
import com.clickpost.app.viewmodel.MainViewModel

@UnstableApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandingControlScreen(
    viewModel: MainViewModel,
    onExportClicked: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val prefs   = uiState.brandingPrefs
    val profile = uiState.brandProfile
    val videoUri = uiState.selectedVideoUri

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Branding controls", fontWeight = FontWeight.SemiBold)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color(0xFFF5F6F8)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ── Video preview ────────────────────────────────────────────────
            videoUri?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black)
                ) {
                    AsyncImage(
                        model = it,
                        contentDescription = "Video preview",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Simulated branding overlay preview
                    BrandOverlayPreview(
                        profile = profile,
                        logoPosition = prefs.logoPosition,
                        showContact = prefs.showContactInfo
                    )
                }
            }

            // ── Logo branding ────────────────────────────────────────────────
            ClickPostCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SectionHeader("Show Logo Overlay")
                    Switch(
                        checked = prefs.showLogo,
                        onCheckedChange = { viewModel.setLogoVisible(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = Teal500)
                    )
                }

                if (prefs.showLogo) {
                    Spacer(Modifier.height(8.dp))
                    Text("Logo position", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 8.dp)) {
                        PositionChip(
                            label = "Top left",
                            selected = prefs.logoPosition == LogoPosition.TOP_LEFT,
                            onClick = { viewModel.setLogoPosition(LogoPosition.TOP_LEFT) },
                            modifier = Modifier.weight(1f)
                        )
                        PositionChip(
                            label = "Top right",
                            selected = prefs.logoPosition == LogoPosition.TOP_RIGHT,
                            onClick = { viewModel.setLogoPosition(LogoPosition.TOP_RIGHT) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // ── Contact info toggle ──────────────────────────────────────────
            ClickPostCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Show contact info",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        if (profile?.contactInfo?.isNotBlank() == true) {
                            Text(
                                text = profile.contactInfo,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Text(
                                "No contact info set",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Switch(
                        checked = prefs.showContactInfo,
                        onCheckedChange = { viewModel.setContactInfoVisible(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = Teal500)
                    )
                }
            }

            // ── Video Highlight Text ─────────────────────────────────────────
            var showHighlightText by remember { mutableStateOf(prefs.videoOverlayText.isNotBlank()) }

            ClickPostCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SectionHeader("Video Highlight Text (First 15s)")
                    Switch(
                        checked = showHighlightText,
                        onCheckedChange = { 
                            showHighlightText = it
                            if (!it) viewModel.setVideoOverlayText("")
                        },
                        colors = SwitchDefaults.colors(checkedThumbColor = Teal500)
                    )
                }

                if (showHighlightText) {
                    var textState by remember { mutableStateOf(prefs.videoOverlayText) }
                    
                    // Update local state when remote state changes (e.g., initial load or reset)
                    LaunchedEffect(prefs.videoOverlayText) {
                        if (textState != prefs.videoOverlayText) {
                            textState = prefs.videoOverlayText
                        }
                    }

                    OutlinedTextField(
                        value = textState,
                        onValueChange = { newValue ->
                            textState = newValue
                            viewModel.setVideoOverlayText(newValue)
                        },
                        label = { Text("Enter text for video") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    
                    Spacer(Modifier.height(16.dp))
                    
                    Text("Select Font", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val fonts = listOf("Impact", "Bebas", "Modern")
                        fonts.forEach { font ->
                            FilterChip(
                                selected = prefs.videoOverlayFont == font,
                                onClick = { viewModel.setVideoOverlayFont(font) },
                                label = { Text(font) }
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    
                    Text("Select Color", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val colors = listOf(
                            "#FFFFFF" to Color.White,
                            "#39FF14" to Color(0xFF39FF14),
                            "#FFD700" to Color(0xFFFFD700), // Gold
                            "#FF3131" to Color(0xFFFF3131), // Neon Red
                            "#1F51FF" to Color(0xFF1F51FF)  // Neon Blue
                        )
                        colors.forEach { (hex, composeColor) ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(androidx.compose.foundation.shape.CircleShape)
                                    .background(composeColor)
                                    .border(
                                        width = if (prefs.videoOverlayColor == hex) 2.dp else 0.dp,
                                        color = if (prefs.videoOverlayColor == hex) Teal500 else Color.Transparent,
                                        shape = androidx.compose.foundation.shape.CircleShape
                                    )
                                    .clickable { viewModel.setVideoOverlayColor(hex) }
                            )
                        }
                    }
                }
            }

            // ── Branding summary ─────────────────────────────────────────────
            ClickPostCard {
                Text(
                    "What will be added",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(12.dp))
                BrandingSummaryRow("Logo overlay", "Positioned ${if (prefs.logoPosition == LogoPosition.TOP_LEFT) "top-left" else "top-right"}", true)
                BrandingSummaryRow("Company name", profile?.companyName ?: "—", profile?.companyName?.isNotBlank() == true)
                BrandingSummaryRow("Contact info", profile?.contactInfo ?: "—", prefs.showContactInfo && profile?.contactInfo?.isNotBlank() == true)
                BrandingSummaryRow("End clip", if (profile?.endClipPath?.isNotBlank() == true) "Outro will be appended" else "No outro set", profile?.endClipPath?.isNotBlank() == true)
            }

            // ── Export Settings ─────────────────────────────────────────────────────
            ClickPostCard {
                SectionHeader("Export Settings")
                OutlinedTextField(
                    value = uiState.customFileName,
                    onValueChange = { viewModel.setCustomFileName(it) },
                    label = { Text("Custom File Name") },
                    placeholder = { Text("e.g. MyBrandedVideo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            // ── Export CTA ───────────────────────────────────────────────────
            GradientButton(
                text = "Export & Brand",
                onClick = onExportClicked,
                enabled = uiState.canExport
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun BrandingPreviewOverlay(
    logoPath: String?,
    companyName: String?,
    showContact: Boolean,
    contactInfo: String?,
    logoPosition: LogoPosition
) {
    // Visual preview of where overlays will appear on the video
    // Baseline: 15cm height, 8.4cm width.
    // 1dp is roughly 0.04cm on a standard high-density screen, 
    // but here we just want to show the relative positioning.
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Logo badge: 2cm top, 4cm side
        if (logoPath?.isNotBlank() == true) {
            val horizontalBias = if (logoPosition == LogoPosition.TOP_LEFT) (4f / 8.4f) else 1f - (4f / 8.4f)
            val verticalBias = (2f / 15f)
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = if (logoPosition == LogoPosition.TOP_LEFT) 40.dp else 0.dp,
                        end = if (logoPosition == LogoPosition.TOP_RIGHT) 40.dp else 0.dp,
                        top = 16.dp
                    )
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White.copy(alpha = 0.85f))
                        .align(if (logoPosition == LogoPosition.TOP_LEFT) Alignment.TopStart else Alignment.TopEnd)
                ) {
                    AsyncImage(
                        model = logoPath,
                        contentDescription = "Logo overlay preview",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize().padding(4.dp)
                    )
                }
            }
        }

        // Contact info: Increased left margin by 2cm (~50dp additional) and bottom by 3cm
        if (showContact || companyName?.isNotBlank() == true) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 70.dp, bottom = 115.dp)
            ) {
                companyName?.takeIf { it.isNotBlank() }?.let { name ->
                    Text(
                        text = name,
                        color = Color(0xFF39FF14), // Neon Green
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        style = LocalTextStyle.current.copy(
                            shadow = androidx.compose.ui.graphics.Shadow(
                                color = Color.Black,
                                blurRadius = 4f
                            )
                        )
                    )
                }
                if (showContact) {
                    contactInfo?.takeIf { it.isNotBlank() }?.let { contact ->
                        Text(
                            text = contact,
                            color = Color(0xFF39FF14), // Neon Green
                            fontSize = 8.sp,
                            style = LocalTextStyle.current.copy(
                                shadow = androidx.compose.ui.graphics.Shadow(
                                    color = Color.Black,
                                    blurRadius = 4f
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BrandOverlayPreview(
    profile: com.clickpost.app.data.model.BrandProfile?,
    logoPosition: LogoPosition,
    showContact: Boolean
) {
    BrandingPreviewOverlay(
        logoPath = profile?.logoPath,
        companyName = profile?.companyName,
        showContact = showContact,
        contactInfo = profile?.contactInfo,
        logoPosition = logoPosition
    )
}

@Composable
private fun BrandingSummaryRow(label: String, value: String, active: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = if (active) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        if (active) Teal500 else MaterialTheme.colorScheme.outline,
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
        }
    }
}
