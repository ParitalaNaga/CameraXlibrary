package com.jukshio.cameraxlibrary;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class PreviewActicity extends View {

    AutoFitTextureView textureView;
    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    PreviewConfig pConfig;
    Preview preview;
    Bitmap bitmap;
    OutputStream outputStream;
    ImageCaptureConfig imageCapture;
    ImageCapture imgCap;
    Uri imageuri;
    ContentResolver resolver;
    ContentValues contentValues;
    private CameraManager cameraManager;
    private String getCameraID;
    private boolean flashLight = true;
    Context context;

    public PreviewActicity(Context context) {
        super(context);
        this.context = context;
        startCamera();

    }

    public PreviewActicity(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PreviewActicity(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PreviewActicity(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

/*
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textureView = new AutoFitTextureView(this);
        setContentView(textureView);

        if (allPermissionsGranted()) {
            startCamera(); //start camera if permission has been granted by user

        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
        capturePhotos();
        //flashLightOn();
    }
*/

    public void startCamera() {

        CameraX.unbindAll();

        Rational aspectRatio = new Rational(textureView.getWidth(), textureView.getHeight());
        Size screen = new Size(textureView.getWidth(), textureView.getHeight()); //size of the screen


        pConfig = new PreviewConfig.Builder().setTargetAspectRatio(aspectRatio).setTargetResolution(screen).build();
        preview = new Preview(pConfig);

        preview.setOnPreviewOutputUpdateListener(
                new Preview.OnPreviewOutputUpdateListener() {
                    //to update the surface texture we  have to destroy it first then re-add it
                    @Override
                    public void onUpdated(Preview.PreviewOutput output) {
                        ViewGroup parent = (ViewGroup) textureView.getParent();
                        parent.removeView(textureView);
                        parent.addView(textureView, 0);

                        textureView.setSurfaceTexture(output.getSurfaceTexture());
                        updateTransform();
                    }
                });
/*

        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();
        final ImageCapture imgCap = new ImageCapture(imageCaptureConfig);

        File file = new File(Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".png");
        imgCap.takePicture(file, new ImageCapture.OnImageSavedListener() {
            @Override
            public void onImageSaved(@NonNull File file) {
                String msg = "Pic captured at " + file.getAbsolutePath();
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
                String msg = "Pic capture failed : " + message;
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                if (cause != null) {
                    cause.printStackTrace();
                }
            }
        });*/

    }

    public void capturePhotos() {


/*
        File direct = new File(Environment.getExternalStorageDirectory() + "/DirName");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/DirName/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File("/sdcard/DirName/", System.currentTimeMillis() + ".jpg");
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            Bitmap original = BitmapFactory.decodeStream(getAssets().open("500x250.jpg"));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            original.compress(Bitmap.CompressFormat.PNG, 100, out);

            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
/*
        try {
            imageCapture = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MAX_QUALITY)
                    .setTargetRotation(context.getWindowManager().getDefaultDisplay().getRotation()).build();
            imgCap = new ImageCapture(imageCapture);
            resolver = context.getContentResolver();
            contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_" + ".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "CameraXFolder");
            imageuri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            outputStream = (FileOutputStream) resolver.openOutputStream(Objects.requireNonNull(imageuri));
            File file = new File(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "CameraXFolder");
            Objects.requireNonNull(outputStream);
            imgCap.takePicture(file, new ImageCapture.OnImageSavedListener() {
                @Override
                public void onImageSaved(@NonNull File file) {
                    Toast.makeText(context, "Photo Saved" + file, Toast.LENGTH_SHORT).show();
                    Log.e("TAG", "onImageSaved: " + file);
                }

                @Override
                public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {

                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }
            });

            CameraX.bindToLifecycle((LifecycleOwner) this, preview, imgCap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void flashLightOn() {
        cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            // O means back camera unit,
            // 1 means front camera unit
            getCameraID = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        if (flashLight) {
            try {
                // true sets the torch in ON mode
                cameraManager.setTorchMode(getCameraID, true);


                // Inform the user about the flashlight
                // status using Toast message
                Toast.makeText(context, "Flashlight is turned ON", Toast.LENGTH_SHORT).show();
            } catch (CameraAccessException e) {
                // prints stack trace on standard error
                // output error stream
                e.printStackTrace();
            }
        }

    }


    public void updateTransform() {
        Matrix mx = new Matrix();
        float w = textureView.getMeasuredWidth();
        float h = textureView.getMeasuredHeight();

        float cX = w / 2f;
        float cY = h / 2f;

        int rotationDgr;
        int rotation = (int) textureView.getRotation();

        switch (rotation) {
            case Surface.ROTATION_0:
                rotationDgr = 0;
                break;
            case Surface.ROTATION_90:
                rotationDgr = 90;
                break;
            case Surface.ROTATION_180:
                rotationDgr = 180;
                break;
            case Surface.ROTATION_270:
                rotationDgr = 270;
                break;
            default:
                return;
        }

        mx.postRotate((float) rotationDgr, cX, cY);
        textureView.setTransform(mx);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(context, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();

            }
        }
    }

    public boolean allPermissionsGranted() {

        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
