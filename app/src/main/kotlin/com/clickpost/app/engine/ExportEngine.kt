package com.clickpost.app.engine

import android.content.Context
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.transformer.Composition
import androidx.media3.transformer.ExportException
import androidx.media3.transformer.ExportResult
import androidx.media3.transformer.ProgressHolder
import androidx.media3.transformer.Transformer
import androidx.media3.transformer.TransformationRequest
import com.clickpost.app.data.model.DeviceCapabilities
import com.clickpost.app.data.model.Resolution
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

sealed class ExportState {
    object Idle : ExportState()
    data class Processing(val progress: Float = 0f) : ExportState()
    data class Success(val outputPath: String) : ExportState()
    data class Error(val message: String) : ExportState()
}

@UnstableApi
@Singleton
class ExportEngine @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _state = MutableStateFlow<ExportState>(ExportState.Idle)
    val state: StateFlow<ExportState> = _state

    private var activeTransformer: Transformer? = null
    private var progressPollingJob: Job? = null

    fun export(
        composition: Composition,
        outputPath: String,
        resolution: Resolution,
        deviceCaps: DeviceCapabilities,
        scope: CoroutineScope
    ) {
        // Cancel any in-progress export
        cancel()

        _state.value = ExportState.Processing(0f)

        val mimeType = if (deviceCaps.supportsH265 && resolution.height >= 2160)
            MimeTypes.VIDEO_H265 else MimeTypes.VIDEO_H264

        val request = TransformationRequest.Builder()
            .setVideoMimeType(mimeType)
            .build()

        val listener = object : Transformer.Listener {
            override fun onCompleted(comp: Composition, result: ExportResult) {
                progressPollingJob?.cancel()
                _state.value = ExportState.Success(outputPath)
            }

            override fun onError(comp: Composition, result: ExportResult, exception: ExportException) {
                progressPollingJob?.cancel()
                _state.value = ExportState.Error(exception.localizedMessage ?: "Export failed")
            }
        }

        val transformer = Transformer.Builder(context)
            .setTransformationRequest(request)
            .addListener(listener)
            .build()

        activeTransformer = transformer
        transformer.start(composition, outputPath)

        // Poll progress every 250ms
        progressPollingJob = scope.launch(Dispatchers.Main) {
            val progressHolder = ProgressHolder()
            while (true) {
                delay(250)
                val currentState = _state.value
                if (currentState !is ExportState.Processing) break
                val progressState = transformer.getProgress(progressHolder)
                if (progressState == Transformer.PROGRESS_STATE_AVAILABLE) {
                    _state.value = ExportState.Processing(progressHolder.progress / 100f)
                }
            }
        }
    }

    fun cancel() {
        progressPollingJob?.cancel()
        progressPollingJob = null
        activeTransformer?.cancel()
        activeTransformer = null
        _state.value = ExportState.Idle
    }

    fun reset() {
        _state.value = ExportState.Idle
    }

    fun getVideoDurationUs(uri: android.net.Uri): Long {
        val retriever = android.media.MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, uri)
            val durationMs = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L
            durationMs * 1000L
        } catch (e: Exception) {
            0L
        } finally {
            retriever.release()
        }
    }
}
