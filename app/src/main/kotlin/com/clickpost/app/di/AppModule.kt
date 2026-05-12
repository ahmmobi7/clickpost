package com.clickpost.app.di

import android.content.Context
import androidx.media3.common.util.UnstableApi
import com.clickpost.app.engine.BrandingEngine
import com.clickpost.app.engine.DeviceCapabilityChecker
import com.clickpost.app.engine.ExportEngine
import com.clickpost.app.engine.ShareEngine
import com.clickpost.app.storage.StorageManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@UnstableApi
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStorageManager(@ApplicationContext context: Context): StorageManager =
        StorageManager(context)

    @Provides
    @Singleton
    fun provideBrandingEngine(@ApplicationContext context: Context): BrandingEngine =
        BrandingEngine(context)

    @Provides
    @Singleton
    fun provideExportEngine(@ApplicationContext context: Context): ExportEngine =
        ExportEngine(context)

    @Provides
    @Singleton
    fun provideShareEngine(@ApplicationContext context: Context): ShareEngine =
        ShareEngine(context)

    @Provides
    @Singleton
    fun provideDeviceCapabilityChecker(@ApplicationContext context: Context): DeviceCapabilityChecker =
        DeviceCapabilityChecker(context)
}
