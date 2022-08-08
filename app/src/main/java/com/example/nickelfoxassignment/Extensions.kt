package com.example.nickelfoxassignment

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity

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

fun Context.shareData(headline: String, url: String) {
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_SUBJECT, "News")
    var shareMessage = "\n" + headline + "\n\n"
    shareMessage = shareMessage + url + "\n\n"
    intent.putExtra(Intent.EXTRA_TEXT, shareMessage)
    startActivity(Intent.createChooser(intent, "Share to"))
}


