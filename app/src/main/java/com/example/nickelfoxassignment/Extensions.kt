package com.example.nickelfoxassignment

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