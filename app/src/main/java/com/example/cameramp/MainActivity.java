package com.example.cameramp;

/*
Use existing camera application
 */

///////////////////////////////////////////////////////////////////////////
// Imports
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

///////////////////////////////////////////////////////////////////////////
// MainActivity
public class MainActivity extends AppCompatActivity {
    // ----- CLASS VARIABLES UI ----- //
    //Button takePictureButton;
    //ImageView imageView;
    TextureView textureView;

    // ----- CLASS VARIABLES NON-UI ----- //
    String cameraID;


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
        textureView.setSurfaceTextureListener(surfaceTextureListener);
    }

    TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            // open camera
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

            try {
                cameraID = cameraManager.getCameraIdList()[0];

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
