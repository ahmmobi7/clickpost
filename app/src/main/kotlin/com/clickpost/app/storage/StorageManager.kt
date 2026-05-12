package com.clickpost.app.storage

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.clickpost.app.data.model.BrandedVideo
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // ── Root directories ──────────────────────────────────────────────────────

    val brandingDir: File = File(context.filesDir, "branding").also { it.mkdirs() }
    val cacheDir: File = File(context.cacheDir, "temp_videos").also { it.mkdirs() }
    val exportsDir: File = File(
        context.getExternalFilesDir(Environment.DIRECTORY_MOVIES),
        "ClickPost"
    ).also { it.mkdirs() }

    // ── Fixed asset paths ─────────────────────────────────────────────────────

    val logoFile: File get() = File(brandingDir, "logo.png")
    val endClipFile: File get() = File(brandingDir, "outro.mp4")

    // ── Path helpers ──────────────────────────────────────────────────────────

    fun newExportFile(resolutionLabel: String): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        return File(exportsDir, "ClickPost_${timestamp}_${resolutionLabel}.mp4")
    }

    fun newCacheFile(name: String): File = File(cacheDir, "$name.mp4")

    // ── Retrieval ─────────────────────────────────────────────────────────────

    fun getBrandedVideos(): List<BrandedVideo> {
        return exportsDir.listFiles { file -> file.isFile && file.extension == "mp4" }
            ?.map { file ->
                BrandedVideo(
                    path = file.absolutePath,
                    fileName = file.name,
                    sizeBytes = file.length(),
                    createdAt = file.lastModified()
                )
            }?.sortedByDescending { it.createdAt } ?: emptyList()
    }

    fun deleteVideo(path: String): Boolean {
        return File(path).delete()
    }

    // ── Copy from Uri (for user-selected logo/clip) ───────────────────────────

    fun copyUriToFile(uri: Uri, destination: File): Boolean {
        return runCatching {
            val inputStream: InputStream = context.contentResolver.openInputStream(uri)
                ?: return false
            inputStream.use { input ->
                destination.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            true
        }.getOrDefault(false)
    }

    // ── Cleanup ───────────────────────────────────────────────────────────────

    fun clearCache(): Long {
        var freed = 0L
        cacheDir.listFiles()?.forEach { file ->
            freed += file.length()
            file.delete()
        }
        return freed
    }

    fun clearOldExports(keepCount: Int = 10) {
        val files = exportsDir.listFiles()?.sortedByDescending { it.lastModified() } ?: return
        files.drop(keepCount).forEach { it.delete() }
    }
}
