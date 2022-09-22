package com.example.nickelfoxassignment.utils

import androidx.lifecycle.MutableLiveData

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
    const val CHANNEL_ID = "timer_notify"
    const val BROADCAST_NAME = "timer_broadcast"
    const val NOTIFICATION_NAME = "Timer Notification"
    const val NOTIFICATION_DESC = "This channel is used to display the current timer"
    const val SHARED_PREF = "TimerSharedPref"
    const val PATTERN = "mm:ss.SS"
    const val PATTERN_MIN_SEC = "mm:ss"
    const val TIME_ZONE = "GMT"
    val SECONDS: MutableLiveData<Long> = MutableLiveData(0)
}