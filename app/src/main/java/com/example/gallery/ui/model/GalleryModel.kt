package com.example.gallery.ui.model

import com.example.gallery.util.DateUtils
import com.example.gallery.util.Util
import java.io.Serializable

data class GalleryModel(
    var bookmark: Long = 0L,
    var bucketDisplayName: String? = null,
    var bucketId: Long = 0L,
    var choose: Boolean = false,
    var dateAdd: Long = 0L,
    var datetaken: Long = 0L,
    var days: String? = null,
    var duration: String? = null,
    var favourate: Boolean = false,
    var height: Int = 0,
    var id: Int = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var month: String? = null,
    var path: String? = null,
    var resolution: String? = null,
    var size: Long = 0L,
    var typeMedia: Int = 0,
    var uri: String? = null,
    var width: Int = 0,
    var year: String? = null
) : Serializable {

    // Custom getter for days with logic to format the date
    fun getFormattedDays(): String? = days?.let { DateUtils.formatDate(it.toLong()) }

    // Custom getter for month with logic to format the date
    fun getFormattedMonth(): String? = month?.let { DateUtils.formatDate(it.toLong()) }

    // Custom getter for year with logic to format the date
    fun getFormattedYear(): String? = year?.let { DateUtils.formatDate(it.toLong()) }

    // Custom getter for duration with logic to convert the duration
    fun getFormattedDuration(): String? = duration?.let { Util.convert(it) }
}
