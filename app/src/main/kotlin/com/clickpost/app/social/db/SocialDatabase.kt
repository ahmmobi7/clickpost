package com.clickpost.app.social.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PublishJobEntity::class], version = 1, exportSchema = false)
abstract class SocialDatabase : RoomDatabase() {
    abstract fun publishJobDao(): PublishJobDao

    companion object {
        const val DB_NAME = "clickpost_social.db"
    }
}
