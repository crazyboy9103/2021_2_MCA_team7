package com.mobilecompute.test.stepcounter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.mobilecompute.test.stepcounter.R;

public class MainActivity extends AppCompatActivity {
    private static final int ACTIVITY_RECOGNITION_REQUEST_CODE = 34;
    StepCounterListener listener;
    CadenceLPFEstimator cadenceLPFEstimator;
    Stopwatch stopwatch;
    VibrationFeedback vibrator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initSensors();

    }

    private void initViews() {}

    private void initSensors(){
        cadenceLPFEstimator = new CadenceLPFEstimator(0.5F);
        listener = new StepCounterListener(this, cadenceLPFEstimator);
        vibrator = new VibrationFeedback(this);
        stopwatch = new Stopwatch(this, listener, cadenceLPFEstimator, vibrator);
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
                new String[]{Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.VIBRATE},
                ACTIVITY_RECOGNITION_REQUEST_CODE);
    }


}


