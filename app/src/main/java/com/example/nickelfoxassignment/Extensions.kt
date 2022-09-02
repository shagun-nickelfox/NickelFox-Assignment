package com.example.nickelfoxassignment

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.PopupMenu
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

fun showPopUpMenu(menuRes: Int, view: View): PopupMenu {
    val popupMenu = PopupMenu(view.context, view)
    popupMenu.menuInflater.inflate(menuRes, popupMenu.menu)
    try {
        val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
        fieldMPopup.isAccessible = true
        val mPopup = fieldMPopup.get(popupMenu)
        mPopup.javaClass
            .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
            .invoke(mPopup, true)
    } catch (e: Exception) {
    }
    return popupMenu
}


