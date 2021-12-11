package com.mobilecompute.test.stepcounter;

import static android.content.Context.VIBRATOR_SERVICE;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VoiceFeedback {
    Context context;

    final String goWideStep = "Increase your step size!";
    final String goNarrowStep = "Decrease your step size!";
    final String wait_pace = "Wait until collecting 10 10 samples";
    final int repeat = -1;

    private TextView target_distance, target_time;
    private EditText target_distance_input, target_time_input;
    Button target_distance_button, target_time_button;

    private double TARGET_DISTANCE=10;
    private double TARGET_TIME=60;
    private double TARGET_PACE;

    private TextView target_pace;

    public VoiceFeedback(Context context){
        this.context = context;

        target_distance = (TextView) ((AppCompatActivity) context).findViewById(R.id.target_distance);
        target_time = (TextView) ((AppCompatActivity) context).findViewById(R.id.target_time);
        target_distance_input = (EditText) ((AppCompatActivity) context).findViewById(R.id.target_distance_input);
        target_time_input = (EditText) ((AppCompatActivity) context).findViewById(R.id.target_time_input);
        target_distance_button = (Button) ((AppCompatActivity) context).findViewById(R.id.target_distance_button);
        target_time_button = (Button) ((AppCompatActivity) context).findViewById(R.id.target_time_button);
        target_pace = (TextView) ((AppCompatActivity) context).findViewById(R.id.target_pace);

        target_distance_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String s1 = target_distance_input.getText().toString();
                TARGET_DISTANCE = Double.parseDouble(s1);
                TARGET_PACE = (TARGET_DISTANCE * 1000) / (TARGET_TIME * 60); // 단위: m/sec
                target_pace.setText(String.valueOf(TARGET_PACE));
            }
        });

        target_time_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String s2 = target_time_input.getText().toString();
                TARGET_TIME = Double.parseDouble(s2);
                TARGET_PACE = (TARGET_DISTANCE * 1000) / (TARGET_TIME * 60); // 단위: m/sec
                target_pace.setText(String.valueOf(TARGET_PACE));
            }
        });

    }

    public void feedback(double pace) {
        if (pace == -1){
            play(wait_pace);
        }
        else{
            if (pace >= TARGET_PACE * 1.1) {
                play(goNarrowStep);
            } else if (pace <= TARGET_PACE * 0.9) {
                play(goWideStep);
            }
        }
    }

    public void play(String voice){
        Toast.makeText(context.getApplicationContext(), voice, Toast.LENGTH_SHORT).show();
    }

}
