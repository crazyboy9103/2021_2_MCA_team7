package com.example.watch_pacemaker;


import static android.content.Context.VIBRATOR_SERVICE;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

public class VibrationFeedback {
    Context context;
    Vibrator vibrator;

    final long[] goFasterPattern = new long[] {125, 250, 125, 250, 125, 250, 125, 250};
    final long[] goSlowerPattern = new long[] {500,1000};
    final int repeat = -1;

    final static int OPTIMAL_CADENCE = 180;
    final static int CADENCE_MARGIN = 5;
    final static int TOO_FAST_CADENCE = OPTIMAL_CADENCE + CADENCE_MARGIN;
    final static int TOO_SLOW_CADENCE = OPTIMAL_CADENCE - CADENCE_MARGIN;

    public VibrationFeedback(Context context){
        this.context = context;
        vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

    }

    public void feedback(int cadence) {
        if (cadence >= TOO_FAST_CADENCE) {
            vibrate(goSlowerPattern);
            Log.i("Vibrator", "Go Slower");
        } else if (cadence <= TOO_SLOW_CADENCE) {
            vibrate(goFasterPattern);
            Log.i("Vibrator", "Go Faster");
        }
    }

    public void vibrate(long[] pattern){
        vibrator.vibrate(VibrationEffect.createWaveform(pattern, repeat));


    }

    public void cancel(){
        vibrator.cancel();
    }
}