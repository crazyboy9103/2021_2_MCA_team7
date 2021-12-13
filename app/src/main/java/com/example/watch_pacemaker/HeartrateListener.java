package com.example.watch_pacemaker;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.watch_pacemaker.databinding.ActivityMainBinding;

public class HeartrateListener implements SensorEventListener{
    Context context;
    ActivityMainBinding binding;
    SensorManager sensorManager;
    Sensor mHeartSensor;

    private double heartrate;

    public HeartrateListener(Context context, ActivityMainBinding binding) {
        this.context = context;
        this.binding = binding;

        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE) != null) {
            Log.i("Heartrate Sensor", "Found");
            mHeartSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        } else {
            Log.i("Heartrate Sensor", "Not found");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            heartrate = event.values[0];
            Log.i("Heartrate Sensor", "heartrate: " + (int)heartrate);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void startSensor() {
        sensorManager.registerListener(this, mHeartSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
