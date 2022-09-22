package com.example.nickelfoxassignment.stopwatch

import java.util.*

class CountUpTimer(
    var time: Long,
    incrementer: Long = 1000L,
    private val listeners: CountUpListeners
) : Timer() {
    init {
        schedule(object : TimerTask() {
            override fun run() {
                time += incrementer
                listeners.onTick(time)
            }
        }, incrementer, incrementer)
    }
}