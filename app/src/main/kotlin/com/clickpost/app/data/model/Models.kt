package com.clickpost.app.data.model

import android.net.Uri

// ─── Enums ──────────────────────────────────────────────────────────────────

enum class LogoPosition { TOP_LEFT, TOP_RIGHT }

enum class Resolution(val label: String, val height: Int) {
    HD_720("720p", 720),
    FHD_1080("1080p", 1080),
    QHD_2K("2K", 1440),
    UHD_4K("4K", 2160)
}

enum class ExportStatus { IDLE, QUEUED, PROCESSING, DONE, ERROR }

// ─── Core Data Classes ───────────────────────────────────────────────────────

data class BrandProfile(
    val companyName: String = "",
    val contactInfo: String = "",
    val logoPath: String = "",          // absolute path to logo.png
    val endClipPath: String = "",       // absolute path to outro.mp4
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val isComplete: Boolean
        get() = companyName.isNotBlank() && logoPath.isNotBlank()
}

data class BrandingPreferences(
    val showLogo: Boolean = true,
    val logoPosition: LogoPosition = LogoPosition.TOP_LEFT,
    val showContactInfo: Boolean = true,
    val logoScaleRatio: Float = 0.15f,   // logo width as fraction of video width
    val overlayPaddingRatio: Float = 0.03f,
    val videoOverlayText: String = "",
    val videoOverlayFont: String = "Impact",
    val videoOverlayColor: String = "#FFFFFF"
)

data class DeviceCapabilities(
    val supportedResolutions: List<Resolution> = listOf(Resolution.HD_720, Resolution.FHD_1080),
    val supportsH265: Boolean = false,
    val hardwareEncoderAvailable: Boolean = true
)

data class ExportJob(
    val id: String = java.util.UUID.randomUUID().toString(),
    val inputVideoUri: Uri = Uri.EMPTY,
    val profile: BrandProfile = BrandProfile(),
    val preferences: BrandingPreferences = BrandingPreferences(),
    val targetResolution: Resolution = Resolution.FHD_1080,
    val status: ExportStatus = ExportStatus.IDLE,
    val progressPercent: Float = 0f,
    val outputPath: String = "",
    val errorMessage: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

// ─── UI State ────────────────────────────────────────────────────────────────

data class BrandedVideo(
    val id: String = java.util.UUID.randomUUID().toString(),
    val path: String,
    val fileName: String,
    val sizeBytes: Long,
    val createdAt: Long
)

data class UiState(
    val isLoading: Boolean = true,
    val isFirstLaunch: Boolean = true,
    val brandProfile: BrandProfile? = null,
    val selectedVideoUri: Uri? = null,
    val brandingPrefs: BrandingPreferences = BrandingPreferences(),
    val deviceCaps: DeviceCapabilities = DeviceCapabilities(),
    val exportJob: ExportJob? = null,
    val snackbarMessage: String? = null,
    val brandedVideos: List<BrandedVideo> = emptyList(),
    val customFileName: String = ""
) {
    val hasProfile: Boolean get() = brandProfile?.isComplete == true
    val canExport: Boolean get() = selectedVideoUri != null && hasProfile
}
