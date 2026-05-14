package com.clickpost.app.promo.engine

import android.graphics.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageBlender @Inject constructor() {

    fun blend(
        modelBitmap: Bitmap,
        productBitmap: Bitmap,
        targetWidth: Int,
        targetHeight: Int,
        description: String,
        logoBitmap: Bitmap?,
        contactInfo: String?
    ): Bitmap {
        val result = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)

        val modelScaled = scaleCenterCrop(modelBitmap, targetWidth, targetHeight)
        canvas.drawBitmap(modelScaled, 0f, 0f, null)

        val productWidth = (targetWidth * 0.25f).toInt()
        val aspectRatio = productBitmap.height.toFloat() / productBitmap.width
        val productHeight = (productWidth * aspectRatio).toInt()
        val productScaled = Bitmap.createScaledBitmap(productBitmap, productWidth, productHeight, true)

        val productX = targetWidth - productWidth - (targetWidth * 0.05f)
        val productY = (targetHeight - productHeight) / 2f
        canvas.drawBitmap(productScaled, productX, productY.toFloat(), null)

        val paint = Paint().apply {
            color = Color.WHITE
            textSize = targetHeight * 0.03f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            setShadowLayer(5f, 0f, 0f, Color.BLACK)
        }
        canvas.drawText(description, targetWidth / 2f, productY + productHeight + 40f, paint)

        logoBitmap?.let {
            val logoWidth = (targetWidth * 0.15f).toInt()
            val logoAspectRatio = it.height.toFloat() / it.width
            val logoHeight = (logoWidth * logoAspectRatio).toInt()
            val logoScaled = Bitmap.createScaledBitmap(it, logoWidth, logoHeight, true)
            canvas.drawBitmap(logoScaled, targetWidth * 0.05f, targetHeight * 0.05f, null)
        }

        contactInfo?.let {
            val contactPaint = Paint().apply {
                color = Color.WHITE
                textSize = targetHeight * 0.025f
                isAntiAlias = true
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText(it, targetWidth / 2f, targetHeight * 0.75f, contactPaint)
        }

        return result
    }

    private fun scaleCenterCrop(source: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val sourceWidth = source.width
        val sourceHeight = source.height
        val xScale = newWidth.toFloat() / sourceWidth
        val yScale = newHeight.toFloat() / sourceHeight
        val scale = Math.max(xScale, yScale)
        val scaledWidth = scale * sourceWidth
        val scaledHeight = scale * sourceHeight
        val left = (newWidth - scaledWidth) / 2
        val top = (newHeight - scaledHeight) / 2
        val targetRect = RectF(left, top, left + scaledWidth, top + scaledHeight)
        val dest = Bitmap.createBitmap(newWidth, newHeight, source.config ?: Bitmap.Config.ARGB_8888)
        val canvas = Canvas(dest)
        canvas.drawBitmap(source, null, targetRect, null)
        return dest
    }
}
