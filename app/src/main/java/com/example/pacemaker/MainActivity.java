package com.example.pacemaker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int ACTIVITY_RECOGNITION_REQUEST_CODE = 34;
    private TextView textViewStepRate;
    StepCounterListener listener;
    Stopwatch stopwatch;
    Thread cadenceCalculator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initSensors();

        /*

        cadenceCalculator = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(6000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textViewStepRate.setText("Current cadence: " + (listener.step_count * 10));
                                listener.step_count = 0;
                            }
                        });
                    }
                } catch (InterruptedException e) {}
            }
        };
        */

    }

    private void initViews() {
        textViewStepRate = findViewById(R.id.tvStepRate);
    }

    private void initSensors(){
        listener = new StepCounterListener(this);
        stopwatch = new Stopwatch(this, listener);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!checkPermissions()) {
            requestPermissions();
        }

    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                ACTIVITY_RECOGNITION_REQUEST_CODE);
    }


}