package com.clickpost.app.promo.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PromoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHookVideo(hookVideo: HookVideo)

    @Query("SELECT * FROM hook_videos")
    fun getAllHookVideos(): Flow<List<HookVideo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductAsset(productAsset: ProductAsset)

    @Query("SELECT * FROM product_assets")
    fun getAllProductAssets(): Flow<List<ProductAsset>>

    @Query("SELECT * FROM product_assets WHERE id IN (:ids)")
    suspend fun getProductAssetsByIds(ids: List<Long>): List<ProductAsset>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModelImage(modelImage: ModelImage)

    @Query("SELECT * FROM model_images")
    fun getAllModelImages(): Flow<List<ModelImage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeneratedPromo(generatedPromo: GeneratedPromo)

    @Query("SELECT * FROM generated_promos ORDER BY timestamp DESC")
    fun getAllGeneratedPromos(): Flow<List<GeneratedPromo>>

    @Delete
    suspend fun deleteGeneratedPromo(generatedPromo: GeneratedPromo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMusic(music: PromoMusic)

    @Query("SELECT * FROM promo_music")
    fun getAllMusic(): Flow<List<PromoMusic>>
}
