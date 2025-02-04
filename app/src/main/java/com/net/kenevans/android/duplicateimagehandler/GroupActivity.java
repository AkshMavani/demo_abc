package com.net.kenevans.android.duplicateimagehandler;

import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.demo_full.R;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class GroupActivity extends AppCompatActivity implements IConstants {
    private Handler mHandler;
    private RecyclerView mListView;
    private ProgressBar mProgressBar;
    private List<Image> mImages;
    private List<Image> mInvalidImages;
    private List<Group> mGroups;
    private boolean mHandleDelete;
    private String mDirectory;
    private boolean mUseSubdirectories;

    ActivityResultLauncher<IntentSenderRequest> deleteResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartIntentSenderForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            Log.d(TAG, "deleteResultLauncher: "
                                    + ActivityResult.resultCodeToString(result.getResultCode()));
                            mHandleDelete = true;
                            if (result.getResultCode() == RESULT_OK) {
                                // Do nothing for now
                            } else if (result.getResultCode() == RESULT_CANCELED) {
                                Utils.errMsg(GroupActivity.this,
                                        "Deleting images was cancelled");
                            }
                        }
                    }
            );


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, this.getClass().getSimpleName() + ".onCreate:");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        mListView = findViewById(R.id.list);
        mListView.setLayoutManager(new GridLayoutManager(GroupActivity.this,3));
        mDirectory = null;
        mUseSubdirectories = true;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mDirectory = extras.getString(DIRECTORY_CODE, null);
            mUseSubdirectories = extras.getBoolean(USE_SUBDIRECTORIES_CODE,
                    true);
        }

        mProgressBar = findViewById(R.id.progressBar);
        mHandler = new Handler(getMainLooper());
        refresh();
    }

    @Override
    public void onResume() {
        Log.d(TAG, this.getClass().getSimpleName() + " onResume:"
                + "mHandleDelete=" + mHandleDelete);
        super.onResume();
        if (mHandleDelete) {
            mHandleDelete = false;
            refresh();
        }
    }

    @Override
    protected void onPause() {
        Log.v(TAG, this.getClass().getSimpleName() + " onPause");
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.info) {
            info();
            return true;
        } else if (id == R.id.remove_checked) {
            removeCheckedImages();
            return true;
        } else if (id == R.id.refresh) {
            refresh();
            return true;
        } else if (id == R.id.problem_images) {
            showProblemImages();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startFind() {
        Log.d(TAG, this.getClass().getSimpleName() + ".startFind:");
        new Thread(() -> {
            Log.d(TAG, "GroupActivity: run: images=" + mImages.size());
            mGroups = new ArrayList<>();
            runOnUiThread(() ->
                    mListView.setAdapter(new GroupAdapter(GroupActivity.this,mGroups)
                    ));
            final List<Group> groups =
                    SimilarImage.find(GroupActivity.this, mImages,
                            mProgressBar);
            int nMultiple = 0;
            List<Group> prunedGroups = new ArrayList<>();
            for (Group group : groups) {
                if (group.getImages().size() > 1) {
                    nMultiple++;
                    prunedGroups.add(group);
                }
            }
            Log.d(TAG, "Found " + nMultiple + " multiple-image groups out" +
                    " of "
                    + groups.size() + " groups");

            String msg = "Found " + mImages.size() + " images in "
                    + prunedGroups.size() + " multiple-image groups";
            mHandler.post(() -> {
                mProgressBar.setVisibility(View.GONE);
//                        Utils.infoMsg(GroupActivity.this, msg);
            });

            mHandler.post(() -> {
                Log.d(TAG, msg);
                // Find the ones that are invalid (no thumbnail)
                mInvalidImages = new ArrayList<>();
                for (Image image : mImages) {
                    if (!image.isValid()) {
                        mInvalidImages.add(image);
                    }
                }
                Log.d(TAG,
                        mInvalidImages.size() + " invalid of " + mImages.size() +
                                " images");
                mGroups = prunedGroups;
                // Find the number of images that in groups
                int nGroupImages = 0;
                for (Group group : mGroups) {
                    nGroupImages += group.getImages().size();
                }
                Log.d(TAG, "Final: images=" + mImages.size() + " groups="
                        + mGroups.size() + " with " + nGroupImages
                        + " group images");
                mListView.setAdapter(new GroupAdapter(GroupActivity.this,mGroups));
            });
        }).start();
    }

    private void refresh() {
        mImages = ImageRepository.getImages(this, mDirectory,
                mUseSubdirectories);
        startFind();
    }

    /**
     * Displays info about the current configuration
     */
    private void info() {
        try {
            StringBuilder info = new StringBuilder();
            if (mGroups == null) {
                info.append("There are no groups").append("\n");
            } else {
                info.append(mImages.size()).
                        append(" images in ").append(mGroups.size())
                        .append(" multiple-image groups").append("\n");
            }
            int nChecked = 0;
            for (Image image : mImages) {
                if (image.isChecked()) nChecked++;
            }
            info.append("Number of images checked: ").append(nChecked);
            Utils.infoMsg(this, info.toString());
        } catch (Throwable t) {
            Utils.excMsg(this, "Error showing info", t);
            Log.e(TAG, "Error showing info", t);
        }
    }

    /**
     * Brings up a viewer, e.g. the Gallery app, to view the image. Will
     * typically give the user a choice of apps that can handle the image.
     *
     * @param image The image.
     */
    private void showViewerForImage(Image image) {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri imageUri = ContentUris.withAppendedId(uri, image.getId());
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(imageUri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (Exception ex) {
            Utils.errMsg(this,
                    "There is no activity available to view this image");
        }
    }

    /**
     * Removes the checked images. Uses MediaStore.createDeleteRequest
     * available in API 30. Works for All Media.
     */
    private void removeCheckedImages() {
        Log.d(TAG, this.getClass().getSimpleName() +
                ".removeCheckedImages:");
        List<Uri> uriList = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        for (Image image : mImages) {
            if (image.isChecked()) {
                uriList.add(ContentUris.withAppendedId(uri, image.getId()));
            }
        }
        if (uriList.size() == 0) {
            Utils.warnMsg(this, "There are no checked images to remove");
            return;
        }

        Log.d(TAG, "removeCheckedImages: Launching PendingIntent"
                + " nImages=" + uriList.size());
        PendingIntent pi =
                MediaStore.createDeleteRequest(this.getContentResolver(),
                        uriList);
        try {
            IntentSenderRequest senderRequest =
                    new IntentSenderRequest.Builder(pi)
                            .setFillInIntent(null)
                            .setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION, 0)
                            .build();
            deleteResultLauncher.launch(senderRequest);
        } catch (Exception ex) {
            Log.d(TAG, "removeCheckedImages: Error launching " +
                    "PendingIntent");
        }
    }

    /**
     * Show invalid images.
     */
    private void showProblemImages() {
        // Create the info message
        StringBuilder info = new StringBuilder();
        if (mInvalidImages.size() == 0) {
            info.append("There are no invalid images");
        } else {
            info.append("\n");
            for (Image image : mInvalidImages) {
                info.append(image.toString()).append("\n");
            }
        }
        TextView textView = new TextView(this);
        textView.setText(info.toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.invalid_images_title);
        builder.setView(textView);
        builder.setPositiveButton(android.R.string.ok,
                (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

        private final Context context;
        private final List<Group> mGroups;

        public GroupAdapter(Context context, List<Group> groups) {
            this.context = context;
            this.mGroups = groups;
        }

        @NonNull
        @Override
        public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_list_group, parent, false);
            return new GroupViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
            Group group = mGroups.get(position);

            // Set group name
            holder.groupName.setText(context.getString(R.string.group_title, position));

            // Set images in nested RecyclerView
            List<Image> images = group.getImages();
            ImageAdapter imageAdapter = new ImageAdapter(context, images);
            holder.imagesRecyclerView.setAdapter(imageAdapter);
        }

        @Override
        public int getItemCount() {
            return mGroups == null ? 0 : mGroups.size();
        }

         class GroupViewHolder extends RecyclerView.ViewHolder {
            TextView groupName;
            RecyclerView imagesRecyclerView;

            public GroupViewHolder(@NonNull View itemView) {
                super(itemView);
                groupName = itemView.findViewById(R.id.group_name);
                imagesRecyclerView = itemView.findViewById(R.id.imagesRecyclerView);

                // Set GridLayoutManager for nested RecyclerView
                imagesRecyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), 3));
            }
        }
    }
    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

        private final Context context;
        private final List<Image> images;

        public ImageAdapter(Context context, List<Image> images) {
            this.context = context;
            this.images = images;
        }

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.image_info_item, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            Image image = images.get(position);

            // Load image using Glide
            Glide.with(context)
                    .load(image.getPath())
                    .centerCrop()
                    .placeholder(R.drawable.ic_album)
                    .into(holder.image);

            // Set text and listeners
            holder.imageName.setText(image.getName());
            holder.checkBox.setChecked(image.isChecked());

            holder.image.setOnClickListener(v -> {
                // Handle image click
                Utils.infoMsg(context, image.toString());
            });

            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                image.setChecked(isChecked);
            });
        }

        @Override
        public int getItemCount() {
            return images == null ? 0 : images.size();
        }

         class ImageViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView imageName;
            CheckBox checkBox;

            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.image);
                imageName = itemView.findViewById(R.id.image_name);
                checkBox = itemView.findViewById(R.id.checkBox);
            }
        }
    }

}
