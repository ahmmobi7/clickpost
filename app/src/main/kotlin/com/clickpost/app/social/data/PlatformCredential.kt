package com.clickpost.app.social.data

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class PlatformCredential(
    val id: String = UUID.randomUUID().toString(),
    val groupId: String,
    val platform: Platform,
    val accountDisplayName: String,   // e.g. "@myshop" — shown in UI only
    val keyAlias: String,             // pointer into CredentialVault, NOT the token
    val scopes: List<String> = emptyList(),
    val status: CredentialStatus = CredentialStatus.VALID,
    val lastVerifiedAt: Long = System.currentTimeMillis()
)

enum class CredentialStatus { VALID, EXPIRED, ERROR, UNVERIFIED }
