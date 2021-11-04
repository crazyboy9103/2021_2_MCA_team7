package com.example.pacemaker;

import static android.content.Context.SENSOR_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class VibrationFeedback {
    Context context;
    Vibrator vibrator;

    final long[] goFasterPattern = new long[] {125, 250, 125, 250, 125, 250, 125, 250};
    final long[] goSlowerPattern = new long[] {500,1000};
    final int repeat = -1;

    final static int OPTIMAL_CADENCE = 120;
    final static int CADENCE_MARGIN = 30;
    final static int TOO_FAST_CADENCE = OPTIMAL_CADENCE + CADENCE_MARGIN;
    final static int TOO_SLOW_CADENCE = OPTIMAL_CADENCE - CADENCE_MARGIN;

    public VibrationFeedback(Context context){
        this.context = context;
        vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

    }

    public void feedback(int cadence) {
        if (cadence >= TOO_FAST_CADENCE) {
            vibrate(goSlowerPattern);
        } else if (cadence <= TOO_SLOW_CADENCE) {
            vibrate(goFasterPattern);
        }
    }

    public void vibrate(long[] pattern){
        vibrator.vibrate(VibrationEffect.createWaveform(pattern, repeat));

    }

    public void cancel(){
        vibrator.cancel();
    }
}
