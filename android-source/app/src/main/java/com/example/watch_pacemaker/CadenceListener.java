package com.example.watch_pacemaker;


import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.watch_pacemaker.databinding.ActivityMainBinding;

public class CadenceListener implements SensorEventListener {
    Context context;
    TextView tvStepRate, textDetectorExists;
    ActivityMainBinding binding;

    public int stepCount = 0;

    public CadenceEstimator cadenceEstimator;

    private SensorManager sensorManager;
    private Sensor mStepDetector;

    public CadenceListener(Context context, ActivityMainBinding binding, CadenceEstimator cadenceKFEstimator){
        this.context=context;
        this.cadenceEstimator = cadenceKFEstimator;
        this.binding = binding;

        tvStepRate = (TextView) binding.tvTitle;
        textDetectorExists = (TextView) binding.tvDetectorExists;

        initSensors();
    }

    private void initSensors(){
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
            mStepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            textDetectorExists.setText("Sensors Detected");
            Log.i("Steprate Listener", "Found");
        } else {
            textDetectorExists.setText("Sensors not detected");
            Log.i("Steprate Listener", "Not found");
        }

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            stepCount++;
            cadenceEstimator.update(SystemClock.elapsedRealtime());
            Log.i("StepListener", "stepcount: "+stepCount);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void pauseSensor() {
        sensorManager.unregisterListener(this);
    }

    public void startSensor() {
        sensorManager.registerListener(this, mStepDetector, SensorManager.SENSOR_DELAY_NORMAL);
        cadenceEstimator.reset();
    }
}