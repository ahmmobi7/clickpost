package com.clickpost.app.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.clickpost.app.data.model.BrandProfile
import com.clickpost.app.data.model.BrandingPreferences
import com.clickpost.app.data.model.ExportJob
import com.clickpost.app.data.model.LogoPosition
import com.clickpost.app.data.model.Resolution
import com.clickpost.app.data.model.UiState
import com.clickpost.app.data.repository.ProfileRepository
import com.clickpost.app.engine.BrandingEngine
import com.clickpost.app.engine.DeviceCapabilityChecker
import com.clickpost.app.engine.ExportEngine
import com.clickpost.app.engine.ExportState
import com.clickpost.app.engine.ShareEngine
import com.clickpost.app.engine.ShareTarget
import com.clickpost.app.storage.StorageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val profileRepo: ProfileRepository,
    private val storageManager: StorageManager,
    private val brandingEngine: BrandingEngine,
    private val exportEngine: ExportEngine,
    private val shareEngine: ShareEngine,
    private val deviceCapChecker: DeviceCapabilityChecker
) : AndroidViewModel(application) {

    // ── Internal mutable state ─────────────────────────────────────────────────
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    val exportState: StateFlow<ExportState> = exportEngine.state

    // ── Init: load persisted data ──────────────────────────────────────────────
    init {
        loadBrandedVideos()
        viewModelScope.launch {
            val caps = deviceCapChecker.query()
            profileRepo.profileFlow.collect { profile ->
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        isFirstLaunch = profile == null,
                        brandProfile = profile,
                        deviceCaps = caps
                    )
                }
            }
        }
        viewModelScope.launch {
            profileRepo.brandingPrefsFlow.collect { prefs ->
                _uiState.update { it.copy(brandingPrefs = prefs) }
            }
        }
    }

    // ── Profile actions ────────────────────────────────────────────────────────

    fun saveProfile(
        companyName: String,
        contactInfo: String,
        logoUri: Uri?,
        endClipUri: Uri?
    ) = viewModelScope.launch {
        var logoPath = _uiState.value.brandProfile?.logoPath ?: ""
        var endClipPath = _uiState.value.brandProfile?.endClipPath ?: ""

        // Copy assets to app storage if new ones were selected
        if (logoUri != null) {
            storageManager.copyUriToFile(logoUri, storageManager.logoFile)
            logoPath = storageManager.logoFile.absolutePath
        }
        if (endClipUri != null) {
            storageManager.copyUriToFile(endClipUri, storageManager.endClipFile)
            endClipPath = storageManager.endClipFile.absolutePath
        }

        val profile = BrandProfile(
            companyName = companyName.trim(),
            contactInfo = contactInfo.trim(),
            logoPath = logoPath,
            endClipPath = endClipPath
        )
        profileRepo.saveProfile(profile)
        _uiState.update { it.copy(brandProfile = profile, isFirstLaunch = false) }
        showSnackbar("Profile saved")
    }

    // ── Video selection ────────────────────────────────────────────────────────

    fun onVideoSelected(uri: Uri) {
        _uiState.update { it.copy(selectedVideoUri = uri) }
    }

    fun clearSelectedVideo() {
        _uiState.update { it.copy(selectedVideoUri = null) }
        exportEngine.reset()
    }

    // ── Branding preferences ───────────────────────────────────────────────────

    private fun updatePrefs(reducer: (BrandingPreferences) -> BrandingPreferences) {
        _uiState.update { state ->
            val newPrefs = reducer(state.brandingPrefs)
            state.copy(brandingPrefs = newPrefs)
        }
        val currentPrefs = _uiState.value.brandingPrefs
        viewModelScope.launch {
            profileRepo.saveBrandingPrefs(currentPrefs)
        }
    }

    fun setLogoPosition(position: LogoPosition) = updatePrefs { it.copy(logoPosition = position) }

    fun setLogoVisible(visible: Boolean) = updatePrefs { it.copy(showLogo = visible) }

    fun setContactInfoVisible(visible: Boolean) = updatePrefs { it.copy(showContactInfo = visible) }

    fun setVideoOverlayText(text: String) = updatePrefs { it.copy(videoOverlayText = text) }

    fun setVideoOverlayFont(font: String) = updatePrefs { it.copy(videoOverlayFont = font) }

    fun setVideoOverlayColor(color: String) = updatePrefs { it.copy(videoOverlayColor = color) }

    fun setCustomFileName(name: String) {
        _uiState.update { it.copy(customFileName = name) }
    }

    // ── Export ─────────────────────────────────────────────────────────────────

    fun startExport(resolution: Resolution) = viewModelScope.launch {
        val state = _uiState.value
        val videoUri = state.selectedVideoUri ?: return@launch
        val profile = state.brandProfile ?: return@launch

        val baseName = if (state.customFileName.isNotBlank()) state.customFileName else "branded_video"
        val outputFile = storageManager.newExportFile("${baseName}_${resolution.label}")

        val composition = brandingEngine.buildComposition(
            videoUri = videoUri,
            profile = profile,
            prefs = state.brandingPrefs,
            targetHeight = resolution.height,
            videoDurationUs = exportEngine.getVideoDurationUs(videoUri)
        )

        exportEngine.export(
            composition = composition,
            outputPath = outputFile.absolutePath,
            resolution = resolution,
            deviceCaps = state.deviceCaps,
            scope = viewModelScope
        )
    }

    fun cancelExport() {
        exportEngine.cancel()
    }

    fun resetExport() {
        exportEngine.reset()
        loadBrandedVideos()
    }

    // ── Branded Videos Management ──────────────────────────────────────────────

    fun loadBrandedVideos() {
        val videos = storageManager.getBrandedVideos()
        _uiState.update { it.copy(brandedVideos = videos) }
    }

    fun deleteBrandedVideo(path: String) {
        if (storageManager.deleteVideo(path)) {
            loadBrandedVideos()
            showSnackbar("Video deleted")
        }
    }

    // ── Share ──────────────────────────────────────────────────────────────────

    fun shareVideo(filePath: String, target: ShareTarget): android.content.Intent {
        return if (target == ShareTarget.GENERAL_DOWNLOAD) {
            shareEngine.buildChooserIntent(filePath)
        } else {
            shareEngine.buildShareIntent(filePath, target)
        }
    }

    val isInstagramInstalled: Boolean get() = shareEngine.isInstagramInstalled
    val isWhatsAppInstalled: Boolean get() = shareEngine.isWhatsAppInstalled

    // ── Snackbar ───────────────────────────────────────────────────────────────

    fun showSnackbar(message: String) {
        _uiState.update { it.copy(snackbarMessage = message) }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}
