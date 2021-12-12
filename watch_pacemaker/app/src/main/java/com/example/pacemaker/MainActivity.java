package com.example.pacemaker;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import com.example.pacemaker.databinding.ActivityMainBinding;

public class MainActivity extends Activity {
    private static final int ACTIVITY_RECOGNITION_REQUEST_CODE = 34;

    private ActivityMainBinding binding;
    CadenceEstimator cadenceEstimator;
    CadenceListener cadenceListener;
    PaceEstimator paceEstimator;
    VibrationFeedback vibrator;
    VoiceFeedback voiceFeedback;

    Stopwatch stopwatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initSensors();
    }

    protected void initSensors() {
        cadenceEstimator = new CadenceEstimator();
        cadenceListener = new CadenceListener(this, binding, cadenceEstimator);
        voiceFeedback = new VoiceFeedback(this, 2.0, 12.0);
        paceEstimator = new PaceEstimator(this, binding);

        vibrator = new VibrationFeedback(this);
        stopwatch = new Stopwatch(this, binding, cadenceListener, paceEstimator, voiceFeedback);
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
                new String[]{Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.VIBRATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                ACTIVITY_RECOGNITION_REQUEST_CODE);
    }
}