package com.example.gallery.ui

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.gallery.ui.model.GalleryModel
import java.io.File
import java.util.Collections

class MediaRepository(private val context: Context) {

    fun fetchAllMedia(): List<MediaItem> {
        val mediaItems: MutableList<MediaItem> = ArrayList()
        val mediaUris = arrayOf(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )
        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.MIME_TYPE)
        for (uri in mediaUris) {
            val cursor = context.contentResolver.query(uri!!, projection, null, null, null)
            cursor?.use {
                val dataColumn = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                val mimeTypeColumn = it.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)
                while (it.moveToNext()) {
                    val path = it.getString(dataColumn)
                    val mimeType = it.getString(mimeTypeColumn)
                    mediaItems.add(MediaItem(path, mimeType))
                }
            }
        }
        return mediaItems
    }

    @SuppressLint("Range")
    fun setImages(): List<GalleryModel> {
        val contentUri: Uri
        val arrayList: ArrayList<GalleryModel> = ArrayList()
        try {
            contentUri = if (Build.VERSION.SDK_INT >= 29) {
                MediaStore.Files.getContentUri("external_primary")
            } else {
                MediaStore.Files.getContentUri("external")
            }
            val query = context.contentResolver.query(
                contentUri,
                null,
                "media_type in (1, 3)",
                null,
                "date_modified DESC"
            ) ?: return arrayList

            query.use {
                val columnIndex = it.getColumnIndex("_id")
                val count = it.count
                for (i in 0 until count) {
                    it.moveToPosition(i)
                    val columnIndex2 = it.getColumnIndex("_data")
                    val columnIndex3 = it.getColumnIndex("date_added")
                    val path = it.getString(columnIndex2)
                    it.getLong(columnIndex3)
                    val galleryModel = GalleryModel().apply {
                        this.path = path
                        val dateTaken = it.getLong(it.getColumnIndex("datetaken"))
                        val dateModified = it.getLong(it.getColumnIndex("date_modified")) * 1000
                        val j = maxOf(dateTaken, dateModified)
                        this.days = j.toString()
                        this.month = j.toString()
                        this.year = j.toString()
                        this.datetaken = j
                        this.size = it.getLong(it.getColumnIndexOrThrow("_size"))
                        this.uri = if (path?.contains(".mp4") == true) {
                            ContentUris.withAppendedId(
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                it.getInt(columnIndex).toLong()
                            ).toString()
                        } else {
                            ContentUris.withAppendedId(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                it.getInt(columnIndex).toLong()
                            ).toString()
                        }
                        this.bucketDisplayName = it.getString(it.getColumnIndex("bucket_display_name")) ?: "unknow_album"
                        this.bucketId = it.getLong(it.getColumnIndex("bucket_id"))
                        this.duration = it.getString(it.getColumnIndexOrThrow("duration"))
                        this.width = it.getInt(it.getColumnIndexOrThrow("width"))
                        this.height = it.getInt(it.getColumnIndexOrThrow("height"))
                    }

                    if (File(galleryModel.path!!).length() > 0) {
                        arrayList.add(galleryModel)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return arrayList
    }





}
