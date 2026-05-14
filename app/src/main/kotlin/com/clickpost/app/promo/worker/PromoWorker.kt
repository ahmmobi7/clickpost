package com.clickpost.app.promo.worker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.media3.transformer.Transformer
import androidx.media3.transformer.ExportResult
import androidx.media3.transformer.Composition
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.clickpost.app.promo.data.*
import com.clickpost.app.promo.engine.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.CountDownLatch
import androidx.hilt.work.HiltWorker

@HiltWorker
class PromoWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val backgroundRemover: BackgroundRemover,
    private val imageBlender: ImageBlender,
    private val videoEngine: PromoVideoEngine,
    private val repository: PromoRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.Default) {
        val hookUri = inputData.getString("hookUri") ?: return@withContext Result.failure()
        val productUris = inputData.getStringArray("productUris") ?: return@withContext Result.failure()
        val modelUri = inputData.getString("modelUri") ?: return@withContext Result.failure()
        val musicUri = inputData.getString("musicUri")
        val description = inputData.getString("description") ?: ""
        val resolutionHeight = inputData.getInt("resolutionHeight", 1080)
        val targetWidth = (resolutionHeight * 9 / 16)

        try {
            val modelBitmap = loadScaledBitmap(modelUri, targetWidth, resolutionHeight)
            val blendedUris = mutableListOf<Uri>()

            for ((index, pUri) in productUris.withIndex()) {
                val productBitmap = loadScaledBitmap(pUri, targetWidth / 2, resolutionHeight / 2)
                val processedProduct = backgroundRemover.removeBackground(productBitmap)

                val blendedBitmap = imageBlender.blend(
                    modelBitmap = modelBitmap,
                    productBitmap = processedProduct,
                    targetWidth = targetWidth,
                    targetHeight = resolutionHeight,
                    description = "$description ($index)",
                    logoBitmap = null,
                    contactInfo = "Contact us: @clickpost"
                )

                val blendedFile = File(applicationContext.cacheDir, "blended_${index}_${System.currentTimeMillis()}.png")
                FileOutputStream(blendedFile).use {
                    blendedBitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                }
                blendedUris.add(Uri.fromFile(blendedFile))
            }

            val outputFile = File(applicationContext.filesDir, "promo_${System.currentTimeMillis()}.mp4")
            val latch = CountDownLatch(1)
            var exportError: Throwable? = null

            val transformerListener = object : Transformer.Listener {
                override fun onCompleted(composition: Composition, exportResult: ExportResult) {
                    latch.countDown()
                }

                override fun onError(composition: Composition, exportResult: ExportResult, exception: androidx.media3.transformer.ExportException) {
                    exportError = exception
                    latch.countDown()
                }
            }

            val transformer = videoEngine.createTransformer(transformerListener)
            val composition = videoEngine.buildComposition(
                hookVideoUri = Uri.parse(hookUri),
                blendedBitmapUris = blendedUris,
                musicUri = musicUri?.let { Uri.parse(it) },
                targetResolutionHeight = resolutionHeight
            )

            withContext(Dispatchers.Main) {
                transformer.start(composition, outputFile.absolutePath)
            }

            latch.await()

            if (exportError != null) {
                return@withContext Result.failure(workDataOf("error" to exportError?.message))
            }

            repository.insertGeneratedPromo(GeneratedPromo(filePath = outputFile.absolutePath))

            // Scan file for gallery visibility
            MediaScannerConnection.scanFile(applicationContext, arrayOf(outputFile.absolutePath), null, null)

            Result.success()
        } catch (e: Exception) {
            Result.failure(workDataOf("error" to e.message))
        }
    }

    private fun loadScaledBitmap(uriString: String, reqWidth: Int, reqHeight: Int): Bitmap {
        val uri = Uri.parse(uriString)
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        applicationContext.contentResolver.openInputStream(uri).use {
            BitmapFactory.decodeStream(it, null, options)
        }

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false

        return applicationContext.contentResolver.openInputStream(uri).use {
            BitmapFactory.decodeStream(it, null, options)
        } ?: throw Exception("Failed to load bitmap")
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.outHeight to options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}
