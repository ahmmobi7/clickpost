package com.clickpost.app.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.clickpost.app.data.model.BrandProfile
import com.clickpost.app.data.model.BrandingPreferences
import com.clickpost.app.data.model.LogoPosition
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "clickpost_prefs")

@Singleton
class ProfileRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val gson = Gson()

    private object Keys {
        val PROFILE_JSON = stringPreferencesKey("brand_profile")
        val PREFS_JSON = stringPreferencesKey("branding_prefs")
        val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
    }

    val profileFlow: Flow<BrandProfile?> = context.dataStore.data.map { prefs ->
        prefs[Keys.PROFILE_JSON]?.let {
            runCatching { gson.fromJson(it, BrandProfile::class.java) }.getOrNull()
        }
    }

    val brandingPrefsFlow: Flow<BrandingPreferences> = context.dataStore.data.map { prefs ->
        prefs[Keys.PREFS_JSON]?.let {
            runCatching { gson.fromJson(it, BrandingPreferences::class.java) }.getOrNull()
        } ?: BrandingPreferences()
    }

    val isFirstLaunchFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.FIRST_LAUNCH] ?: true
    }

    suspend fun saveProfile(profile: BrandProfile) {
        context.dataStore.edit { prefs ->
            prefs[Keys.PROFILE_JSON] = gson.toJson(profile.copy(updatedAt = System.currentTimeMillis()))
            prefs[Keys.FIRST_LAUNCH] = false
        }
    }

    suspend fun saveBrandingPrefs(prefs: BrandingPreferences) {
        context.dataStore.edit { store ->
            store[Keys.PREFS_JSON] = gson.toJson(prefs)
        }
    }

    suspend fun getProfile(): BrandProfile? = profileFlow.firstOrNull()

    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}
