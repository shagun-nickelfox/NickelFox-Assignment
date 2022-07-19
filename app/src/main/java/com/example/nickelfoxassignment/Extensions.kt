package com.example.nickelfoxassignment

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

fun Toolbar.showToolbar(
    titleToolbar: String,
    titleColor: Int,
    backColor: Int
): Toolbar {
    title = titleToolbar
    setBackgroundColor(
        ContextCompat.getColor(
            context,
            backColor
        )
    )
    setTitleTextColor(
        ContextCompat.getColor(
            context,
            titleColor
        )
    )
    return this
}

fun Context.shortToast(text: String) {
    Toast.makeText(
        this,
        text,
        Toast.LENGTH_SHORT
    ).show()
}

fun Context.longToast(text: String) {
    Toast.makeText(
        this,
        text,
        Toast.LENGTH_LONG
    ).show()
}

fun <T> Context.showAnotherActivity(activity: Class<T>) {
    val intent = Intent(this, activity)
    startActivity(intent)
}


