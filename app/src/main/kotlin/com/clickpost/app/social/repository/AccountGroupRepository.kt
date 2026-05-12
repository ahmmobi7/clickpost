package com.clickpost.app.social.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.clickpost.app.social.data.AccountGroup
import com.clickpost.app.social.data.CredentialStatus
import com.clickpost.app.social.data.Platform
import com.clickpost.app.social.data.PlatformCredential
import com.clickpost.app.social.storage.CredentialVault
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "social_account_groups")

@Singleton
class AccountGroupRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val vault: CredentialVault
) {
    private val GROUPS_JSON = stringPreferencesKey("groups_json")
    private val gson = Gson()

    val groupsFlow: Flow<List<AccountGroup>> = context.dataStore.data.map { preferences ->
        val json = preferences[GROUPS_JSON] ?: "[]"
        val type = object : TypeToken<List<AccountGroup>>() {}.type
        gson.fromJson(json, type)
    }

    suspend fun getGroups(): List<AccountGroup> = groupsFlow.firstOrNull() ?: emptyList()

    suspend fun createGroup(name: String): AccountGroup {
        if (name.isBlank()) throw IllegalArgumentException("Group name cannot be blank")
        val groups = getGroups().toMutableList()
        if (groups.any { it.name.equals(name, ignoreCase = true) }) {
            throw IllegalArgumentException("Group with name '$name' already exists")
        }
        val newGroup = AccountGroup(name = name)
        groups.add(newGroup)
        saveGroups(groups)
        return newGroup
    }

    suspend fun updateGroupName(groupId: String, newName: String) {
        val groups = getGroups().toMutableList()
        val index = groups.indexOfFirst { it.id == groupId }
        if (index != -1) {
            groups[index] = groups[index].copy(name = newName)
            saveGroups(groups)
        }
    }

    suspend fun deleteGroup(groupId: String) {
        val groups = getGroups().toMutableList()
        val group = groups.find { it.id == groupId }
        group?.let {
            it.credentials.forEach { cred ->
                vault.delete(cred.keyAlias)
            }
            groups.remove(it)
            saveGroups(groups)
        }
    }

    suspend fun addCredential(
        groupId: String,
        platform: Platform,
        accountDisplayName: String,
        token: String,
        scopes: List<String>
    ): PlatformCredential {
        val groups = getGroups().toMutableList()
        val groupIndex = groups.indexOfFirst { it.id == groupId }
        if (groupIndex == -1) throw IllegalArgumentException("Group not found")
        val group = groups[groupIndex]

        if (group.credentials.size >= 4) throw IllegalStateException("Maximum 4 platforms per group")
        if (group.credentials.any { it.platform == platform }) {
            throw IllegalArgumentException("Platform $platform already exists in this group")
        }

        val alias = "cp_cred_${UUID.randomUUID()}"
        vault.store(alias, token)

        val newCredential = PlatformCredential(
            groupId = groupId,
            platform = platform,
            accountDisplayName = accountDisplayName,
            keyAlias = alias,
            scopes = scopes,
            status = CredentialStatus.VALID
        )

        val updatedCredentials = group.credentials + newCredential
        groups[groupIndex] = group.copy(credentials = updatedCredentials)
        saveGroups(groups)
        return newCredential
    }

    suspend fun updateCredential(credId: String, newToken: String, newDisplayName: String) {
        val groups = getGroups().toMutableList()
        var updated = false
        for (i in groups.indices) {
            val group = groups[i]
            val credIndex = group.credentials.indexOfFirst { it.id == credId }
            if (credIndex != -1) {
                val cred = group.credentials[credIndex]
                vault.store(cred.keyAlias, newToken)
                val updatedCred = cred.copy(
                    accountDisplayName = newDisplayName,
                    lastVerifiedAt = System.currentTimeMillis(),
                    status = CredentialStatus.VALID
                )
                val updatedCredentials = group.credentials.toMutableList()
                updatedCredentials[credIndex] = updatedCred
                groups[i] = group.copy(credentials = updatedCredentials)
                updated = true
                break
            }
        }
        if (updated) saveGroups(groups)
    }

    suspend fun deleteCredential(credId: String) {
        val groups = getGroups().toMutableList()
        var updated = false
        for (i in groups.indices) {
            val group = groups[i]
            val credIndex = group.credentials.indexOfFirst { it.id == credId }
            if (credIndex != -1) {
                val cred = group.credentials[credIndex]
                vault.delete(cred.keyAlias)
                val updatedCredentials = group.credentials.toMutableList()
                updatedCredentials.removeAt(credIndex)
                groups[i] = group.copy(credentials = updatedCredentials)
                updated = true
                break
            }
        }
        if (updated) saveGroups(groups)
    }

    suspend fun markCredentialStatus(credId: String, status: CredentialStatus) {
        val groups = getGroups().toMutableList()
        var updated = false
        for (i in groups.indices) {
            val group = groups[i]
            val credIndex = group.credentials.indexOfFirst { it.id == credId }
            if (credIndex != -1) {
                val cred = group.credentials[credIndex]
                val updatedCred = cred.copy(status = status)
                val updatedCredentials = group.credentials.toMutableList()
                updatedCredentials[credIndex] = updatedCred
                groups[i] = group.copy(credentials = updatedCredentials)
                updated = true
                break
            }
        }
        if (updated) saveGroups(groups)
    }

    private suspend fun saveGroups(groups: List<AccountGroup>) {
        context.dataStore.edit { preferences ->
            preferences[GROUPS_JSON] = gson.toJson(groups)
        }
    }
}
