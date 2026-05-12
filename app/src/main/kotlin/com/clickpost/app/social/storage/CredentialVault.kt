package com.clickpost.app.social.storage

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CredentialVault @Inject constructor(
    @ApplicationContext context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .setUserAuthenticationRequired(false)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "clickpost_credential_vault",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    suspend fun store(alias: String, token: String) = withContext(Dispatchers.IO) {
        prefs.edit().putString(alias, token).apply()
    }

    suspend fun retrieve(alias: String): String? = withContext(Dispatchers.IO) {
        try {
            prefs.getString(alias, null)
        } catch (e: Exception) {
            throw VaultException("Failed to retrieve credential for alias: $alias", e)
        }
    }

    suspend fun delete(alias: String) = withContext(Dispatchers.IO) {
        prefs.edit().remove(alias).apply()
    }

    suspend fun exists(alias: String): Boolean = withContext(Dispatchers.IO) {
        prefs.contains(alias)
    }
}

class VaultException(message: String, cause: Throwable) : Exception(message, cause)
