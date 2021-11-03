package com.mobilecompute.test.stepcounter;

import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class VibrationFeedback {
    Vibrator vibrator;

    // vibration patterns
    final long[] tooFastVibPattern = new long[]{125, 250, 125, 250, 125, 250, 125, 250};
    final long[] tooSlowVibPattern = new long[]{500, 1000};
    final int repeat = -1;

    // cadence norm for giving feedback
    final static double OPTIMAL_CADENCE = 120.0;
    final static double CADENCE_MARGIN = 30.0;
    final static double TOO_FAST_CADENCE = OPTIMAL_CADENCE + CADENCE_MARGIN;
    final static double TOO_SLOW_CADENCE = OPTIMAL_CADENCE - CADENCE_MARGIN;

    public VibrationFeedback(Vibrator vibrator){
        this.vibrator = vibrator;
    }

    public void feedback(double cadence) {
        if (cadence >= TOO_FAST_CADENCE) {
            vibrate(tooFastVibPattern);
        } else if (cadence <= TOO_SLOW_CADENCE) {
            vibrate(tooSlowVibPattern);
        }
    }

    public void vibrate(long[] pattern){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, repeat));
        } else{
            vibrator.vibrate(pattern, repeat);
        }
    }

    public void cancel(){
        vibrator.cancel();
    }
}
