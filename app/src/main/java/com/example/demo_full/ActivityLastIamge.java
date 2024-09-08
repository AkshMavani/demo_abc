package com.example.demo_full;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.demo_full.databinding.ActivityLastIamgeBinding;

public class ActivityLastIamge extends AppCompatActivity {
    ActivityLastIamgeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLastIamgeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

      String str= GalleryHelper.INSTANCE.getLastItemFromCameraFolder(this);
        Log.e("TAG_STR", "onCreate: "+str);

        Glide.with(this)
                .load(str.toString())
                .into(binding.imageView10);
    }
}

