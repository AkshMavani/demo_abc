package com.example.abc;

package com.example.twitterlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.twitterlogin.databinding.ActivityMain3Binding;
import com.otaliastudios.cameraview.CameraException;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraOptions;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.VideoResult;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.Mode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity3 extends AppCompatActivity {
    private ActivityMain3Binding activityMain3Binding;
    boolean flag;

    float value;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GestureDetector gestureDetector;
        activityMain3Binding=ActivityMain3Binding.inflate(getLayoutInflater());
        setContentView(activityMain3Binding.getRoot());
        activityMain3Binding.camera.addCameraListener(new Listener());
        activityMain3Binding.camera.setLifecycleOwner(this);
//        ViewGroup.LayoutParams params = activityMain3Binding.camera.getLayoutParams();
//        params.width = 100;
//        params.height = 100;
//        activityMain3Binding.camera.setLayoutParams(params);

        activityMain3Binding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display Toast when CameraView is clicked
                Toast.makeText(MainActivity3.this, "CameraView clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        // Initialize GestureDetector
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                // Display Toast when CameraView is clicked
                Toast.makeText(MainActivity3.this, "CameraView clicked!", Toast.LENGTH_SHORT).show();
                return super.onSingleTapConfirmed(e);
            }
        });
        activityMain3Binding.camera.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        activityMain3Binding.capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capturePicture();
            }
        });
        activityMain3Binding.video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // activityMain3Binding.camera.setMode(Mode.VIDEO);
                captureVideo();
            }
        });
        activityMain3Binding.front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=!flag;
                if (flag){
                    activityMain3Binding.camera.setFacing(Facing.FRONT);

                }else{
                    activityMain3Binding.camera.setFacing(Facing.BACK);
                }
            }
        });

        activityMain3Binding.stopvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityMain3Binding.camera.stopVideo();
            }
        });
        activityMain3Binding.seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                value=getConvertedValue(progress);



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                activityMain3Binding.camera.setZoom(value);
            }
        });
    }
    private class Listener extends CameraListener {
        @Override
        public void onCameraOpened(CameraOptions options) {

        }

        @Override
        public void onCameraError(CameraException exception) {
            super.onCameraError(exception);
            // message("Got CameraException #" + exception.getReason(), true);
        }

        @Override
        public void onPictureTaken(PictureResult result) {
            super.onPictureTaken(result);
            Log.e("TAG_CAMERA", "onPictureTaken: "+result );
            // Bitmap bitmap = BitmapFactory.decodeByteArray(result.getData(), 0, result.getData().length);

            result.toBitmap(result.getSize().getWidth(), result.getSize().getHeight(), bitmap -> {
                Bitmap croppedBitmap = cropBitmapToSquare(bitmap);
                Bitmap resizedBitmap = resizeBitmap(croppedBitmap, 1080, 1080);

                activityMain3Binding.camera.setVisibility(View.GONE);
                activityMain3Binding.img.setVisibility(View.VISIBLE);
                activityMain3Binding.img.setImageBitmap(resizedBitmap);
                saveBitmapToGallery(resizedBitmap);
            });

            // Get the orientation of the device
//            int rotation = getWindowManager().getDefaultDisplay().getRotation();
//
//            // Rotate the bitmap based on the device orientation
//            switch (rotation) {
//                case Surface.ROTATION_0:
//                    // No rotation needed
//                    break;
//                case Surface.ROTATION_90:
//                    bitmap = rotateBitmap(bitmap, 90);
//                    break;
//                case Surface.ROTATION_180:
//                    bitmap = rotateBitmap(bitmap, 180);
//                    break;
//                case Surface.ROTATION_270:
//                    bitmap = rotateBitmap(bitmap, 270);
//                    break;
//            }
//
//            // Crop the bitmap to the desired aspect ratio
//            Bitmap croppedBitmap = cropToAspectRatio(bitmap, 16f / 9f);
//
//            activityMain3Binding.camera.setVisibility(View.GONE);
//            activityMain3Binding.img.setVisibility(View.VISIBLE);
//            activityMain3Binding.img.setImageBitmap(croppedBitmap);

//            Bitmap croppedBitmap = cropToAspectRatio(bitmap, 3 / 4f);
//            activityMain3Binding.camera.setVisibility(View.GONE);
//            activityMain3Binding.img.setVisibility(View.VISIBLE);
//            activityMain3Binding.img.setImageBitmap(croppedBitmap);


//            activityMain3Binding.camera.setVisibility(View.GONE);
//            activityMain3Binding.img.setVisibility(View.VISIBLE);
//            activityMain3Binding.img.setImageBitmap(bitmap);
        }

        @Override
        public void onVideoTaken(VideoResult result) {
            super.onVideoTaken(result);
            Log.e("TAG_CAMERA", "videoTaken: "+result );
        }

        @Override
        public void onVideoRecordingStart() {
            super.onVideoRecordingStart();

        }

        @Override
        public void onVideoRecordingEnd() {
            super.onVideoRecordingEnd();

        }

        @Override
        public void onExposureCorrectionChanged(float newValue, float[] bounds, PointF[] fingers) {
            super.onExposureCorrectionChanged(newValue, bounds, fingers);

        }

        @Override
        public void onZoomChanged(float newValue, float[] bounds, PointF[] fingers) {
            super.onZoomChanged(newValue, bounds, fingers);;
            Log.e("newVALUE", "onZoomChanged: "+newValue);
        }


    }
    private void captureVideo() {
        activityMain3Binding.camera.setMode(Mode.VIDEO);
        if (activityMain3Binding.camera.getMode() == Mode.PICTURE) {
            Toast.makeText(MainActivity3.this,"Can't record HQ videos while in PICTURE mode",Toast.LENGTH_SHORT).show();
            // message("Can't record HQ videos while in PICTURE mode.", false);
            return;
        }
        if ( activityMain3Binding.camera.isTakingPicture() || activityMain3Binding.camera.isTakingVideo()) {
            return;
        }
        // message("Recording for 5 seconds...", true);
        activityMain3Binding.camera.takeVideo(new File(getFilesDir(), "video.mp4"), 5000);
    }
    private void capturePicture() {
        activityMain3Binding.camera.setMode(Mode.PICTURE);
        if (activityMain3Binding.camera.getMode() == Mode.VIDEO) {
            Toast.makeText(MainActivity3.this,"Can't take HQ pictures while in VIDEO mode",Toast.LENGTH_SHORT).show();
            // message("Can't take HQ pictures while in VIDEO mode.", false);
            return;
        }
        if (activityMain3Binding.camera.isTakingPicture()) {
            return;
        }

        activityMain3Binding.camera.takePicture();
    }
    public Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public Bitmap cropToAspectRatio(Bitmap bitmap, float aspectRatio) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Calculate the target width and height based on the aspect ratio
        int targetWidth;
        int targetHeight;

        if ((float) width / height > aspectRatio) {
            targetHeight = height;
            targetWidth = (int) (height * aspectRatio);
        } else {
            targetWidth = width;
            targetHeight = (int) (width / aspectRatio);
        }

        // Calculate the starting points for cropping
        int xOffset = (width - targetWidth) / 2;
        int yOffset = (height - targetHeight) / 2;

        // Crop the bitmap to the desired aspect ratio
        return Bitmap.createBitmap(bitmap, xOffset, yOffset, targetWidth, targetHeight);
    }

    private Bitmap cropBitmapToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newDim = Math.min(width, height);  // Get the smallest dimension
        return Bitmap.createBitmap(bitmap, (width - newDim) / 2, (height - newDim) / 2, newDim, newDim);
    }

    // Resize the bitmap to 1080x1080
    private Bitmap resizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    public void saveBitmapToGallery(Bitmap bitmap) {
        ContentResolver resolver = getContentResolver();
        ContentValues contentValues = new ContentValues();

        // Set up metadata for the image
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "image_" + System.currentTimeMillis() + ".jpg");
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

        // Set up the folder where the image should be saved
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android 10 and above, save to the Pictures/abcd folder
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/abcd");
        } else {
            // For older versions, save to Pictures/abcd folder on the file system
            String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/abcd";
            contentValues.put(MediaStore.MediaColumns.DATA, imagesDir + "/image_" + System.currentTimeMillis() + ".jpg");
        }

        // Insert the content values into the MediaStore
        OutputStream imageOutStream = null;
        try {
            // Get the URI where the image will be saved
            android.net.Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            if (uri != null) {
                // Open an output stream and write the bitmap
                imageOutStream = resolver.openOutputStream(uri);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageOutStream);
                Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }  finally {
            if (imageOutStream != null) {
                try {
                    imageOutStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}