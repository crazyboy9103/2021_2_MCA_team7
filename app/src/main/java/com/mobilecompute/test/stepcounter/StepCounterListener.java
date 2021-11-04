package com.mobilecompute.test.stepcounter;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class StepCounterListener implements SensorEventListener {
    Context context;
    TextView tvStepRate, textDetectorExists;
    public int step_count = 0;

    CadenceLPFEstimator cadenceLPFEstimator;

    private SensorManager sensorManager;
    private Sensor mStepDetector;

    public StepCounterListener(Context context, CadenceLPFEstimator cadenceLPFEstimator){
        this.context=context;
        this.cadenceLPFEstimator = cadenceLPFEstimator;

        tvStepRate = (TextView) ((AppCompatActivity)context).findViewById(R.id.tvStepRate);
        textDetectorExists = (TextView) ((AppCompatActivity)context).findViewById(R.id.tvDetectorExists);

        initSensors();
    }

    private void initSensors(){
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
            mStepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            textDetectorExists.setText("Sensor Detected");
        } else {
            Toast.makeText(context, "No step detector", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            step_count++;
            cadenceLPFEstimator.update(SystemClock.elapsedRealtime());
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
        cadenceLPFEstimator.reset();
    }
}