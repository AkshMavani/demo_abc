package com.example.gallery;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupMenu;

import com.example.abc.BottomSheetDialog;
import com.example.demo_full.R;
import com.example.demo_full.databinding.FragmentDetailImage2Binding;
import com.example.gallery.ui.MediaRepository;
import com.example.gallery.ui.MediaViewModel;
import com.example.gallery.ui.MediaViewModelFactory;
import com.example.gallery.ui.adapter.AddPhotoToGallery;
import com.example.gallery.ui.adapter.ThumbnailAdapter;
import com.example.gallery.ui.model.GalleryModel;
import com.example.gallery.util.AlbumUtil;
import com.example.gallery.util.DragLayout;
import com.example.gallery.util.FIleUtils;
import com.example.gallery.util.IVideoUpdate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DetailImageFragment2 extends Fragment {
    static final int MIN_DISTANCE = 150;
    public static final int VOLUME_PRESSED__INTERVAL = 100;
    FragmentDetailImage2Binding binding;

//    AddPhotoToGallery bottomSheetAddPhotoToAlbum;
    private GestureDetectorCompat detector;
    float hScale;
    int height;
    MainActivity11 homeActivity;
    MediaViewModel homeViewModel;
    AddPhotoToGallery bottomSheetAddPhotoToAlbum;
    boolean isLoad;
    private ConstraintLayout.LayoutParams layoutParams;
    int left;
    String name;
    PagerAdapterMediaFragment pagerAdapterMediaFragment;
    ThumbnailAdapter thumbnailAdapter;
    int top;
    int type;
    float wScale;
    int widget;
    private float x1;
    private float x2;
    List<GalleryModel> mGalleryModels = new ArrayList();
    boolean isplay = true;
    boolean ismute = true;
    int[] screenLocation1 = new int[2];
    private boolean skipNavigationView = false;
    private long upKeyEventTime = 0;
    private long downKeyEventTime = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$onActivityResult$12() {
    }

    public void clikNewFolder(String name) {
    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailImageFragment2() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DetailImageFragment2 newInstances(int galleryModel, String name, int type, int[] screenLocation, int widget, int height) {
        Bundle bundle = new Bundle();
        bundle.putInt("INDEX_DETAIL_IMAGE", galleryModel);
        bundle.putString("budget_name", name);
        bundle.putInt("type", type);
        bundle.putInt("widget", widget);
        bundle.putInt("height", height);
        bundle.putIntArray("screenLocation", screenLocation);
        DetailImageFragment2 detailImageFragment = new DetailImageFragment2();
        detailImageFragment.setArguments(bundle);
        return detailImageFragment;
    }

    public static DetailImageFragment2 newInstances(int type, int[] screenLocation, int widget, int height) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putInt("widget", widget);
        bundle.putInt("height", height);
        bundle.putIntArray("screenLocation", screenLocation);
        DetailImageFragment2 detailImageFragment = new DetailImageFragment2();
        detailImageFragment.setArguments(bundle);
        return detailImageFragment;
    }

    public static DetailImageFragment2 newInstances(int[] screenLocation, int widget, int height) {
        Bundle bundle = new Bundle();
        bundle.putInt("widget", widget);
        bundle.putInt("height", height);
        bundle.putIntArray("screenLocation", screenLocation);
        DetailImageFragment2 detailImageFragment = new DetailImageFragment2();
        detailImageFragment.setArguments(bundle);
        return detailImageFragment;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve arguments
        if (getArguments() != null) {
            int galleryModel = getArguments().getInt("INDEX_DETAIL_IMAGE", -1); // Default to -1 if not present
            String budgetName = getArguments().getString("budget_name", "");   // Default to empty string
            int type = getArguments().getInt("type", 0);                      // Default to 0
            int widget = getArguments().getInt("widget", 0);                  // Default to 0
            int height = getArguments().getInt("height", 0);                  // Default to 0
            int[] screenLocation = getArguments().getIntArray("screenLocation");

            // Log the retrieved values for debugging
            Log.d("DetailImageFragment2", "Gallery Model: " + galleryModel);
            Log.d("DetailImageFragment2", "Budget Name: " + budgetName);
            Log.d("DetailImageFragment2", "Type: " + type);
            Log.d("DetailImageFragment2", "Widget: " + widget);
            Log.d("DetailImageFragment2", "Height: " + height);
            if (screenLocation != null) {
                Log.d("DetailImageFragment2", "Screen Location: x=" + screenLocation[0] + ", y=" + screenLocation[1]);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

            // Retrieve values from the bundle using the provided keys




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentDetailImage2Binding.inflate(getLayoutInflater());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" +requireContext().getPackageName()));
                startActivity(intent);
            }
        }
        Log.e("GETLIST", "onCreateView: "+getImagesFromAppFolder() );
        binding.inHeader.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runExitAnimaton();
            }
        });
        binding.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        MediaRepository repository = new MediaRepository(requireContext()); // Pass the context
        MediaViewModelFactory factory = new MediaViewModelFactory(repository);

        homeViewModel = new ViewModelProvider(this, factory).get(MediaViewModel.class);
        initList();

        setuplist();
