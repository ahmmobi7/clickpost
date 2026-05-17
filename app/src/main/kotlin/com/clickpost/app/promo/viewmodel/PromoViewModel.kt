package com.clickpost.app.promo.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.clickpost.app.promo.data.*
import com.clickpost.app.promo.worker.PromoWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

data class PromoUiState(
    val hookVideos: List<HookVideo> = emptyList(),
    val productAssets: List<ProductAsset> = emptyList(),
    val modelImages: List<ModelImage> = emptyList(),
    val generatedPromos: List<GeneratedPromo> = emptyList(),
    val musicList: List<PromoMusic> = emptyList(),
    val selectedHookVideo: HookVideo? = null,
    val selectedProductAssets: List<ProductAsset> = emptyList(),
    val selectedModelImage: ModelImage? = null,
    val selectedMusic: PromoMusic? = null,
    val slideDurationS: Int = 5,
    val contrast: Float = 1.0f,
    val sharpness: Float = 0.0f,
    val isProcessing: Boolean = false,
    val exportProgress: Float = 0f,
    val errorMessage: String? = null,
    val productDescription: String = "",
    val selectedFont: String = "Default",
    val selectedColor: String = "#FFFFFF",
    val previewAssetUri: String? = null,
    val previewAssetType: String? = null // "Image" or "Video"
)

@HiltViewModel
class PromoViewModel @Inject constructor(
    private val repository: PromoRepository,
    private val workManager: WorkManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(PromoUiState())
    val uiState: StateFlow<PromoUiState> = _uiState.asStateFlow()

    val activeWorkInfo: StateFlow<List<WorkInfo>> = workManager
        .getWorkInfosByTagFlow("PromoWorker")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        combine(
            repository.getAllHookVideos(),
            repository.getAllProductAssets(),
            repository.getAllModelImages(),
            repository.getAllGeneratedPromos(),
            repository.getAllMusic()
        ) { hook, product, model, generated, music ->
            _uiState.update { state ->
                state.copy(
                    hookVideos = hook,
                    productAssets = product,
                    modelImages = model,
                    generatedPromos = generated,
                    musicList = music,
                    // Auto-select the latest if none selected
                    selectedHookVideo = state.selectedHookVideo ?: hook.lastOrNull(),
                    selectedModelImage = state.selectedModelImage ?: model.lastOrNull(),
                    selectedMusic = state.selectedMusic ?: music.lastOrNull()
                )
            }
        }.launchIn(viewModelScope)

        // Monitor work status
        activeWorkInfo.onEach { workInfos ->
            val isProcessing = workInfos.any { it.state == WorkInfo.State.ENQUEUED || it.state == WorkInfo.State.RUNNING }
            _uiState.update { it.copy(isProcessing = isProcessing) }
        }.launchIn(viewModelScope)
    }

    fun selectHookVideo(uriString: String) {
        viewModelScope.launch {
            val internalUri = copyToInternalStorage(uriString, "hook_video.mp4")
            val hook = HookVideo(uri = internalUri)
            repository.insertHookVideo(hook)
            _uiState.update { it.copy(selectedHookVideo = hook) }
        }
    }

    fun addProductAssets(uriStrings: List<String>) {
        viewModelScope.launch {
            val newAssets = uriStrings.map { uriString ->
                val fileName = "product_asset_${System.currentTimeMillis()}_${uriString.hashCode()}.png"
                val internalUri = copyToInternalStorage(uriString, fileName)
                val asset = ProductAsset(uri = internalUri, type = "Image", description = "Product Image")
                repository.insertProductAsset(asset)
                asset
            }
            _uiState.update { it.copy(selectedProductAssets = it.selectedProductAssets + newAssets) }
        }
    }

    fun removeProductAsset(asset: ProductAsset) {
        _uiState.update { it.copy(selectedProductAssets = it.selectedProductAssets - asset) }
    }

    fun selectModelImage(uriString: String) {
        viewModelScope.launch {
            val internalUri = copyToInternalStorage(uriString, "model_image.png")
            val model = ModelImage(uri = internalUri)
            repository.insertModelImage(model)
            _uiState.update { it.copy(selectedModelImage = model) }
        }
    }

    fun selectMusic(uriString: String, name: String) {
        viewModelScope.launch {
            val internalUri = copyToInternalStorage(uriString, "background_music.mp3")
            val music = PromoMusic(uri = internalUri, name = name)
            repository.insertMusic(music)
            _uiState.update { it.copy(selectedMusic = music) }
        }
    }

    fun clearMusic() {
        _uiState.update { it.copy(selectedMusic = null) }
    }

    fun updateSlideDuration(durationS: Int) {
        _uiState.update { it.copy(slideDurationS = durationS) }
    }

    fun updateContrast(contrast: Float) {
        _uiState.update { it.copy(contrast = contrast) }
    }

    fun updateSharpness(sharpness: Float) {
        _uiState.update { it.copy(sharpness = sharpness) }
    }

    fun updateProductDescription(description: String) {
        _uiState.update { it.copy(productDescription = description) }
    }

    fun updateSelectedFont(font: String) {
        _uiState.update { it.copy(selectedFont = font) }
    }

    fun updateSelectedColor(colorHex: String) {
        _uiState.update { it.copy(selectedColor = colorHex) }
    }

    fun setPreviewAsset(uri: String?, type: String?) {
        _uiState.update { it.copy(previewAssetUri = uri, previewAssetType = type) }
    }

    private fun copyToInternalStorage(uriString: String, fileName: String): String {
        val uri = Uri.parse(uriString)
        val destFile = File(context.filesDir, "promo_assets/$fileName")
        destFile.parentFile?.mkdirs()
        
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(destFile).use { output ->
                input.copyTo(output)
            }
        } ?: return uriString // Fallback to original if copy fails
        return "file://${destFile.absolutePath}"
    }

    fun deletePromo(promo: GeneratedPromo) {
        viewModelScope.launch {
            repository.deleteGeneratedPromo(promo)
        }
    }

    fun startPromoGeneration(resolutionHeight: Int) {
        val uiState = _uiState.value
        val hookUri = uiState.selectedHookVideo?.uri ?: return
        val productUris = uiState.selectedProductAssets.map { it.uri }.toTypedArray()
        if (productUris.isEmpty()) return
        val modelUri = uiState.selectedModelImage?.uri ?: return
        val musicUri = uiState.selectedMusic?.uri

        val workRequest = OneTimeWorkRequestBuilder<PromoWorker>()
            .setInputData(workDataOf(
                "hookUri" to hookUri,
                "productUris" to productUris,
                "modelUri" to modelUri,
                "musicUri" to musicUri,
                "description" to uiState.productDescription,
                "selectedFont" to uiState.selectedFont,
                "selectedColor" to uiState.selectedColor,
                "resolutionHeight" to resolutionHeight,
                "slideDurationS" to uiState.slideDurationS,
                "contrast" to uiState.contrast,
                "sharpness" to uiState.sharpness,
                "productAssetIds" to uiState.selectedProductAssets.map { it.id }.toLongArray()
            ))
            .build()

        workManager.enqueue(workRequest)
    }
}
