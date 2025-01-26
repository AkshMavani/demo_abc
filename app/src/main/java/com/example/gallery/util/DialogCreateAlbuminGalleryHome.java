//package com.example.gallery.util;
//
//import android.app.Dialog;
//import android.view.View;
//import android.view.Window;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.example.demo_full.R;
//import com.example.gallery.GalleryFragment;
//import com.example.gallery.ui.MediaViewModel;
//
//public class DialogCreateAlbuminGalleryHome {
//    Dialog dialog;
//    GalleryFragment generalAlbumFragment;
//    MediaViewModel homeViewModel;
//
//    public DialogCreateAlbuminGalleryHome(GalleryFragment detailImageFragment, MediaViewModel homeViewModel) {
//        this.generalAlbumFragment = detailImageFragment;
//        this.homeViewModel = homeViewModel;
//    }
//
//    public void showPictureialog(final AddPhotoToGalleryHome addPhotoToGallery) {
//        Dialog dialog = new Dialog(this.generalAlbumFragment.getContext());
//        this.dialog = dialog;
//        dialog.getWindow().requestFeature(1);
//        this.dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
//        Window window = this.dialog.getWindow();
//        window.setGravity(17);
//        window.setLayout(-1, -1);
//        this.dialog.setTitle((CharSequence) null);
//        this.dialog.setContentView(R.layout.dialog_create_albums);
//        this.dialog.setCancelable(true);
//        TextView textView = (TextView) this.dialog.findViewById(R.id.tvSave);
//        TextView textView2 = (TextView) this.dialog.findViewById(R.id.tvCancel);
//        final EditText editText = (EditText) this.dialog.findViewById(R.id.edtTitleAlbums);
//        textView2.setOnClickListener(new View.OnClickListener() { // from class: com.example.iphoto.view.DialogCreateAlbuminGalleryHome.1
//            @Override // android.view.View.OnClickListener
//            public void onClick(View view) {
//                DialogCreateAlbuminGalleryHome.this.dialog.dismiss();
//            }
//        });
//        textView.setOnClickListener(new View.OnClickListener() { // from class: com.example.iphoto.view.DialogCreateAlbuminGalleryHome.2
//            @Override // android.view.View.OnClickListener
//            public void onClick(View view) {
//                DialogCreateAlbuminGalleryHome.this.saveFolder(editText.getText().toString(), addPhotoToGallery);
//            }
//        });
//        this.dialog.show();
//    }
//
//    /* JADX INFO: Access modifiers changed from: private */
//    public void saveFolder(String title, AddPhotoToGalleryHome addPhotoToGallery) {
//        String trim = title.trim();
//        if (!trim.isEmpty() && !trim.matches(".*[/\n\r\t\u0000\f`?*\\<>|\":].*")) {
//            this.dialog.dismiss();
//        }
//        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/" + trim);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        AlbumDetail albumDetail = new AlbumDetail();
//        albumDetail.setBuget_name(title);
//        albumDetail.setFolder(true);
//        if (this.homeViewModel.mAlbumModels.getValue() != null) {
//            List<AlbumDetail> value = this.homeViewModel.mAlbumModels.getValue();
//            value.add(0, albumDetail);
//            this.homeViewModel.mAlbumModels.postValue(value);
//            this.homeViewModel.insertAlbums(albumDetail);
//            addPhotoToGallery.saveMedia(albumDetail);
//        }
//    }
//}
