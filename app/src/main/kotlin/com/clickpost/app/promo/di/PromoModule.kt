package com.clickpost.app.promo.di

import android.content.Context
import androidx.room.Room
import com.clickpost.app.promo.data.PromoDao
import com.clickpost.app.promo.db.PromoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PromoModule {

    @Provides
    @Singleton
    fun providePromoDatabase(@ApplicationContext context: Context): PromoDatabase {
        return Room.databaseBuilder(
            context,
            PromoDatabase::class.java,
            PromoDatabase.DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providePromoDao(database: PromoDatabase): PromoDao {
        return database.promoDao()
    }
}
