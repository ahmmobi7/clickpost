package com.clickpost.app.engine

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

enum class ShareTarget { INSTAGRAM, WHATSAPP, GENERAL_DOWNLOAD }

@Singleton
class ShareEngine @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val authority = "${context.packageName}.fileprovider"

    fun buildShareIntent(filePath: String, target: ShareTarget): Intent {
        val file = File(filePath)
        val contentUri: Uri = FileProvider.getUriForFile(context, authority, file)

        return when (target) {
            ShareTarget.INSTAGRAM -> Intent(Intent.ACTION_SEND).apply {
                type = "video/*"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                setPackage("com.instagram.android")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            ShareTarget.WHATSAPP -> Intent(Intent.ACTION_SEND).apply {
                type = "video/*"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                setPackage("com.whatsapp")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            ShareTarget.GENERAL_DOWNLOAD -> Intent(Intent.ACTION_SEND).apply {
                type = "video/*"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
    }

    fun buildChooserIntent(filePath: String): Intent {
        val shareIntent = buildShareIntent(filePath, ShareTarget.GENERAL_DOWNLOAD)
        return Intent.createChooser(shareIntent, "Share Video").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    fun isAppInstalled(packageName: String): Boolean {
        return runCatching {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        }.getOrDefault(false)
    }

    val isInstagramInstalled: Boolean get() = isAppInstalled("com.instagram.android")
    val isWhatsAppInstalled: Boolean get() = isAppInstalled("com.whatsapp")
}
