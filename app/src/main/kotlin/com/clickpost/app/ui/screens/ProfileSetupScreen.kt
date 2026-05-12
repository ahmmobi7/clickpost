package com.clickpost.app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import com.clickpost.app.ui.components.GradientButton
import com.clickpost.app.ui.components.SectionHeader
import com.clickpost.app.ui.components.UploadBox
import com.clickpost.app.ui.theme.Teal400
import com.clickpost.app.ui.theme.Teal500
import com.clickpost.app.viewmodel.MainViewModel

@UnstableApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    viewModel: MainViewModel,
    onSetupComplete: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val existingProfile = uiState.brandProfile

    var companyName by remember { mutableStateOf(existingProfile?.companyName ?: "") }
    var contactInfo  by remember { mutableStateOf(existingProfile?.contactInfo ?: "") }
    var logoUri      by remember { mutableStateOf<Uri?>(null) }
    var endClipUri   by remember { mutableStateOf<Uri?>(null) }
    var isSaving     by remember { mutableStateOf(false) }

    val hasLogo    = logoUri != null || existingProfile?.logoPath?.isNotBlank() == true
    val hasEndClip = endClipUri != null || existingProfile?.endClipPath?.isNotBlank() == true
    val canSave    = companyName.isNotBlank() && hasLogo

    // File pickers
    val logoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { logoUri = it } }

    val endClipPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { endClipUri = it } }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Teal400.copy(alpha = 0.12f), Color(0xFFF5F6F8))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Header
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically { -40 }
            ) {
                Column {
                    Text(
                        "Set up your brand",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "This will be applied to every video automatically",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(32.dp))
                }
            }

            // Logo upload
            SectionHeader("Brand logo *")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Logo preview circle
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Teal400.copy(alpha = 0.15f))
                ) {
                    if (logoUri != null) {
                        AsyncImage(
                            model = logoUri,
                            contentDescription = "Logo preview",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(CircleShape)
                        )
                    } else if (existingProfile?.logoPath?.isNotBlank() == true) {
                        AsyncImage(
                            model = existingProfile.logoPath,
                            contentDescription = "Current logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(CircleShape)
                        )
                    } else {
                        Icon(
                            Icons.Default.AddAPhoto,
                            contentDescription = null,
                            tint = Teal500,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    if (hasLogo) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Logo uploaded",
                            tint = Teal500,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(20.dp)
                                .background(Color.White, CircleShape)
                        )
                    }
                }

                UploadBox(
                    label = if (hasLogo) "Logo uploaded ✓" else "Upload PNG logo",
                    subLabel = "Recommended: 512×512px",
                    icon = {
                        Icon(
                            Icons.Default.AddAPhoto,
                            contentDescription = null,
                            tint = if (hasLogo) Teal500 else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    onClick = { logoPicker.launch("image/*") },
                    hasContent = hasLogo,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))

            // Company name
            SectionHeader("Company name *")
            OutlinedTextField(
                value = companyName,
                onValueChange = { companyName = it },
                placeholder = { Text("e.g. Acme Studios") },
                leadingIcon = {
                    Icon(Icons.Default.Business, contentDescription = null, tint = Teal500)
                },
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Teal500,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(Modifier.height(20.dp))

            // Contact info
            SectionHeader("Contact info")
            OutlinedTextField(
                value = contactInfo,
                onValueChange = { contactInfo = it },
                placeholder = { Text("Phone number, email or website") },
                leadingIcon = {
                    Icon(Icons.Default.Phone, contentDescription = null, tint = Teal500)
                },
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Teal500,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(Modifier.height(24.dp))

            // End clip
            SectionHeader("End clip (outro)")
            UploadBox(
                label = if (hasEndClip) "Outro clip uploaded ✓" else "Upload outro video",
                subLabel = if (hasEndClip) "Tap to replace" else "Max 30 seconds · MP4",
                icon = {
                    Icon(
                        Icons.Default.VideoLibrary,
                        contentDescription = null,
                        tint = if (hasEndClip) Teal500 else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                },
                onClick = { endClipPicker.launch("video/*") },
                hasContent = hasEndClip,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(40.dp))

            // Save CTA
            GradientButton(
                text = "Save & Continue",
                enabled = canSave,
                isLoading = isSaving,
                onClick = {
                    isSaving = true
                    viewModel.saveProfile(
                        companyName = companyName,
                        contactInfo = contactInfo,
                        logoUri = logoUri,
                        endClipUri = endClipUri
                    )
                    // Navigate after a brief moment to allow save to complete
                    isSaving = false
                    onSetupComplete()
                }
            )

            if (!canSave) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "* Company name and logo are required",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
