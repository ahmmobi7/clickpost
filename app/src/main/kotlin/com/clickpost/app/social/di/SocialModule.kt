package com.clickpost.app.social.di

import android.content.Context
import androidx.room.Room
import com.clickpost.app.social.adapters.FacebookAdapter
import com.clickpost.app.social.adapters.InstagramAdapter
import com.clickpost.app.social.adapters.TikTokAdapter
import com.clickpost.app.social.adapters.YouTubeAdapter
import com.clickpost.app.social.data.Platform
import com.clickpost.app.social.db.PublishJobDao
import com.clickpost.app.social.db.SocialDatabase
import com.clickpost.app.social.engine.PlatformAdapter
import com.clickpost.app.social.engine.PublishEngine
import com.clickpost.app.social.repository.AccountGroupRepository
import com.clickpost.app.social.repository.PublishHistoryRepository
import com.clickpost.app.social.storage.CredentialVault
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@MapKey
annotation class PlatformKey(val value: Platform)

@Module
@InstallIn(SingletonComponent::class)
object SocialModule {

    @Provides
    @Singleton
    fun provideCredentialVault(@ApplicationContext context: Context): CredentialVault = 
        CredentialVault(context)

    @Provides
    @Singleton
    fun provideSocialDatabase(@ApplicationContext context: Context): SocialDatabase =
        Room.databaseBuilder(context, SocialDatabase::class.java, SocialDatabase.DB_NAME).build()

    @Provides
    @Singleton
    fun providePublishJobDao(db: SocialDatabase): PublishJobDao = db.publishJobDao()

    @Provides
    @Singleton
    fun provideAccountGroupRepository(
        @ApplicationContext context: Context,
        vault: CredentialVault
    ): AccountGroupRepository = AccountGroupRepository(context, vault)

    @Provides
    @Singleton
    fun providePublishHistoryRepository(dao: PublishJobDao): PublishHistoryRepository =
        PublishHistoryRepository(dao)

    @Provides
    @Singleton
    @IntoMap
    @PlatformKey(Platform.TIKTOK)
    fun provideTikTokAdapter(): PlatformAdapter = TikTokAdapter()

    @Provides
    @Singleton
    @IntoMap
    @PlatformKey(Platform.FACEBOOK)
    fun provideFacebookAdapter(): PlatformAdapter = FacebookAdapter()

    @Provides
    @Singleton
    @IntoMap
    @PlatformKey(Platform.INSTAGRAM)
    fun provideInstagramAdapter(): PlatformAdapter = InstagramAdapter()

    @Provides
    @Singleton
    @IntoMap
    @PlatformKey(Platform.YOUTUBE)
    fun provideYouTubeAdapter(): PlatformAdapter = YouTubeAdapter()

    @Provides
    @Singleton
    fun providePublishEngine(
        adapters: Map<Platform, @JvmSuppressWildcards PlatformAdapter>,
        vault: CredentialVault,
        groupRepo: AccountGroupRepository,
        historyRepo: PublishHistoryRepository
    ): PublishEngine = PublishEngine(adapters, vault, groupRepo, historyRepo)
}
