package com.example.abc;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CameraCharacteristics;
import android.view.Surface;
import android.util.Log;

public class CameraFlashController {

    private static final String TAG = "CameraFlashController";
    private CameraDevice cameraDevice;
    private CameraCaptureSession captureSession;
    private CaptureRequest.Builder previewRequestBuilder;
    private CameraManager cameraManager;
    private String cameraId;

    public CameraFlashController(Context context, CameraDevice cameraDevice, CameraCaptureSession captureSession, CaptureRequest.Builder previewRequestBuilder) {
        this.cameraDevice = cameraDevice;
        this.captureSession = captureSession;
        this.previewRequestBuilder = previewRequestBuilder;
        this.cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        try {
            cameraId = cameraManager.getCameraIdList()[0];  // Back camera ID (typically 0)
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // Enable flash mode
    public void turnOnFlash() {
        try {
            if (isFlashSupported()) {
                previewRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                captureSession.setRepeatingRequest(previewRequestBuilder.build(), null, null);
                Log.d(TAG, "Flashlight turned ON");
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // Disable flash mode
    public void turnOffFlash() {
        try {
            previewRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
            captureSession.setRepeatingRequest(previewRequestBuilder.build(), null, null);
            Log.d(TAG, "Flashlight turned OFF");
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // Check if flash is supported
    private boolean isFlashSupported() {
        try {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            return characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
