package com.clickpost.app.promo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.clickpost.app.promo.data.*
import com.clickpost.app.promo.worker.PromoWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
    val isProcessing: Boolean = false,
    val exportProgress: Float = 0f,
    val errorMessage: String? = null
)

@HiltViewModel
class PromoViewModel @Inject constructor(
    private val repository: PromoRepository,
    private val workManager: WorkManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(PromoUiState())
    val uiState: StateFlow<PromoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.getAllHookVideos(),
                repository.getAllProductAssets(),
                repository.getAllModelImages(),
                repository.getAllGeneratedPromos(),
                repository.getAllMusic()
            ) { hook, product, model, generated, music ->
                PromoUiState(
                    hookVideos = hook,
                    productAssets = product,
                    modelImages = model,
                    generatedPromos = generated,
                    musicList = music
                )
            }.collect {
                _uiState.value = it
            }
        }
    }

    fun selectHookVideo(uri: String) {
        viewModelScope.launch {
            val hook = HookVideo(uri = uri)
            repository.insertHookVideo(hook)
            _uiState.update { it.copy(selectedHookVideo = hook) }
        }
    }

    fun addProductAsset(uri: String, type: String, description: String) {
        viewModelScope.launch {
            val asset = ProductAsset(uri = uri, type = type, description = description)
            repository.insertProductAsset(asset)
            _uiState.update { it.copy(selectedProductAssets = it.selectedProductAssets + asset) }
        }
    }

    fun selectModelImage(uri: String) {
        viewModelScope.launch {
            val model = ModelImage(uri = uri)
            repository.insertModelImage(model)
            _uiState.update { it.copy(selectedModelImage = model) }
        }
    }

    fun selectMusic(uri: String, name: String) {
        viewModelScope.launch {
            val music = PromoMusic(uri = uri, name = name)
            repository.insertMusic(music)
            _uiState.update { it.copy(selectedMusic = music) }
        }
    }

    fun deletePromo(promo: GeneratedPromo) {
        viewModelScope.launch {
            repository.deleteGeneratedPromo(promo)
        }
    }

    fun startPromoGeneration(description: String, resolutionHeight: Int) {
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
                "description" to description,
                "resolutionHeight" to resolutionHeight
            ))
            .build()

        workManager.enqueue(workRequest)
    }
}
