package com.example.gallery.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.example.gallery.ui.model.AlbumDetail;
import com.example.gallery.ui.model.GalleryModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Video_Recently_Utils {
    @SuppressLint("Range")
    public static  void GalleryImagesTrash(Context context) throws Throwable {
        Uri uri;
        try {
            ArrayList arrayList = new ArrayList();
            Bundle bundle = new Bundle();
            bundle.putInt("android:query-arg-match-trashed", 1);
            bundle.putString("android:query-arg-sql-selection", "is_trashed = 1 ");
            bundle.putString("android:query-arg-sql-sort-order", "date_modified DESC");
            if (Build.VERSION.SDK_INT >= 30) {
                uri = MediaStore.Files.getContentUri("external");
            } else {
                uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            }
            Cursor query = Build.VERSION.SDK_INT >= 26 ? context.getContentResolver().query(uri, null, bundle, null) : null;
            int columnIndex = query.getColumnIndex("_id");
            int count = query.getCount();
            for (int i = 0; i < count; i++) {
                query.moveToPosition(i);
                query.getInt(columnIndex);
                query.getString(query.getColumnIndex("relative_path"));
                query.getString(query.getColumnIndex("_display_name"));
                query.getString(query.getColumnIndexOrThrow("_data"));
                query.getString(query.getColumnIndexOrThrow("bucket_display_name"));
                int columnIndex2 = query.getColumnIndex("_data");
                int columnIndex3 = query.getColumnIndex("date_added");
                String string = query.getString(columnIndex2);
                long j = query.getLong(columnIndex3);
                GalleryModel galleryModel = new GalleryModel();
                galleryModel.setPath(string);
                galleryModel.setDateAdd(j);
                @SuppressLint("Range") long j2 = query.getLong(query.getColumnIndex("datetaken"));
                @SuppressLint("Range") long j3 = query.getLong(query.getColumnIndex("date_modified")) * 1000;
                if (j3 > j2) {
                    j2 = j3;
                }
                galleryModel.setDays(String.valueOf(j2));
                galleryModel.setMonth(String.valueOf(j2));
                galleryModel.setYear(String.valueOf(j2));
                galleryModel.setDatetaken(j2);
                galleryModel.setSize(query.getLong(query.getColumnIndexOrThrow("_size")));
                if (galleryModel.getPath().contains(".mp4")) {
                    galleryModel.setUri(String.valueOf(ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, query.getInt(query.getColumnIndex("_id")))));
                } else {
                    galleryModel.setUri(String.valueOf(ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, query.getInt(query.getColumnIndex("_id")))));
                }
                @SuppressLint("Range") String string2 = query.getString(query.getColumnIndex("bucket_display_name"));
                if (string2 == null) {
                    string2 = "unknow_album";
                }
                galleryModel.setBucketDisplayName(string2);
                galleryModel.setBucketId(query.getLong(query.getColumnIndex("bucket_id")));
                galleryModel.setDuration(query.getString(query.getColumnIndexOrThrow("duration")));
                galleryModel.setWidth(query.getInt(query.getColumnIndexOrThrow("width")));
                galleryModel.setHeight(query.getInt(query.getColumnIndexOrThrow("height")));
                if (new File(galleryModel.getPath()).length() > 0) {
                    arrayList.add(galleryModel);
                }
            }
            Log.e("Arr_list1", "trush: "+arrayList );
            Collections.reverse(arrayList);
            query.close();

        } catch (Exception e) {
            e.getMessage();
        }
    }
    @SuppressLint("Range")
    public static  ArrayList<AlbumDetail> GalleryVideo(Context context){
        Uri uri;
        ArrayList arrayList = new ArrayList();
        try {

            if (Build.VERSION.SDK_INT >= 29) {
                uri = MediaStore.Video.Media.getContentUri("external");
            } else {
                uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            }
            Cursor query = context.getContentResolver().query(uri, null, null, null, "date_modified DESC");
            int columnIndex = query.getColumnIndex("_id");
            int count = query.getCount();
            for (int i = 0; i < count; i++) {
                query.moveToPosition(i);
                query.getInt(columnIndex);
                int columnIndex2 = query.getColumnIndex("_data");
                int columnIndex3 = query.getColumnIndex("date_added");
                String string = query.getString(columnIndex2);
                long j = query.getLong(columnIndex3);
                GalleryModel galleryModel = new GalleryModel();
                galleryModel.setPath(string);
                galleryModel.setDateAdd(j);
                @SuppressLint("Range") long j2 = query.getLong(query.getColumnIndex("datetaken"));
                @SuppressLint("Range") long j3 = query.getLong(query.getColumnIndex("date_modified")) * 1000;
                if (j3 > j2) {
                    j2 = j3;
                }
                galleryModel.setDays(String.valueOf(j2));
                galleryModel.setMonth(String.valueOf(j2));
                galleryModel.setYear(String.valueOf(j2));
                galleryModel.setDatetaken(j2);
                galleryModel.setSize(query.getLong(query.getColumnIndexOrThrow("_size")));
                galleryModel.setUri(String.valueOf(ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, query.getInt(query.getColumnIndex("_id")))));
                @SuppressLint("Range") String string2 = query.getString(query.getColumnIndex("bucket_display_name"));
                if (string2 == null) {
                    string2 = "unknow_album";
                }
                galleryModel.setBucketDisplayName(string2);
                galleryModel.setBucketId(query.getLong(query.getColumnIndex("bucket_id")));
                galleryModel.setDuration(query.getString(query.getColumnIndexOrThrow("duration")));
                galleryModel.setWidth(query.getInt(query.getColumnIndexOrThrow("width")));
                galleryModel.setHeight(query.getInt(query.getColumnIndexOrThrow("height")));
                if (new File(galleryModel.getPath()).length() > 0) {
                    arrayList.add(galleryModel);
                }
            }
            Log.e("Arr_list1", "GalleryVideo: "+arrayList );
            Collections.reverse(arrayList);
            query.close();
        } catch (Exception e) {
            e.getMessage();
        }
        return arrayList;
    }
    public static void getListAlbum(Context context) throws Throwable {
        Uri contentUri;
        try {
            ArrayList arrayList = new ArrayList();
            Bundle bundle = new Bundle();
            bundle.putString("android:query-arg-sql-sort-order", "date_modified DESC");
            bundle.putString("android:query-arg-sql-group-by", "bucket_display_name");
            if (Build.VERSION.SDK_INT >= 30) {
                contentUri = MediaStore.Files.getContentUri("external_primary");
            } else {
                contentUri = MediaStore.Files.getContentUri("external");
            }
            Cursor query = Build.VERSION.SDK_INT >= 26 ? context.getContentResolver().query(contentUri, null, bundle, null) : null;
            int count = query.getCount();
            for (int i = 0; i < count; i++) {
                AlbumDetail albumDetail = new AlbumDetail();
                query.moveToPosition(i);
                @SuppressLint("Range") String string = query.getString(query.getColumnIndex("_data"));
                @SuppressLint("Range") String string2 = query.getString(query.getColumnIndex("bucket_display_name"));
                @SuppressLint("Range") int i2 = query.getInt(query.getColumnIndex("bucket_id"));
                albumDetail.setPath(string);
                albumDetail.setBuget_name(string2);
                albumDetail.setBugetId(i2);
                albumDetail.setCount(getCount(context, string2));
                if (string.contains("mp4") || string.contains("jpeg") || string.contains("jpg") || string.contains("png")) {
                    arrayList.add(albumDetail);
                    Log.e("Arr_list1", "getListAlbum: "+arrayList );
                    Log.e("Arr_list1", "getListAlbum: "+ string);
                    Log.e("Arr_list1", "getListAlbum: "+ string2);
                    Log.e("Arr_list1", "getListAlbum: "+ i2);


                }
            }

            query.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }
    public static List<GalleryModel> getAlbumNames(List<GalleryModel> galleryModels, String bugetId) {
        ArrayList arrayList = new ArrayList();
        if (galleryModels != null && galleryModels.size() > 0) {
            for (int i = 0; i < galleryModels.size(); i++) {
                if (galleryModels.get(i).getPath() != null && galleryModels.get(i).getBucketDisplayName().equals(bugetId)) {
                    arrayList.add(galleryModels.get(i));
                }
            }
        }
        return arrayList;
    }

    public static int getCount(final Context context, final String bucketId) {
        int i = 0;
        Cursor query = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), null, "bucket_display_name=?", new String[]{bucketId}, null);
        if (query != null) {
            try {
                if (query.moveToFirst()) {
                    i = query.getCount();
                }
            } catch (Throwable th) {
                if (query != null) {
                    try {
                        query.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        if (query != null) {
            query.close();
        }
        return i;
    }
    public static List<GalleryModel> getAlbumScreenShots(List<GalleryModel> galleryModels) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < galleryModels.size(); i++) {
            if (galleryModels.get(i).getPath() != null && galleryModels.get(i).getBucketDisplayName().contains("Screenshots")) {
                arrayList.add(galleryModels.get(i));
            }
        }
        return arrayList;
    }

    public static int getCountALbum(List<GalleryModel> galleryModels, String bugetId) {
        if (galleryModels == null || galleryModels.size() <= 0) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < galleryModels.size(); i2++) {
            if (galleryModels.get(i2).getPath() != null && galleryModels.get(i2).getBucketDisplayName().equals(bugetId)) {
                i++;
            }
        }
        return i;
    }



}
