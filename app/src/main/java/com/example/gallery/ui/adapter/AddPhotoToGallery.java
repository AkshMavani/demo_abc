package com.example.gallery.ui.adapter;

import static com.example.gallery.util.AlbumUtil.getListAlbum;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.demo_full.R;
import com.example.demo_full.databinding.DialogAddPhotoToGalleryBinding;
import com.example.gallery.DetailImageFragment2;
import com.example.gallery.GalleryFragment;
import com.example.gallery.ui.MediaRepository;
import com.example.gallery.ui.MediaViewModel;
import com.example.gallery.ui.MediaViewModelFactory;
import com.example.gallery.ui.event.IImageClick;
import com.example.gallery.ui.model.AlbumDetail;
import com.example.gallery.ui.model.GalleryModel;
import com.example.gallery.util.DialogCreateAlbuminGallery;
import com.example.gallery.util.FIleUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AddPhotoToGallery extends BottomSheetDialogFragment implements IImageClick {
    AddImageAdapter addImageAdapter;
    DialogAddPhotoToGalleryBinding binding;
    DetailImageFragment2 context;
    String folder;
    List<GalleryModel> galleryModelList = new ArrayList();
    MediaViewModel homeViewModel;
    List<GalleryModel> mGalleryModels;
    int type;

    @Override // com.example.iphoto.callback.IImageClick
    public void add(int pos, GalleryModel galleryModel) {
    }

    @Override // com.example.iphoto.callback.IImageClick
    public void remove(int pos, GalleryModel galleryModel) {
    }

    public AddPhotoToGallery(DetailImageFragment2 context, String folder, List<GalleryModel> mgalleryModels) {
        ArrayList arrayList = new ArrayList();
        this.mGalleryModels = arrayList;
        this.folder = folder;
        this.context = context;
        this.type = this.type;
        arrayList.clear();
        Log.e("CHL1", "AddPhotoToGallery: "+mgalleryModels );
        this.mGalleryModels.addAll(mgalleryModels);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(0, R.style.BottomSheetDialog);
    }

    public String getFolder() {
        return this.folder;
    }

    public List<GalleryModel> getGalleryModelList() {
        return this.galleryModelList;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = DialogAddPhotoToGalleryBinding.inflate(getLayoutInflater());
        MediaRepository repository = new MediaRepository(requireContext()); // Or inject this
        MediaViewModelFactory factory = new MediaViewModelFactory(repository);

        this.homeViewModel = new ViewModelProvider(this, factory).get(MediaViewModel.class);
        ConstraintLayout root = this.binding.getRoot();
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.example.iphoto.view.AddPhotoToGallery.1
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface dialog) {
                View findViewById = ((BottomSheetDialog) dialog).findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior.from(findViewById).setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                BottomSheetBehavior.from(findViewById).setState(3);
            }
        });
        return root;
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        dismiss();
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            this.folder = getArguments().getString("folder");
        }

        Log.e("CHL1", "folder: "+folder );
        this.binding.imgThumb.setOnClickListener(new View.OnClickListener() { // from class: com.example.iphoto.view.AddPhotoToGallery.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                new DialogCreateAlbuminGallery(AddPhotoToGallery.this.context, AddPhotoToGallery.this.homeViewModel).showPictureialog(AddPhotoToGallery.this);
            }
        });
        this.binding.tvCancel.setOnClickListener(new View.OnClickListener() { // from class: com.example.iphoto.view.AddPhotoToGallery$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                AddPhotoToGallery.this.m261lambda$onViewCreated$0$comexampleiphotoviewAddPhotoToGallery(view2);
            }
        });
        homeViewModel.getMediaItemsLiveData().observe(getViewLifecycleOwner(), new Observer() { // from class: com.example.iphoto.ui.DetailAlbumFragment$$ExternalSyntheticLambda0
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Object obj) {
                ArrayList arrayList = new ArrayList();
                arrayList.clear();
                arrayList.addAll((Collection) obj);
                Log.e("CHL1", "onChanged: "+obj);
                Log.e("CHL1", "onChanged: "+arrayList);
                new AddImageAdapter(arrayList, getContext(), AddPhotoToGallery.this);
            }
        });

       // if (this.homeViewModel.mAlbumModels.getValue() != null) {
         //   HomeViewModel homeViewModel = this.homeViewModel;
            this.binding.rcvAllAlbums.setAdapter(new AlbumsGeneralAdapter(homeViewModel, getListAlbum(requireContext()), getContext(), new AlbumsGeneralAdapter.OnClickCustom() { // from class: com.example.iphoto.view.AddPhotoToGallery.3
                @Override // com.example.iphoto.adapter.AlbumsGeneralAdapter.OnClickCustom
                public void remove(AlbumDetail albumDetail) {
                }

                @Override // com.example.iphoto.adapter.AlbumsGeneralAdapter.OnClickCustom
                public void onCLick1(AlbumDetail albumDetail) {
                    AddPhotoToGallery.this.saveMedia(albumDetail);
                }
            }));
            this.binding.rcvAllAlbums.setLayoutManager(new GridLayoutManager(getContext(), 2, 1, false));
       //// }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: lambda$onViewCreated$0$com-example-iphoto-view-AddPhotoToGallery, reason: not valid java name */
    public /* synthetic */ void m261lambda$onViewCreated$0$comexampleiphotoviewAddPhotoToGallery(View view) {
        dismiss();
    }

    public void saveMedia(AlbumDetail albumDetail) {
        Log.e("CHL1", "saveMedia: "+albumDetail.getPath() );
        this.folder = albumDetail.getBuget_name();
        this.galleryModelList.clear();
        this.galleryModelList.add(this.mGalleryModels.get(GalleryFragment.Companion.getCurrentPosition()));
        Log.e("CHL111", "saveMedia: "+galleryModelList.size() );
        if (this.galleryModelList.size() == 0) {
            Toast.makeText(requireContext(), getActivity().getString(R.string.have_not_selected_photos), 0).show();
            return;
        }
        List<Uri> changelistImage = changelistImage((ArrayList) this.galleryModelList);
        if (Build.VERSION.SDK_INT >= 30) {
            try {
                this.context.startIntentSenderForResult(MediaStore.createWriteRequest(requireActivity().getContentResolver(), changelistImage).getIntentSender(), 333, null, 0, 0, 0, null);
                return;
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
                return;
            }
        }
        new Handler().post(new Runnable() { // from class: com.example.iphoto.view.AddPhotoToGallery.4
            @Override // java.lang.Runnable
            public void run() {
                Log.e("SVEEE", "run: "+homeViewModel );
                Log.e("SVEEE", "run: "+folder );
                Log.e("SVEEE", "run: "+galleryModelList );
                new FIleUtils(AddPhotoToGallery.this.context.getContext(), AddPhotoToGallery.this.homeViewModel, new FIleUtils.Update() { // from class: com.example.iphoto.view.AddPhotoToGallery.4.1
                    @Override // com.example.iphoto.util.FIleUtils.Update
                    public void onUpdate() {
                        AddPhotoToGallery.this.dismiss();
                    }
                }).moveFile(AddPhotoToGallery.this.folder, AddPhotoToGallery.this.galleryModelList);
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("CHL1", "onActivityResult: "+resultCode );
        Log.e("CHL1", "onActivityResult: "+requestCode );

    }

    public final List<Uri> changelistImage(ArrayList<GalleryModel> arrayList) {
        LinkedList linkedList = new LinkedList();
        Iterator<GalleryModel> it = arrayList.iterator();
        while (it.hasNext()) {
            linkedList.add(Uri.parse(it.next().getUri()));
        }
        return linkedList;
    }
}
