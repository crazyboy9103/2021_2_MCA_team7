package com.mobilecompute.test.stepcounter;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity  {

    private SensorManager sensorManager;
    private Sensor stepCountSensor;
    TextView tvStepCount;

    ToggleButton vibFeedbackOnOffButton;

    StepCounterListener listener;
    Vibrator vibrator;
    VibrationFeedback vibrationFeedback;
    Thread feedbackThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initSensor();
    }
    private void initViews() {
        tvStepCount = (TextView)findViewById(R.id.tvStepRate);

        vibFeedbackOnOffButton = findViewById(R.id.vibFeedOnOffButton);
        vibFeedbackOnOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vibFeedbackOnOffButton.isChecked()){
                    feedbackThread.interrupt();
                    feedbackThread = null;
                } else{
                    feedbackThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()){
                                vibrationFeedback.feedback(listener.rate);
                                try{
                                    Thread.sleep(2000);
                                } catch (InterruptedException e){
                                    e.printStackTrace();
                                    Thread.currentThread().interrupt();
                                    vibrationFeedback.cancel();
                                }
                            }
                        }
                    });
                    feedbackThread.start();
                }
            }
        });
    }
    private void initSensor() {
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(stepCountSensor == null) {
            Toast.makeText(this, "No Step Detect Sensor", Toast.LENGTH_SHORT).show();
        }

        listener = new StepCounterListener(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrationFeedback = new VibrationFeedback(vibrator);
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
