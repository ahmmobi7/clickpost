package com.clickpost.app.social.data

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class AccountGroup(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val credentials: List<PlatformCredential> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) {
    val platformCount: Int get() = credentials.size
    val isValid: Boolean get() = credentials.isNotEmpty()
}
