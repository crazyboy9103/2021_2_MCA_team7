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
    CadenceKFEstimator cadenceKFEstimator;
    PaceEstimator paceEstimator;
    Stopwatch stopwatch;
    Stopwatch2 stopwatch2;
    VibrationFeedback vibrator;
    VoiceFeedback voice;


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
        cadenceKFEstimator = new CadenceKFEstimator();
        paceEstimator = new PaceEstimator(this);
        listener = new StepCounterListener(this, cadenceLPFEstimator, cadenceKFEstimator);
        vibrator = new VibrationFeedback(this);
        voice = new VoiceFeedback(this);
        stopwatch = new Stopwatch(this, listener, cadenceLPFEstimator, cadenceKFEstimator, vibrator);
        stopwatch2 = new Stopwatch2(this, paceEstimator, voice);

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!checkPermissions()) {
            requestPermissions();
        }

    }

    // YSKIM: checkPermissions 는 "ACTIVITY_RECOGNITION" 에 대해서만 하고, 권한 부여는 진동이나 위치에 대한 거를 한꺼번에 다하는듯??
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.VIBRATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                ACTIVITY_RECOGNITION_REQUEST_CODE);
    }


}


