package com.mobilecompute.test.stepcounter;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity  {

    private SensorManager sensorManager;
    private Sensor stepCountSensor;
    TextView tvStepCount;

    StepCounterListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initSensor();


    }
    private void initViews() { tvStepCount = (TextView)findViewById(R.id.tvStepRate); }
    private void initSensor() {
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(stepCountSensor == null) {
            Toast.makeText(this, "No Step Detect Sensor", Toast.LENGTH_SHORT).show();
        }

        listener = new StepCounterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(listener,
                stepCountSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(listener);
    }

    public void onToggleClicked(View v){
        if(!((ToggleButton) v).isChecked()) {
            listener = new StepCounterListener(this);
        } else {
            sensorManager.unregisterListener(listener);
        }
    }

}
