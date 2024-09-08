package com.example.gallery.util

object Util {
    fun convert(songs_duration: String): String? {
        var songs_duration = songs_duration ?: return "0"
        return try {
            val longValue = java.lang.Long.valueOf(songs_duration).toLong() / 1000
            val j = longValue / 60
            val j2 = longValue % 60
            songs_duration = if (j2 < 10) {
                "$j:0$j2"
            } else {
                "$j:$j2"
            }
            songs_duration
        } catch (unused: NumberFormatException) {
            songs_duration
        }
    }

}