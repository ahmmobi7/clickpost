package com.clickpost.app.promo.engine

import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions
import com.google.mlkit.vision.segmentation.Segmentation
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackgroundRemover @Inject constructor() {
    private val options = SelfieSegmenterOptions.Builder()
        .setDetectorMode(SelfieSegmenterOptions.SINGLE_IMAGE_MODE)
        .build()
    private val segmenter = Segmentation.getClient(options)

    suspend fun removeBackground(bitmap: Bitmap): Bitmap {
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        val mask = segmenter.process(inputImage).await()
        val maskBuffer = mask.buffer
        val maskWidth = mask.width
        val maskHeight = mask.height

        val resultBitmap = createBitmap(maskWidth, maskHeight, Bitmap.Config.ARGB_8888)
        val pixels = IntArray(maskWidth * maskHeight)
        
        // Ensure the source bitmap matches mask dimensions
        val sourceBitmap = if (bitmap.width != maskWidth || bitmap.height != maskHeight) {
            bitmap.scale(maskWidth, maskHeight, true)
        } else {
            bitmap
        }

        sourceBitmap.getPixels(pixels, 0, maskWidth, 0, 0, maskWidth, maskHeight)
        maskBuffer.rewind()

        for (i in 0 until maskWidth * maskHeight) {
            val confidence = maskBuffer.getFloat()
            if (confidence <= 0.5f) {
                pixels[i] = Color.TRANSPARENT
            }
        }

        resultBitmap.setPixels(pixels, 0, maskWidth, 0, 0, maskWidth, maskHeight)
        maskBuffer.rewind()
        return resultBitmap
    }
}
