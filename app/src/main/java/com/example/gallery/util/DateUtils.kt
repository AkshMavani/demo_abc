package com.example.gallery.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    fun formatDate(date: Long): String? {
        return SimpleDateFormat("dd", Locale.ENGLISH).format(Date(date))
    }

    fun formatMonth(date: Long): String? {
        return SimpleDateFormat("MM", Locale.ENGLISH).format(Date(date))
    }

    fun formatYear(date: Long): String? {
        return SimpleDateFormat("yyyy", Locale.ENGLISH).format(Date(date))
    }

    fun formatdateImage(date: Long): String? {
        return SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(java.lang.Long.valueOf(date))
    }

    fun formatdateMonth(date: Long): String? {
        return SimpleDateFormat("MMMM yyyy", Locale.ENGLISH).format(java.lang.Long.valueOf(date))
    }

    fun formatdateMonthItem(date: Long): String? {
        return SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH).format(java.lang.Long.valueOf(date))
    }

    fun formatdateYear(date: Long): String? {
        return SimpleDateFormat("yyyy", Locale.ENGLISH).format(java.lang.Long.valueOf(date))
    }
}