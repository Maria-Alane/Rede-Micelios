package com.example.micelios.presentation.common

object TimeFormatter {

    fun formatElapsedTime(from: Long, to: Long = System.currentTimeMillis()): String {
        val diffMinutes = ((to - from) / 1000 / 60).toInt()

        return when {
            diffMinutes <= 0 -> "há 0 min"
            diffMinutes < 60 -> "há $diffMinutes min"
            diffMinutes < 1440 -> "há ${diffMinutes / 60} h"
            else -> "há ${diffMinutes / 1440} d"
        }
    }
}