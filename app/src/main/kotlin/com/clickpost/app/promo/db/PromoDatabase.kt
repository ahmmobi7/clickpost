package com.clickpost.app.promo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.clickpost.app.promo.data.*

@Database(
    entities = [
        HookVideo::class,
        ProductAsset::class,
        ModelImage::class,
        GeneratedPromo::class,
        PromoMusic::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PromoDatabase : RoomDatabase() {
    abstract fun promoDao(): PromoDao

    companion object {
        const val DB_NAME = "clickpost_promo.db"
    }
}
