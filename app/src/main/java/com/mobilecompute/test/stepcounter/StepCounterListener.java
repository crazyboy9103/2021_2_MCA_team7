package com.mobilecompute.test.stepcounter;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class StepCounterListener implements SensorEventListener {
    final static String TAG = StepCounterListener.class.getName();
    Context context;
    TextView tvStepRate;
    int init_count = 0;
    long init_time = 0;
    long elapsed = 0;
    float rate = 0;
    Vibrator vibrator;
    boolean init_checked = false;
    public StepCounterListener(Context context){
        this.context=context;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        tvStepRate = (TextView) ((AppCompatActivity)context).findViewById(R.id.tvStepRate);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (!init_checked) {
                if((int)sensorEvent.values[0] != 0){
                    init_count =(int)sensorEvent.values[0];
                }
                init_time = System.currentTimeMillis();
                init_checked = true;
            }

            elapsed = (System.currentTimeMillis()-init_time);

            rate = ((sensorEvent.values[0] - init_count)/elapsed) * 1000 * 60;


//            if(rate > 120) {
//                vibrator.vibrate(VibrationEffect.createOneShot(500,
//                        VibrationEffect.DEFAULT_AMPLITUDE));
//
//                showToast(String.format("Slow down, %s steps per minute", rate));
//            }
            tvStepRate.setText(rate+"");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void showToast(String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

    }
}
