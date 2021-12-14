package com.example.watch_pacemaker;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import com.example.watch_pacemaker.databinding.ActivityMainBinding;

public class MainActivity extends Activity {
    private static final int ACTIVITY_RECOGNITION_REQUEST_CODE = 34;

    private ActivityMainBinding binding;
    CadenceEstimator cadenceEstimator;
    CadenceListener cadenceListener;
    HeartrateListener heartrateListener;
    PaceEstimator paceEstimator;
    VibrationFeedback vibrator;
    VoiceFeedback voiceFeedback;
    MediaPlayer increasePace, decreasePace, collectingData;

    Stopwatch stopwatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initSensors();
    }

    protected void initSensors() {
        // Initializing Sensors
        cadenceEstimator = new CadenceEstimator();
        cadenceListener = new CadenceListener(this, binding, cadenceEstimator);
        heartrateListener = new HeartrateListener(this, binding);

        // Initializing Media Data
        increasePace = MediaPlayer.create(this, R.raw.increase);
        decreasePace = MediaPlayer.create(this, R.raw.decrease);
        collectingData = MediaPlayer.create(this, R.raw.wait);

        voiceFeedback = new VoiceFeedback(this, increasePace, decreasePace, collectingData,
                                            2.0, 12.0);
        // Initializing Calculators and UI
        paceEstimator = new PaceEstimator(this, binding);
        vibrator = new VibrationFeedback(this);
        stopwatch = new Stopwatch(this, binding, cadenceListener, heartrateListener, paceEstimator, voiceFeedback, vibrator);
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
                new String[]{Manifest.permission.ACTIVITY_RECOGNITION, Manifest.permission.VIBRATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.BODY_SENSORS},
                ACTIVITY_RECOGNITION_REQUEST_CODE);
    }
}