package com.example.cameramp;

/*
Use existing camera application
 */

///////////////////////////////////////////////////////////////////////////
// IMPORTs
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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
    }
} // end class
