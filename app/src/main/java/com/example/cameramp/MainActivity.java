package com.example.cameramp;

/*
Use existing camera application
 */

///////////////////////////////////////////////////////////////////////////
// IMPORTs
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

///////////////////////////////////////////////////////////////////////////
// MainActivity
public class MainActivity extends AppCompatActivity {

    Button takePictureButton;

    // onCreate()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // inflate UI

        // initialize UI widgets to programmatically control later
        takePictureButton = (Button) findViewById(R.id.takePictureButton);

        // button listener
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                System.out.println("button pressed");
            }
        });
    }
} // end class
