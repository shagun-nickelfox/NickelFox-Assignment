package com.example.nickelfoxassignment

import androidx.lifecycle.MutableLiveData
import com.example.nickelfoxassignment.imageuploadapp.model.Data

object Constants {
    const val ONE = "1"
    const val TWO = "2"
    const val THREE = "3"
    const val FOUR = "4"
    const val FIVE = "5"
    const val SIX = "6"
    const val SEVEN = "7"
    const val EIGHT = "8"
    const val NINE = "9"
    const val ZERO = "0"
    const val DOUBLE_ZERO = "00"
    const val ADD = "+"
    const val SUBTRACT = "-"
    const val MULTIPLY = "*"
    const val DIVIDE = "/"
    const val DIVIDE_ = "÷"
    const val MULTIPLY_ = "×"
    const val MOD = "%"
    const val DOT = "."
    const val ARTICLE_TABLE = "article_table"
    const val BOOKMARK_TABLE = "bookmark_table"
    const val ARTICLE_REMOTE_KEYS_TABLE = "article_remote_keys"
    const val NEWS_DATABASE = "news_database"
    const val BOOKMARK_DATABASE = "bookmark_database"
    var DATA : MutableLiveData<String> = MutableLiveData<String>("00 : 00")
    var SECONDS : MutableLiveData<Int> = MutableLiveData(0)
}