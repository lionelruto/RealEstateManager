package com.openclassrooms.realestatemanager.Models

import android.icu.text.DisplayContext
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {


        @TypeConverter
        fun listToJson(value: List<ListPhotos>?) = Gson().toJson(value)

        @TypeConverter
        fun jsonToList(value: String) = Gson().fromJson(value, Array<ListPhotos>::class.java).toList()

        @TypeConverter
        fun fromList(value : List<String>) = Json.encodeToString(value)

        @TypeConverter
        fun toList(value: String) = Json.decodeFromString<List<String>>(value)

}