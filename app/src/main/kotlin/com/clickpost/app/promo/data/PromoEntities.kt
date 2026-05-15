package com.clickpost.app.promo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hook_videos")
data class HookVideo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uri: String,
    val metadata: String? = null
)

@Entity(tableName = "product_assets")
data class ProductAsset(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uri: String,
    val type: String, // "Image" or "Video"
    val description: String
)

@Entity(tableName = "model_images")
data class ModelImage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uri: String,
    val metadata: String? = null
)

@Entity(tableName = "generated_promos")
data class GeneratedPromo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val filePath: String,
    val timestamp: Long = System.currentTimeMillis(),
    val metadata: String? = null,
    val productAssetIds: List<Long> = emptyList()
)

@Entity(tableName = "promo_music")
data class PromoMusic(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uri: String,
    val name: String
)
