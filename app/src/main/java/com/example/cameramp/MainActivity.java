package com.example.cameramp;

/*
Use existing camera application
 */

///////////////////////////////////////////////////////////////////////////
// Imports
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.util.Arrays;

///////////////////////////////////////////////////////////////////////////
// MainActivity
public class MainActivity extends AppCompatActivity {
    // ----- CLASS VARIABLES UI ----- //
    //Button takePictureButton;
    //ImageView imageView;
    TextureView textureView;

    // ----- CLASS VARIABLES NON-UI ----- //
    CameraCaptureSession cameraCaptureSessions;
    CameraDevice cameraDevice;
    String cameraID;
    CaptureRequest captureRequest;
    CaptureRequest.Builder captureRequestBuilder;
    Size imageDimension;
    Handler mBackgroundHandler;
    HandlerThread mBackgroundThread;

    // request codes
    //final int IMAGE_CAPTURE_REQUEST_CODE = 1;

    // ----- onCreate() ----- //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // inflate UI

        // initialize UI widgets to programmatically control later
        //takePictureButton = findViewById(R.id.takePictureButton);
        //imageView = findViewById(R.id.imageView);

        /*
        // button listener
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(cameraIntent.resolveActivity(getPackageManager()) != null){  // prevent app from crashing
                    startActivityForResult(cameraIntent, IMAGE_CAPTURE_REQUEST_CODE);
                }
            }
        });
         */

        /*
        // have camera open automatically using default camera app
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getPackageManager()) != null){  // prevent app from crashing
            startActivityForResult(cameraIntent, IMAGE_CAPTURE_REQUEST_CODE);
        }
         */

        // create my own custom camera preview using SurfaceView
        textureView = findViewById(R.id.textureView);
        assert textureView != null;
        textureView.setSurfaceTextureListener(surfaceTextureListener);
    }

    // ----- TEXTUREVIEW ----- //
    TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            // open camera
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

            try {
                cameraID = cameraManager.getCameraIdList()[0];
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraID);
                StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                assert map != null;

                imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

                // add permission
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 200);
                    return;
                }

                cameraManager.openCamera(cameraID, stateCallback, null);

            } catch (CameraAccessException cae) {
                cae.printStackTrace();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            // transform image
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            //
        }
    };

    // ----- STATE CALLBACK ----- //
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            // when camera is open
            cameraDevice = camera;

            try {
                SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
                assert surfaceTexture != null;
                surfaceTexture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());

                Surface surface = new Surface(surfaceTexture);

                captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                captureRequestBuilder.addTarget(surface);
                cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback(){
                    @Override
                    public void onConfigured (@NonNull CameraCaptureSession cameraCaptureSession){
                        // camera already closed
                        if (cameraDevice == null){
                            return;
                        }

                        // when session ready, display preview
                        cameraCaptureSessions = cameraCaptureSession;

                        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                        try {
                            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
                        } catch (CameraAccessException cae) {
                            cae.printStackTrace();
                        }
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                        // toast message
                    }
                }, null);
            } catch (CameraAccessException cae) {
                cae.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    /*
    // onActivityResult() --> receive resultCode
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // check which request code
        if(requestCode == IMAGE_CAPTURE_REQUEST_CODE){
            // check if result was successful
            if(resultCode == RESULT_OK){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");  // thumbnail image
                imageView.setImageBitmap(imageBitmap);
                imageView.setRotation(90);  // use only in portrait mode
            }
        }
    }
     */

} // end class
