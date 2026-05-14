package com.clickpost.app.promo.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PromoRepository @Inject constructor(
    private val promoDao: PromoDao
) {
    fun getAllHookVideos(): Flow<List<HookVideo>> = promoDao.getAllHookVideos()
    suspend fun insertHookVideo(hookVideo: HookVideo) = promoDao.insertHookVideo(hookVideo)

    fun getAllProductAssets(): Flow<List<ProductAsset>> = promoDao.getAllProductAssets()
    suspend fun insertProductAsset(productAsset: ProductAsset) = promoDao.insertProductAsset(productAsset)

    fun getAllModelImages(): Flow<List<ModelImage>> = promoDao.getAllModelImages()
    suspend fun insertModelImage(modelImage: ModelImage) = promoDao.insertModelImage(modelImage)

    fun getAllGeneratedPromos(): Flow<List<GeneratedPromo>> = promoDao.getAllGeneratedPromos()
    suspend fun insertGeneratedPromo(generatedPromo: GeneratedPromo) = promoDao.insertGeneratedPromo(generatedPromo)
    suspend fun deleteGeneratedPromo(generatedPromo: GeneratedPromo) = promoDao.deleteGeneratedPromo(generatedPromo)

    fun getAllMusic(): Flow<List<PromoMusic>> = promoDao.getAllMusic()
    suspend fun insertMusic(music: PromoMusic) = promoDao.insertMusic(music)
}
