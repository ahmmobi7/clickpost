package com.clickpost.app.promo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.clickpost.app.promo.data.*

@Database(
    entities = [
        HookVideo::class,
        ProductAsset::class,
        ModelImage::class,
        GeneratedPromo::class,
        PromoMusic::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(PromoConverters::class)
abstract class PromoDatabase : RoomDatabase() {
    abstract fun promoDao(): PromoDao

    companion object {
        const val DB_NAME = "clickpost_promo.db"
    }
}
