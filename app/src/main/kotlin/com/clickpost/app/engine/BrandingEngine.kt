package com.clickpost.app.engine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import androidx.media3.common.util.Size
import androidx.media3.common.Effect
import androidx.media3.common.MediaItem
import androidx.media3.common.audio.AudioProcessor
import androidx.media3.common.util.UnstableApi
import androidx.media3.effect.BitmapOverlay
import androidx.media3.effect.OverlayEffect
import androidx.media3.effect.OverlaySettings
import androidx.media3.effect.Presentation
import androidx.media3.transformer.Composition
import androidx.media3.transformer.EditedMediaItem
import androidx.media3.transformer.EditedMediaItemSequence
import androidx.media3.transformer.Effects
import com.clickpost.app.data.model.BrandProfile
import com.clickpost.app.data.model.BrandingPreferences
import com.clickpost.app.data.model.LogoPosition
import com.google.common.collect.ImmutableList
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@UnstableApi
@Singleton
class BrandingEngine @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * Builds an immutable Media3 Composition (the NLE "recipe").
     * The original video is never modified — this is a descriptor only.
     */
    fun buildComposition(
        videoUri: android.net.Uri,
        profile: BrandProfile,
        prefs: BrandingPreferences,
        targetHeight: Int = 1080,
        videoDurationUs: Long = 0L
    ): Composition {
        // ── Load logo bitmap ───────────────────────────────────────────────────
        val logoBitmap: Bitmap? = if (prefs.showLogo && profile.logoPath.isNotBlank()) {
            runCatching { BitmapFactory.decodeFile(profile.logoPath) }.getOrNull()
        } else null

        // ── Build overlay list ─────────────────────────────────────────────────
        val overlays = mutableListOf<BitmapOverlay>()

        if (logoBitmap != null) {
            overlays.add(LogoOverlay(logoBitmap, prefs.logoPosition, prefs.overlayPaddingRatio, videoDurationUs))
        }

        val textLines = buildList {
            if (profile.companyName.isNotBlank()) add(profile.companyName)
            if (prefs.showContactInfo && profile.contactInfo.isNotBlank()) add(profile.contactInfo)
        }
        if (textLines.isNotEmpty()) {
            overlays.add(TextBitmapOverlay(textLines, prefs.overlayPaddingRatio, videoDurationUs))
        }

        // ── Video-specific text overlay (last 15s) ──────────────────────────
        if (prefs.videoOverlayText.isNotBlank()) {
            overlays.add(
                VideoSpecificTextOverlay(
                    text = prefs.videoOverlayText,
                    fontName = prefs.videoOverlayFont,
                    colorHex = prefs.videoOverlayColor,
                    videoDurationUs = videoDurationUs
                )
            )
        }

        // ── Main clip with branding effects ───────────────────────────────────
        val overlayEffect = OverlayEffect(ImmutableList.copyOf(overlays))
        val presentation = Presentation.createForHeight(targetHeight)

        val brandedMain = EditedMediaItem.Builder(MediaItem.fromUri(videoUri))
            .setEffects(
                Effects(
                    ImmutableList.of<AudioProcessor>(), // no audio processing
                    ImmutableList.of<Effect>(presentation, overlayEffect) // video effects
                )
            )
            .build()

        // ── End clip (outro — no branding applied) ─────────────────────────────
        val sequence: EditedMediaItemSequence = if (
            profile.endClipPath.isNotBlank() && File(profile.endClipPath).exists()
        ) {
            val outro = EditedMediaItem.Builder(
                MediaItem.fromUri(android.net.Uri.fromFile(File(profile.endClipPath)))
            ).build()
            EditedMediaItemSequence(ImmutableList.of(brandedMain, outro))
        } else {
            EditedMediaItemSequence(ImmutableList.of(brandedMain))
        }

        return Composition.Builder(ImmutableList.of(sequence)).build()
    }

    // ── Private overlay implementations ───────────────────────────────────────

    /**
     * Logo overlay — uses configure() to scale the bitmap relative to video size.
     * Positioning: 2cm margin from top and side (approx for 9:16).
     */
    private class LogoOverlay(
        private val sourceBitmap: Bitmap,
        private val position: LogoPosition,
        private val paddingRatio: Float,
        private val videoDurationUs: Long
    ) : BitmapOverlay() {

        private var scaledBitmap: Bitmap = sourceBitmap
        private var settings: OverlaySettings = OverlaySettings.Builder().setAlphaScale(0f).build()

        override fun configure(videoSize: Size) {
            // Scale logo to ~15% of video width, maintaining aspect ratio
            val targetWidth = (videoSize.width * 0.15f).toInt().coerceAtLeast(64)
            val aspectRatio = sourceBitmap.width.toFloat() / sourceBitmap.height.toFloat()
            val targetHeight = (targetWidth / aspectRatio).toInt().coerceAtLeast(32)
            scaledBitmap = Bitmap.createScaledBitmap(
                sourceBitmap, targetWidth, targetHeight, true
            )

            // 2cm top margin, 4cm side margin
            // Assuming 15cm height, 8.4cm width baseline
            // GL Coordinates: range from -1 to 1 (total span of 2)
            val verticalMargin = (2f / 15f) * 2f
            val horizontalMargin = (4f / 8.4f) * 2f
            
            val isLeft = position == LogoPosition.TOP_LEFT
            settings = OverlaySettings.Builder()
                .setOverlayFrameAnchor(if (isLeft) -1f else 1f, 1f)
                .setBackgroundFrameAnchor(
                    if (isLeft) -1f + horizontalMargin else 1f - horizontalMargin,
                    1f - verticalMargin
                )
                .build()
        }

        override fun getBitmap(presentationTimeUs: Long): Bitmap = scaledBitmap

        override fun getOverlaySettings(presentationTimeUs: Long): OverlaySettings {
            // Only show for the last 15 seconds
            // If duration is unknown (0), hide it as we can't determine the "last 15 seconds"
            if (videoDurationUs <= 0L) return OverlaySettings.Builder().setAlphaScale(0f).build()

            val startTimeUs = (videoDurationUs - 15_000_000L).coerceAtLeast(0L)
            return if (presentationTimeUs >= startTimeUs) {
                settings
            } else {
                OverlaySettings.Builder().setAlphaScale(0f).build()
            }
        }
    }

    /**
     * Text overlay — renders contact info to a bitmap using Canvas.
     * Positioning: 2cm from left margin, middle vertical. Arial Bold, 16px, neon green shadow.
     */
    private class TextBitmapOverlay(
        private val lines: List<String>,
        private val paddingRatio: Float,
        private val videoDurationUs: Long
    ) : BitmapOverlay() {

        private var cachedBitmap: Bitmap? = null
        private var settings: OverlaySettings = OverlaySettings.Builder().build()

        override fun configure(videoSize: Size) {
            // Restore original fixed size (72px) as requested
            val textSizePx = 72f
            cachedBitmap = renderTextBitmap(lines, textSizePx)
            
            // Position contact info: 2cm from left, 5cm from bottom
            // Baseline 15cm height, 8.4cm width
            // GL Coordinates range from -1 to 1 (total span of 2)
            val horizontalMargin = (2.5f / 8.4f) * 2f // Increase to 2.5cm to ensure visibility
            val verticalMarginFromBottom = (5.5f / 15f) * 2f // Increase to 5.5cm to ensure visibility
            settings = OverlaySettings.Builder()
                .setOverlayFrameAnchor(-1f, -1f) // Bottom-left of overlay
                .setBackgroundFrameAnchor(-1f + horizontalMargin, -1f + verticalMarginFromBottom)
                .build()
        }

        override fun getBitmap(presentationTimeUs: Long): Bitmap {
            return cachedBitmap ?: renderTextBitmap(lines, 72f)
        }

        override fun getOverlaySettings(presentationTimeUs: Long): OverlaySettings {
            // Only show for the last 15 seconds
            // If duration is unknown (0), hide it as we can't determine the "last 15 seconds"
            if (videoDurationUs <= 0L) return OverlaySettings.Builder().setAlphaScale(0f).build()

            val startTimeUs = (videoDurationUs - 15_000_000L).coerceAtLeast(0L)
            return if (presentationTimeUs >= startTimeUs) {
                settings
            } else {
                OverlaySettings.Builder().setAlphaScale(0f).build()
            }
        }

        private fun renderTextBitmap(lines: List<String>, textSizePx: Float): Bitmap {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.parseColor("#39FF14") // Neon green font
                textSize = textSizePx
                typeface = Typeface.create("Arial", Typeface.BOLD)
                // Black shadow
                setShadowLayer(textSizePx * 0.2f, 4f, 4f, Color.BLACK)
                strokeWidth = 3f // Thicker font
                style = Paint.Style.FILL_AND_STROKE
            }

            val lineHeight = (paint.descent() - paint.ascent() + 10f).toInt()
            val maxWidth = lines.maxOf { paint.measureText(it) }.toInt() + 60
            val totalHeight = lineHeight * lines.size + 40

            val bitmap = Bitmap.createBitmap(maxWidth, totalHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            var y = -paint.ascent() + 20f
            lines.forEach { line ->
                // Left aligned within the bitmap, as the block itself is positioned
                canvas.drawText(line, 20f, y, paint)
                y += lineHeight
            }
            return bitmap
        }
    }

    /**
     * Video-specific text overlay — shown only for the first 15 seconds.
     * Positioned 4cm from top, center.
     */
    private class VideoSpecificTextOverlay(
        private val text: String,
        private val fontName: String,
        private val colorHex: String,
        private val videoDurationUs: Long
    ) : BitmapOverlay() {

        private var cachedBitmap: Bitmap? = null
        private var settings: OverlaySettings = OverlaySettings.Builder().build()

        override fun configure(videoSize: Size) {
            val textSizePx = 100f // Larger text for better visibility
            cachedBitmap = renderTextBitmap(text, textSizePx)

            // Position video text 2cm from top
            val verticalMarginFromTop = (2f / 15f) * 2f
            settings = OverlaySettings.Builder()
                .setOverlayFrameAnchor(0f, 1f) // Center top of overlay
                .setBackgroundFrameAnchor(0f, 1f - verticalMarginFromTop) // Center top, 2cm margin
                .setAlphaScale(1.0f) // Ensure visibility
                .build()
        }

        override fun getBitmap(presentationTimeUs: Long): Bitmap {
            return cachedBitmap ?: renderTextBitmap(text, 84f)
        }

        override fun getOverlaySettings(presentationTimeUs: Long): OverlaySettings {
            // Only show for the FIRST 15 seconds
            val endTimeUs = 15_000_000L
            return if (presentationTimeUs <= endTimeUs) {
                settings
            } else {
                OverlaySettings.Builder().setAlphaScale(0f).build()
            }
        }

        private fun renderTextBitmap(text: String, textSizePx: Float): Bitmap {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = try { Color.parseColor(colorHex) } catch (e: Exception) { Color.WHITE }
                textSize = textSizePx
                typeface = when (fontName.lowercase()) {
                    "impact" -> Typeface.create("sans-serif-condensed", Typeface.BOLD)
                    "bebas" -> Typeface.create("serif", Typeface.BOLD)
                    "modern" -> Typeface.create("monospace", Typeface.BOLD)
                    else -> Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                }
                setShadowLayer(8f, 4f, 4f, Color.BLACK)
                strokeWidth = 2f
                style = Paint.Style.FILL_AND_STROKE
            }

            val width = paint.measureText(text).toInt() + 100
            val height = (paint.descent() - paint.ascent()).toInt() + 50

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawText(text, 50f, -paint.ascent() + 25f, paint)
            return bitmap
        }
    }
}
