package com.clickpost.app.promo.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PromoConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromLongList(value: List<Long>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toLongList(value: String?): List<Long>? {
        val listType = object : TypeToken<List<Long>>() {}.type
        return gson.fromJson(value, listType)
    }
}
