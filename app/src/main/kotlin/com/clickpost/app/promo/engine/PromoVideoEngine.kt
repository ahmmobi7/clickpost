package com.clickpost.app.promo.engine

import android.content.Context
import android.net.Uri
import androidx.media3.common.ClippingConfiguration
import androidx.media3.common.Effect
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.audio.AudioProcessor
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.VideoFrameProcessor
import androidx.media3.effect.Contrast
import androidx.media3.effect.DefaultVideoFrameProcessor
import androidx.media3.effect.GlEffect
import androidx.media3.effect.GlShaderProgram
import androidx.media3.effect.SingleFrameGlShaderProgram
import androidx.media3.transformer.Composition
import androidx.media3.transformer.EditedMediaItem
import androidx.media3.transformer.EditedMediaItemSequence
import androidx.media3.transformer.Transformer
import com.google.common.collect.ImmutableList
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PromoVideoEngine @Inject constructor(
    @ApplicationContext private val context: Context
) {
    @UnstableApi
    fun createTransformer(
        listener: Transformer.Listener,
        contrastValue: Float = 1.0f,
        sharpnessValue: Float = 0.0f
    ): Transformer {
        return Transformer.Builder(context)
            .setVideoMimeType(MimeTypes.VIDEO_H264)
            .addListener(listener)
            .build()
    }

    @UnstableApi
    fun buildEditedMediaItem(uri: Uri, durationMs: Long? = null, contrast: Float, sharpness: Float): EditedMediaItem {
        val mediaItemBuilder = MediaItem.Builder().setUri(uri)
        if (durationMs != null) {
            // For images
            mediaItemBuilder.setImageDurationMs(durationMs)
            // For videos: clip to the desired duration
            mediaItemBuilder.setClippingConfiguration(
                ClippingConfiguration.Builder()
                    .setEndPositionMs(durationMs)
                    .build()
            )
        }

        val effects = mutableListOf<Effect>()
        if (contrast != 1.0f) {
            effects.add(Contrast(contrast))
        }
        // Custom Sharpening GL Shader via SingleFrameGlShaderProgram
        if (sharpness > 0f) {
            effects.add(GlEffect { ctx, useHdr ->
                object : SingleFrameGlShaderProgram(useHdr) {
                    private val step = 0.001f

                    override fun drawFrame(inputTexId: Int, presentationTimeUs: Long) {
                        // Standard drawing logic is handled by SingleFrameGlShaderProgram's internal renderer
                        // using the vertex and fragment shaders provided.
                        super.drawFrame(inputTexId, presentationTimeUs)
                    }

                    override fun configure(width: Int, height: Int): VideoFrameProcessor.InputSize {
                        return VideoFrameProcessor.InputSize(width, height)
                    }

                    override fun getVertexCode(): String = """
                        attribute vec4 aFramePosition;
                        attribute vec4 aTexSamplingPosition;
                        varying vec2 vTexSamplingPosition;
                        void main() {
                          gl_Position = aFramePosition;
                          vTexSamplingPosition = aTexSamplingPosition.xy;
                        }
                    """.trimIndent()

                    override fun getFragmentCode(): String = """
                        precision mediump float;
                        varying vec2 vTexSamplingPosition;
                        uniform sampler2D uTexSampler;
                        void main() {
                          vec4 color = texture2D(uTexSampler, vTexSamplingPosition);
                          vec4 up = texture2D(uTexSampler, vTexSamplingPosition + vec2(0.0, $step));
                          vec4 down = texture2D(uTexSampler, vTexSamplingPosition - vec2(0.0, $step));
                          vec4 left = texture2D(uTexSampler, vTexSamplingPosition - vec2($step, 0.0));
                          vec4 right = texture2D(uTexSampler, vTexSamplingPosition + vec2($step, 0.0));
                          gl_FragColor = color + (color * 4.0 - up - down - left - right) * ${sharpness * 2.0};
                        }
                    """.trimIndent()
                }
            })
        }

        return EditedMediaItem.Builder(mediaItemBuilder.build())
            .setEffects(androidx.media3.transformer.Effects(emptyList(), effects))
            .build()
    }

    @UnstableApi
    fun buildComposition(
        hookVideoUri: Uri,
        blendedBitmapUris: List<Uri>,
        musicUri: Uri?,
        targetResolutionHeight: Int,
        slideDurationS: Int = 5,
        contrast: Float = 1.0f,
        sharpness: Float = 0.0f
    ): Composition {
        // Video sequences
        val editedHookItem = buildEditedMediaItem(hookVideoUri, null, contrast, sharpness)

        val blendedSequences = blendedBitmapUris.map { uri ->
            buildEditedMediaItem(uri, slideDurationS * 1000L, contrast, sharpness)
        }

        val videoList = mutableListOf(editedHookItem)
        videoList.addAll(blendedSequences)
        val videoSequence = EditedMediaItemSequence(ImmutableList.copyOf(videoList))

        // Audio sequence (Music)
        val sequences = mutableListOf<EditedMediaItemSequence>(videoSequence)
        if (musicUri != null) {
            val musicMediaItem = MediaItem.fromUri(musicUri)
            val editedMusicItem = EditedMediaItem.Builder(musicMediaItem).build()
            val musicSequence = EditedMediaItemSequence(ImmutableList.of(editedMusicItem))
            sequences.add(musicSequence)
        }

        return Composition.Builder(ImmutableList.copyOf(sequences))
            .build()
    }
}