//        this.pagerAdapterMediaFragment.getItem(GalleryFragment.Companion.getCurrentPosition());
//        this.binding.pagerPhotos.setAdapter(this.pagerAdapterMediaFragment);
//       binding.pagerPhotos.setCurrentItem(GalleryFragment.Companion.getCurrentPosition(), false);
      /*  this.binding.dragLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.example.iphoto.ui.DetailImageFragment.7
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                DetailImageFragment2.this.binding.dragLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                int[] iArr = new int[2];
                DetailImageFragment2.this.binding.dragLayout.getLocationOnScreen(iArr);
                DetailImageFragment2 detailImageFragment = DetailImageFragment2.this;
                detailImageFragment.left = detailImageFragment.screenLocation1[0] - iArr[0];
                DetailImageFragment2 detailImageFragment2 = DetailImageFragment2.this;
                detailImageFragment2.top = detailImageFragment2.screenLocation1[1] - iArr[1];
                int widget = getArguments().getInt("widget", 0);                  // Default to 0
                int height = getArguments().getInt("height", 0);                  // Default to 0
                int[] screenLocation = getArguments().getIntArray("screenLocation");

                DetailImageFragment2.this.wScale = widget/ DetailImageFragment2.this.binding.dragLayout.getWidth();
                DetailImageFragment2.this.hScale = height / DetailImageFragment2.this.binding.dragLayout.getHeight();
                DetailImageFragment2.this.runEnterAnimation();
                return true;
            }
        });
        this.binding.dragLayout.setOnClickListener(new View.OnClickListener() { // from class: com.example.iphoto.ui.DetailImageFragment.11
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
            }
        });*/
        binding.pagerPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "onClick: " );
            }
        });
        this.binding.inBottom.imgDelete.setOnClickListener(new View.OnClickListener() { // from class: com.example.iphoto.ui.DetailImageFragment$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {

            }
        });
        /*this.binding.dragLayout.setOnClickListener(new View.OnClickListener() { // from class: com.example.iphoto.ui.DetailImageFragment.11
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
            }
        });
        this.binding.dragLayout.setDragListener(new DragLayout.DragListener() { // from class: com.example.iphoto.ui.DetailImageFragment.12
            @Override
            public void onDragStarted(boolean check) {
            }

            @Override
            public void onDragFinished(float x, float y) {
                if (y > 50.0f || y < -100.0f) {
                    DetailImageFragment2.this.runExitAnimaton();
                    return;
                }

               *//* DetailImageFragment2.this.binding.inHeader.getRoot().setVisibility(0);

                TypedValue typedValue = new TypedValue();
                DetailImageFragment2.this.getContext().getTheme().resolveAttribute(R.attr.background_color_main, typedValue, true);
                if (typedValue.resourceId != 0) {
                    DetailImageFragment2.this.binding.dragLayout.setBackgroundResource(typedValue.resourceId);
                } else {
                    DetailImageFragment2.this.binding.dragLayout.setBackgroundColor(typedValue.data);
                }*//*
            }

            @Override
            public void onDrag(float x, float y) {
                DetailImageFragment2.this.binding.inHeader.getRoot().setVisibility(4);

                DetailImageFragment2.this.binding.dragLayout.setBackgroundColor(0);
                DetailImageFragment2.this.binding.pagerPhotos.setBackgroundColor(0);
                DetailImageFragment2.this.binding.rrrrr.setBackgroundColor(0);
            }
        });*/

        return binding.getRoot();

    }
    /*public void runEnterAnimation() {
        this.binding.dragLayout.setPivotX(0.0f);
        this.binding.dragLayout.setPivotY(0.0f);
        try {
            this.binding.dragLayout.setScaleX(this.wScale);
            this.binding.dragLayout.setScaleY(this.hScale);
        } catch (Exception unused) {
        }
        this.binding.dragLayout.setTranslationX(this.left);
        this.binding.dragLayout.setTranslationY(this.top);
        long j = 150;
        this.binding.dragLayout.animate().setDuration(j).scaleX(1.0f).scaleY(1.0f).translationX(0.0f).translationY(0.0f).setInterpolator(new DecelerateInterpolator());
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator ofInt = ObjectAnimator.ofInt(this.binding.getRoot(), "alpha", 0, 255);
        ofInt.setDuration(j);
        ofInt.start();
    }*/

    public void initList() {
        List<GalleryModel> list = this.mGalleryModels;
        String str = this.name;
                if (str != null) {
                    this.mGalleryModels.addAll(AlbumUtil.getAlbumNames(homeViewModel.getGalleryItemsLiveData().getValue(), this.name));
                } else if (str != null) {
                    this.mGalleryModels.clear();
                    this.mGalleryModels.addAll(AlbumUtil.getAlbumNames(homeViewModel.getGalleryItemsLiveData().getValue(), this.name));
                } else if (homeViewModel.getGalleryItemsLiveData().getValue() != null) {
                    this.mGalleryModels.clear();
                    this.mGalleryModels.addAll(homeViewModel.getGalleryItemsLiveData().getValue());
                }

        if (this.mGalleryModels.size() != 0 || getFragmentManager() == null) {
            return;
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.example.iphoto.ui.DetailImageFragment.16
            @Override // java.lang.Runnable
            public void run() {
                try {
                    DetailImageFragment2.this.getFragmentManager().popBackStackImmediate();
                } catch (Exception unused2) {
                }
                if (DetailImageFragment2.this.getActivity() != null) {
                    DetailImageFragment2.this.homeActivity.hideDelete();
                }
                DetailImageFragment2.this.homeActivity.getNavView().setVisibility(View.VISIBLE);
            }
        }, 100L);
    }
    public void setuplist() {

            this.pagerAdapterMediaFragment = new PagerAdapterMediaFragment(this, this.mGalleryModels, new IVideoUpdate() { // from class: com.example.iphoto.ui.DetailImageFragment.15
                @Override // com.example.iphoto.callback.IVideoUpdate
                public void resumePlay() {
//                    DetailImageFragment2.this.setPlayVideo(true);
                }

                @Override // com.example.iphoto.callback.IVideoUpdate
                public void updateClickItem() {
                    Log.e("CHL1", "updateClickItem:>>> "+name );
                 /*   DetailImageFragment2.this.bottomSheetAddPhotoToAlbum = new AddPhotoToGallery(DetailImageFragment2.this, DetailImageFragment2.this.name, DetailImageFragment2.this.mGalleryModels);
                    DetailImageFragment2.this.bottomSheetAddPhotoToAlbum.show(DetailImageFragment2.this.getChildFragmentManager(), DetailImageFragment2.this.getTag());*/
                    moveImageToAppFolder(mGalleryModels.get(GalleryFragment.Companion.getCurrentPosition()).getPath());

                    if (DetailImageFragment2.this.binding.inHeader.getRoot().getVisibility() == 0) {
                        DetailImageFragment2.this.binding.inHeader.getRoot().setVisibility( 4);
//                        DetailImageFragment2.this.binding.inBottom.getRoot().setVisibility(4);
//                        DetailImageFragment2.this.binding.navigationViewDetail.setVisibility(4);
//                        DetailImageFragment2.this.binding.inBottomDelete.getRoot().setVisibility(8);
//                        DetailImageFragment2.this.binding.rcvThumbImageBottom.setVisibility(4);
                      //  DetailImageFragment2.this.binding.dragLayout.setBackgroundColor(-16777216);
                        return;
                    }
                    DetailImageFragment2.this.binding.inHeader.getRoot().setVisibility(0);
                    if (DetailImageFragment2.this.type == 3) {
//                        DetailImageFragment2.this.binding.inBottomDelete.getRoot().setVisibility(0);
//                        DetailImageFragment2.this.binding.inBottom.getRoot().setVisibility(4);
//                        DetailImageFragment2.this.binding.navigationViewDetail.setVisibility(4);
                    } else {
//                        DetailImageFragment2.this.binding.inBottomDelete.getRoot().setVisibility(8);
//                        DetailImageFragment2.this.binding.inBottom.getRoot().setVisibility(0);
//                        DetailImageFragment2.this.binding.navigationViewDetail.setVisibility(0);
                    }
//                    DetailImageFragment2.this.binding.rcvThumbImageBottom.setVisibility(0);
                    TypedValue typedValue = new TypedValue();
                    DetailImageFragment2.this.getActivity().getTheme().resolveAttribute(R.attr.background_color_main, typedValue, true);
                    if (typedValue.resourceId != 0) {
                     //   DetailImageFragment2.this.binding.dragLayout.setBackgroundResource(typedValue.resourceId);
                    } else {
                      //  DetailImageFragment2.this.binding.dragLayout.setBackgroundColor(typedValue.data);
                    }
                }
            });
            this.binding.pagerPhotos.setAdapter(this.pagerAdapterMediaFragment);
            this.binding.pagerPhotos.setCurrentItem(GalleryFragment.Companion.getCurrentPosition(), false);

        PagerAdapterMediaFragment pagerAdapterMediaFragment = this.pagerAdapterMediaFragment;
        if (pagerAdapterMediaFragment != null) {
            pagerAdapterMediaFragment.notifyDataSetChanged();
        }
    }
    public void runExitAnimaton() {
        this.binding.inHeader.getRoot().setVisibility(4);
//        this.binding.inBottom.getRoot().setVisibility(4);
//        this.binding.navigationViewDetail.setVisibility(4);
//        this.binding.rcvThumbImageBottom.setVisibility(4);
     //   this.binding.dragLayout.setBackgroundColor(0);
        this.binding.pagerPhotos.setBackgroundColor(0);
        long j = 150;
    /*    this.binding.dragLayout.animate().setDuration(j).scaleX(this.wScale).scaleY(this.hScale).translationX(this.left).translationY(this.top).withEndAction(new Runnable() { // from class: com.example.iphoto.ui.DetailImageFragment$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                DetailImageFragment2.this.m224x291f216a();
            }
        });*/
        ObjectAnimator ofInt = ObjectAnimator.ofInt(this.binding.getRoot(), "alpha", 0, 255);
        ofInt.setDuration(j);
        ofInt.start();
        try {
//            PopupNetworkAds.setupPopupAds(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public /* synthetic */ void m224x291f216a() {
        ((MainActivity11) getActivity()).hideDelete();
        ((MainActivity11) getActivity()).getNavView().setVisibility(View.VISIBLE);
        new Handler().post(new Runnable() { // from class: com.example.iphoto.ui.DetailImageFragment.18
            @Override // java.lang.Runnable
            public void run() {
                if (DetailImageFragment2.this.getFragmentManager().getBackStackEntryCount() >= 0) {
                    DetailImageFragment2.this.getFragmentManager().popBackStack();
                } else {
                    DetailImageFragment2.this.getActivity().onBackPressed();
                }
            }
        });
    }
    public static int getDeviceWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        return displayMetrics.widthPixels;
    }

    public static int getDeviceHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        }
        return displayMetrics.heightPixels;
    }
    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 333 && resultCode == -1) {

            new Handler().post(new Runnable() { // from class: com.example.iphoto.ui.DetailImageFragment$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    m215xcc925db0();
                }
            });
        }
    }
    public /* synthetic */ void m215xcc925db0() {
        Log.e("GETDTA", "m215xcc925db0: " );
        new FIleUtils(getContext(), this.homeViewModel, new FIleUtils.Update() { // from class: com.example.iphoto.ui.DetailImageFragment$$ExternalSyntheticLambda4
            @Override // com.example.iphoto.util.FIleUtils.Update
            public final void onUpdate() {
                DetailImageFragment2.lambda$onActivityResult$12();
            }
        }).moveFile(this.bottomSheetAddPhotoToAlbum.getFolder(), this.bottomSheetAddPhotoToAlbum.getGalleryModelList());
    }
    public void moveImageToAppFolder(String sourcePath) {
        File sourceFile = new File(sourcePath);
        File destDir = new File(requireContext().getExternalFilesDir(null), "HiddenImages");

        if (!destDir.exists()) {
            destDir.mkdirs(); // Create folder if it doesn't exist
        }

        File destFile = new File(destDir, sourceFile.getName());
        if (sourceFile.renameTo(destFile)) {
            Log.d("FileOperation", "Image moved successfully: " + destFile.getPath());
        } else {
            Log.e("FileOperation", "Failed to move the image.");
        }
    }

    public List<File> getImagesFromAppFolder() {
        // Define the app's private "HiddenImages" folder
        File destDir = new File(requireContext().getExternalFilesDir(null), "HiddenImages");

        // Check if the directory exists
        if (!destDir.exists()) {
            Log.e("FileOperation", "HiddenImages folder does not exist.");
            return new ArrayList<>(); // Return an empty list if the folder doesn't exist
        }

        // List all image files in the folder
        File[] files = destDir.listFiles();
        List<File> imageFiles = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && isImageFile(file)) {
                    Log.e("GETLIST", "getImagesFromAppFolder: "+file );
                    imageFiles.add(file); // Add valid image files to the list
                }
            }
        }

        return imageFiles;
    }

    // Helper method to check if a file is an image
    private boolean isImageFile(File file) {
        String[] imageExtensions = {"jpg", "jpeg", "png", "gif", "bmp"};
        String fileName = file.getName().toLowerCase();

        for (String extension : imageExtensions) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

}














