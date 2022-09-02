package com.example.nickelfoxassignment.newsapp.database

import androidx.room.TypeConverter
import com.example.nickelfoxassignment.newsapp.retrofit.response.Source
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SourceTypeConvertor {
    private val gson = Gson()

    @TypeConverter
    fun imageToString(source: Source): String {
        return gson.toJson(source)
    }

    @TypeConverter
    fun stringToImage(source: String): Source {
        val objectType = object : TypeToken<Source>() {}.type
        return gson.fromJson(source, objectType)
    }
}