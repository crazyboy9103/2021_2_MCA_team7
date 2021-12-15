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

    public int OPTIMAL_CADENCE = 80;
    public int CADENCE_MARGIN = 20;
    public int TOO_FAST_CADENCE = OPTIMAL_CADENCE + CADENCE_MARGIN;
    public int TOO_SLOW_CADENCE = OPTIMAL_CADENCE - CADENCE_MARGIN;

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

    public void setTargetCadence(int cadence) {
        OPTIMAL_CADENCE = cadence;
        TOO_FAST_CADENCE = OPTIMAL_CADENCE + CADENCE_MARGIN;
        TOO_SLOW_CADENCE = OPTIMAL_CADENCE - CADENCE_MARGIN;
    }

    public int getTargetCadence() {
        return OPTIMAL_CADENCE;
    }

    public void vibrate(long[] pattern){
        vibrator.vibrate(VibrationEffect.createWaveform(pattern, repeat));
    }

    public void cancel(){
        vibrator.cancel();
    }
}