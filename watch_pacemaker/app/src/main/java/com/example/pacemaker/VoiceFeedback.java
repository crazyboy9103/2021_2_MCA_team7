package com.example.pacemaker;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class VoiceFeedback {
    Context context;

    final String goWideStep = "Increase your step size!";
    final String goNarrowStep = "Decrease your step size!";
    final String wait_pace = "Wait until collecting 10 10 samples";
    final int repeat = -1;

    Button target_distance_button, target_time_button;

    private double TARGET_DISTANCE=10;
    private double TARGET_TIME=60;
    private double TARGET_PACE;

    private TextView target_pace;

    public VoiceFeedback(Context context, Double target_distance, Double target_time){
        this.context = context;


        TARGET_DISTANCE = target_distance; // 2 kilometers
        TARGET_TIME = target_time; // 1200seconds = 20 minutes

        TARGET_PACE = (TARGET_DISTANCE * 1000) / (TARGET_TIME * 60); // 단위: m/sec

    }

    public void feedback(double pace) {
        if (pace == -1){
            play(wait_pace);
        }
        else{
            if (pace >= TARGET_PACE * 1.15) {
                play(goNarrowStep);
            } else if (pace <= TARGET_PACE * 0.85) {
                play(goWideStep);
            }
        }
    }

    public void play(String voice){
        Toast.makeText(context.getApplicationContext(), voice, Toast.LENGTH_SHORT).show();
    }

}