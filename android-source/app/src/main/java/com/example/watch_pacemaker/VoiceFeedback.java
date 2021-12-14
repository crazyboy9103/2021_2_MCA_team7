package com.example.watch_pacemaker;


import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.TextView;


public class VoiceFeedback {
    Context context;
    MediaPlayer increase, decrease, wait;

    final int repeat = -1;
    final String TAG = "Voice Feedback: ";
    final String goWideStep = "Increase your step size!";
    final String goNarrowStep = "Decrease your step size!";
    final String wait_pace = "Wait until collecting 10 samples";


    private double TARGET_DISTANCE=10;
    private double TARGET_TIME=60;
    private double TARGET_PACE;

    private TextView target_pace;

    public VoiceFeedback(Context context, MediaPlayer increase,
                         MediaPlayer decrease, MediaPlayer wait,
                         Double target_distance, Double target_time){
        this.context = context;
        this.increase = increase;
        this.decrease = decrease;
        this.wait = wait;

        TARGET_DISTANCE = target_distance; // 2 kilometers
        TARGET_TIME = target_time; // 1200seconds = 20 minutes

        TARGET_PACE = (TARGET_DISTANCE * 1000) / (TARGET_TIME * 60); // 단위: m/sec

    }

    public void feedback(double pace) {
        if (pace == -1){
            play(wait);
            Log.i(TAG, wait_pace);
        }
        else{
            if (pace >= TARGET_PACE * 1.15) {
                play(decrease);
                Log.i(TAG, goNarrowStep);
            } else if (pace <= TARGET_PACE * 0.85) {
                play(increase);
                Log.i(TAG, goWideStep);
            }
        }
    }

    public void play(MediaPlayer player){
        //Toast.makeText(context.getApplicationContext(), voice, Toast.LENGTH_SHORT).show();
        player.start();

    }

}