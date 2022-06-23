package com.example.nickelfoxassignment.assignment0

import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.PatternItem
import com.google.android.gms.maps.model.Polygon

class CustomPolygon {

    private val white = -0x1
    private val darkGreen = -0xc771c4
    private val lightGreen = -0x7e387c
    private val black = -0x1000000
    private val darkOrange = -0xa80e9
    private val lightOrange = -0x657db
    private val strokeWidth = 8
    private val dashLength = 20

    private val dash: PatternItem = Dash(dashLength.toFloat())

    // Create a stroke pattern of a gap followed by a dash.
    private val patternAlpha = listOf(dash)

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private val patternBETA = listOf(dash)

    fun stylePolygon(polygon: Polygon) {
        // Get the data object stored with the polygon.
        val type = polygon.tag?.toString() ?: ""
        var pattern: List<PatternItem>? = null
        var strokeColor = black
        var fillColor = white
        when (type) {
            "alpha" -> {
                // Apply a stroke pattern to render a dashed line, and define colors.
                pattern = patternAlpha
                strokeColor = darkGreen
                fillColor = lightGreen
            }
            "beta" -> {
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = patternBETA
                strokeColor = darkOrange
                fillColor = lightOrange
            }
        }
        polygon.strokePattern = pattern
        polygon.strokeWidth = strokeWidth.toFloat()
        polygon.strokeColor = strokeColor
        polygon.fillColor = fillColor
    }
}