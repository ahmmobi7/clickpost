package com.clickpost.app.promo.engine

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
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
    fun createTransformer(listener: Transformer.Listener): Transformer {
        return Transformer.Builder(context)
            .setVideoMimeType(MimeTypes.VIDEO_H264)
            .addListener(listener)
            .build()
    }

    fun buildComposition(
        hookVideoUri: Uri,
        blendedBitmapUris: List<Uri>,
        musicUri: Uri?,
        targetResolutionHeight: Int
    ): Composition {
        // Video sequences
        val hookMediaItem = MediaItem.fromUri(hookVideoUri)
        val editedHookItem = EditedMediaItem.Builder(hookMediaItem).build()

        val blendedSequences = blendedBitmapUris.map { uri ->
            EditedMediaItem.Builder(
                MediaItem.Builder()
                    .setUri(uri)
                    .setImageDurationMs(3000)
                    .build()
            ).build()
        }

        val videoList = mutableListOf(editedHookItem)
        videoList.addAll(blendedSequences)
        val videoSequence = EditedMediaItemSequence(ImmutableList.copyOf(videoList))

        // Audio sequence (Music)
        val sequences = mutableListOf(videoSequence)
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
