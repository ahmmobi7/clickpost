package com.clickpost.app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import com.clickpost.app.ui.components.GradientButton
import com.clickpost.app.ui.components.OutlinedClickButton
import com.clickpost.app.ui.components.SectionHeader
import com.clickpost.app.ui.components.UploadBox
import com.clickpost.app.ui.theme.Teal500
import com.clickpost.app.viewmodel.MainViewModel

@UnstableApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
    viewModel: MainViewModel,
    onManageAccounts: () -> Unit,
    onSaved: () -> Unit,
    onBack: () -> Unit
) {
    val uiState  = viewModel.uiState.collectAsState().value
    val existing = uiState.brandProfile

    var companyName by remember { mutableStateOf(existing?.companyName ?: "") }
    var contactInfo  by remember { mutableStateOf(existing?.contactInfo ?: "") }
    var newLogoUri   by remember { mutableStateOf<Uri?>(null) }
    var newClipUri   by remember { mutableStateOf<Uri?>(null) }
    var isSaving     by remember { mutableStateOf(false) }
    var showResetConfirm by remember { mutableStateOf(false) }

    val hasLogo    = newLogoUri != null || existing?.logoPath?.isNotBlank() == true
    val hasEndClip = newClipUri != null || existing?.endClipPath?.isNotBlank() == true
    val canSave    = companyName.isNotBlank() && hasLogo

    val logoPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { newLogoUri = it }
    }
    val clipPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { newClipUri = it }
    }

    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = { Text("Reset profile?") },
            text = { Text("This will clear all branding assets and return you to setup.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetConfirm = false
                        onSaved()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { Text("Reset") }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit profile", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ── Logo section ─────────────────────────────────────────────────
            SectionHeader("Brand logo *")
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Teal500.copy(alpha = 0.1f))
                ) {
                    val logoDisplayUri = newLogoUri ?: existing?.logoPath?.let { if (it.isNotBlank()) it else null }
                    if (logoDisplayUri != null) {
                        AsyncImage(
                            model = logoDisplayUri,
                            contentDescription = "Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(CircleShape)
                        )
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Teal500,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(20.dp)
                                .background(Color.White, CircleShape)
                        )
                    } else {
                        Icon(
                            Icons.Default.AddAPhoto,
                            contentDescription = null,
                            tint = Teal500,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                UploadBox(
                    label = if (hasLogo) "Tap to replace logo" else "Upload PNG logo",
                    subLabel = "Recommended: 512×512px",
                    icon = {
                        Icon(
                            Icons.Default.AddAPhoto,
                            contentDescription = null,
                            tint = if (hasLogo) Teal500 else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    onClick = { logoPicker.launch("image/*") },
                    hasContent = hasLogo,
                    modifier = Modifier.weight(1f)
                )
            }

            // ── Company name ─────────────────────────────────────────────────
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

            // ── Contact info ─────────────────────────────────────────────────
            SectionHeader("Contact info")
            OutlinedTextField(
                value = contactInfo,
                onValueChange = { contactInfo = it },
                placeholder = { Text("Phone, email or website") },
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

            // ── Social Publish ───────────────────────────────────────────────
            SectionHeader("Social Publish")
            OutlinedClickButton(
                text = "Manage publish accounts",
                onClick = onManageAccounts,
                modifier = Modifier.fillMaxWidth()
            )

            // ── End clip ─────────────────────────────────────────────────────
            SectionHeader("End clip (outro)")
            UploadBox(
                label = if (hasEndClip) "Outro uploaded — tap to replace" else "Upload outro video",
                subLabel = if (hasEndClip) "Current outro will be overwritten" else "Max 30s · MP4",
                icon = {
                    Icon(
                        Icons.Default.VideoLibrary,
                        contentDescription = null,
                        tint = if (hasEndClip) Teal500 else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(22.dp)
                    )
                },
                onClick = { clipPicker.launch("video/*") },
                hasContent = hasEndClip,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // ── Actions ──────────────────────────────────────────────────────
            GradientButton(
                text = "Save Changes",
                enabled = canSave,
                isLoading = isSaving,
                onClick = {
                    isSaving = true
                    viewModel.saveProfile(
                        companyName = companyName,
                        contactInfo = contactInfo,
                        logoUri = newLogoUri,
                        endClipUri = newClipUri
                    )
                    isSaving = false
                    onSaved()
                }
            )

            OutlinedClickButton(
                text = "Discard changes",
                onClick = onBack
            )

            TextButton(
                onClick = { showResetConfirm = true },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Reset profile")
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
