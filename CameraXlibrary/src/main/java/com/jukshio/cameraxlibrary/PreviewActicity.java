package com.jukshio.cameraxlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.util.AttributeSet;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class PreviewActicity extends View {

    Context mContext;
    PreviewView previewView;
    private final int REQUEST_CODE = 1001;

    Button button;
    Bitmap bitmap;
    static Context context;
    TextView textView;
    static int width1, height1;
    File file;
    ImageCapture imageCapture;
    ImageCapture.OutputFileOptions outputFileOptions;
    private final String[] Requires_Permission = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private CameraManager cameraManager;
    private String getCameraID;
    private boolean flashLight = true;
    CameraSelector cameraSelector;
    ImageAnalysis imageAnalysis;
    Preview preview;
    ProcessCameraProvider cameraProvider;

    public PreviewActicity(Context context) {
        super(context);


    }

    public PreviewActicity(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public PreviewActicity(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void init(Context context, AttributeSet attributeSet, int defStyleAttr) {
        mContext = context;
        Toast.makeText(context, "Context", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Toast.makeText(mContext, "hi", Toast.LENGTH_SHORT).show();
        if (allpermissionGranted()) {
            getPreviewView();
        } else {
            ActivityCompat.requestPermissions((Activity) mContext, Requires_Permission, REQUEST_CODE);
        }


    }


    public PreviewView getPreviewView() {
        ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture = ProcessCameraProvider.getInstance(mContext);
        cameraProviderListenableFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    cameraProvider = cameraProviderListenableFuture.get();

                    preview = new Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3).build();
                    cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
                    imageAnalysis = new ImageAnalysis.Builder().build();
                    imageCapture = new ImageCapture.Builder().build();
                    preview.setSurfaceProvider(previewView.getSurfaceProvider());
                    cameraProvider.bindToLifecycle((LifecycleOwner) mContext, cameraSelector, imageAnalysis, imageCapture, preview);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(mContext));

        return previewView;
    }


    public boolean allpermissionGranted() {
        for (String permission : Requires_Permission) {
            if (ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(mContext, "Permission Granted", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(mContext, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void bindlifecycle(ProcessCameraProvider cameraProvider) {
        cameraProvider.bindToLifecycle((LifecycleOwner) mContext, cameraSelector, imageAnalysis, imageCapture, preview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //  imagecaptureofcamerax();
        }
    }

/*
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void imagecaptureofcamerax() {

        imageCapture.takePicture(outputFileOptions, mContext.getMainExecutor(), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Toast.makeText(mContext, "Photo Saved", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {

                Toast.makeText(mContext, "Error" + exception.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
*/

}
