package com.example.gallery.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import com.example.gallery.ui.MediaViewModel;
import com.example.gallery.ui.model.GalleryModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FIleUtils {
    Context context;
    MediaViewModel homeViewModel;
    Update update;

    /* loaded from: classes2.dex */
    public interface Update {
        void onUpdate();
    }

    public FIleUtils(Context context, MediaViewModel homeViewModel, Update update) {
        this.context = context;
        this.homeViewModel = homeViewModel;
        this.update = update;
    }

    public FIleUtils(Context context, MediaViewModel homeViewModel) {
        this.context = context;
        this.homeViewModel = homeViewModel;
    }

    public void moveFile(String folder, List<GalleryModel> galleryModels) {
        ArrayList arrayList = new ArrayList();
        ArrayList<String> arrayList2 = new ArrayList<>();
        for (GalleryModel galleryModel : galleryModels) {
            File file = new File(galleryModel.getPath());
            if (Build.VERSION.SDK_INT >= 30) {
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("_display_name", file.getName());
                    String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(file.getPath());
                    contentValues.put("mime_type", fileExtensionFromUrl != null ? MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtensionFromUrl) : null);
                    contentValues.put("relative_path", Environment.DIRECTORY_DCIM + "/" + folder);
                    contentValues.put("bucket_display_name", folder);
                    FileProvider.getUriForFile(this.context, "com.photos.gallery.photoeditor.provider1", file);
                    this.context.getContentResolver().update(Uri.parse(galleryModel.getUri()), contentValues, null);
                    String str = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/" + folder + File.separator + file.getName();
                    arrayList.add(str);
                    arrayList2.add(str);
                    new File(str).getAbsoluteFile().hashCode();
                    this.update.onUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                File file2 = new File(Environment.getExternalStorageDirectory() + "/DCIM/" + folder + "/" + file.getName());
                if (file.renameTo(file2)) {
                    arrayList2.add(file2.getAbsolutePath());
                    arrayList.add(file2.getAbsolutePath());
                    file.delete();
                } else if (copy(file, file2)) {
                    arrayList2.add(file2.getAbsolutePath());
                    arrayList.add(file2.getAbsolutePath());
                    file.delete();
                }
                scanFile(this.context, arrayList2, galleryModel, folder);
            }
        }
    }

    private void scanFile(Context context, ArrayList<String> galleryModelList, GalleryModel galleryModel, String folder) {
        MediaScannerConnection.scanFile(context, (String[]) galleryModelList.toArray(new String[0]), null, new MediaScannerConnection.OnScanCompletedListener() { // from class: com.example.iphoto.util.FIleUtils$$ExternalSyntheticLambda1
            @Override // android.media.MediaScannerConnection.OnScanCompletedListener
            public final void onScanCompleted(String str, Uri uri) {
                FIleUtils.this.m258lambda$scanFile$0$comexampleiphotoutilFIleUtils(str, uri);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$scanFile$0$com-example-iphoto-util-FIleUtils, reason: not valid java name */
    public /* synthetic */ void m258lambda$scanFile$0$comexampleiphotoutilFIleUtils(String str, Uri uri) {
        new File(str).getAbsoluteFile().hashCode();
        this.update.onUpdate();
    }

    public static boolean copy(File file, File file2) {
        try {
            FileChannel channel = new FileInputStream(file).getChannel();
            try {
                FileChannel channel2 = new FileOutputStream(file2).getChannel();
                channel.transferTo(0L, channel.size(), channel2);
                if (channel2 != null) {
                    channel2.close();
                }
                channel.close();
                return true;
            } finally {
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Throwable unused) {
            return false;
        }
    }




    public static boolean checkURIResource(Context context, Uri uri) {
        Cursor query = context.getContentResolver().query(uri, null, null, null, null);
        boolean z = query != null && query.moveToFirst();
        if (query != null) {
            query.close();
        }
        return z;
    }

    public static void saveImageToExernal(Context context, List<GalleryModel> galleryModels, final Update update) {
        ArrayList arrayList = new ArrayList();
        for (GalleryModel galleryModel : galleryModels) {
            StringBuilder sb = new StringBuilder();
            sb.append(context.getFilesDir());
            String str = File.separator;
            sb.append(str);
            sb.append("/RecentlyDeleted");
            File file = new File(sb.toString());
            if (file.exists() || file.mkdirs()) {
                File file2 = new File(galleryModel.getPath());
                if (copy(file2, new File(file.getAbsolutePath() + str + file2.getName()))) {
                    arrayList.add(galleryModel.getPath());
                    file2.delete();
                }
                MediaScannerConnection.scanFile(context, (String[]) arrayList.toArray(new String[0]), null, new MediaScannerConnection.OnScanCompletedListener() { // from class: com.example.iphoto.util.FIleUtils$$ExternalSyntheticLambda0
                    @Override // android.media.MediaScannerConnection.OnScanCompletedListener
                    public final void onScanCompleted(String str2, Uri uri) {

                    }
                });
            }
        }
    }



    public static void copyFile(File src, FileOutputStream dst) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(src);
        FileChannel channel = fileInputStream.getChannel();
        channel.transferTo(0L, channel.size(), dst.getChannel());
        fileInputStream.close();
        dst.close();
    }

    public static boolean deleteFileFromMediaStore(Context context, String fileFullPath) {
        String str;
        File file = new File(fileFullPath);
        String str2 = null;
        try {
            str = file.getAbsolutePath();
        } catch (Exception unused) {
            str = null;
        }
        try {
            str2 = file.getCanonicalPath();
        } catch (Exception unused2) {
        }
        ArrayList arrayList = new ArrayList();
        if (str != null) {
            arrayList.add(str);
        }
        if (str2 != null && !str2.equalsIgnoreCase(str)) {
            arrayList.add(str2);
        }
        if (arrayList.size() == 0) {
            return false;
        }
        ContentResolver contentResolver = context.getContentResolver();
        Uri contentUri = MediaStore.Files.getContentUri("external");
        Iterator it = arrayList.iterator();
        boolean z = false;
        while (it.hasNext()) {
            if (contentResolver.delete(contentUri, "_data=?", new String[]{(String) it.next()}) != 0) {
                z = true;
            }
        }
        return z;
    }

    public static String getShortName(String path) {
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        int lastIndexOf = path.lastIndexOf(47);
        return lastIndexOf == -1 ? path : path.substring(lastIndexOf + 1);
    }

    public static String getstringfromContentUri(Context context, Uri uri) {
        try {
            Cursor query = context.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
            if (query == null) {
                return null;
            }
            String string = query.moveToNext() ? query.getString(query.getColumnIndex("_data")) : null;
            query.close();
            return string;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
