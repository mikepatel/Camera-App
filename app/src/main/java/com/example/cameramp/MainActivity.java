package com.example.cameramp;

/*
Use existing camera application
 */

///////////////////////////////////////////////////////////////////////////
// Imports
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

///////////////////////////////////////////////////////////////////////////
// MainActivity
public class MainActivity extends AppCompatActivity {

    // class variable UI elements
    Button takePictureButton;
    ImageView imageView;

    // request codes
    final int IMAGE_CAPTURE_REQUEST_CODE = 1;

    // onCreate()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // inflate UI

        // initialize UI widgets to programmatically control later
        takePictureButton = findViewById(R.id.takePictureButton);
        imageView = findViewById(R.id.imageView);

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
    }

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

} // end class
