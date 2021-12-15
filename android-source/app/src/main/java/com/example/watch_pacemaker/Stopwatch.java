package com.example.watch_pacemaker;



import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.watch_pacemaker.databinding.ActivityMainBinding;


public class Stopwatch {
    Context context;
    ActivityMainBinding binding;
    Chronometer chronometer;
    VoiceFeedback voice;
    PaceEstimator paceEstimator;
    CadenceListener cadenceListener;
    HeartrateListener heartRateListener;
    VibrationFeedback vibrationFeedback;

//    int needCorrection = 0;
    int targetCorrection = 0;
    int correctionInterval = 5;

//    int ageidx = 1;
//    int[] age = {20, 25, 30, 35, 40, 45, 50, 55, 65, 70};
//    int[] hardcore = {160, 156, 152, 148, 144, 140,136, 132, 124, 120};
//    int[] fatburn = {120, 117, 114, 111, 108, 105, 102, 99, 93, 90};

    int count_cadence_feedback=0;
    int count_vibration_feedback=0;
    int count_voice_feedback=0;

    boolean running = false;
    private long pauseOffset;

    Button startButton, resetButton;
    TextView current_pace, current_cadence, current_heart_rate;
    TextView DetectorExists;

    private Handler mHandler;
    Runnable feedbackLoop;

    final private int mInterval = 1000;

    public Stopwatch(Context context,
                     ActivityMainBinding binding,
                     CadenceListener cadenceListener,
                     HeartrateListener heartRateListener,
                     PaceEstimator paceEstimator,
                     VoiceFeedback voice,
                     VibrationFeedback vibrationFeedback
    ) {
        this.context = context;
        this.binding = binding;
        this.voice = voice;
        this.paceEstimator = paceEstimator;
        this.cadenceListener = cadenceListener;
        this.heartRateListener = heartRateListener;
        this.vibrationFeedback = vibrationFeedback;

        startButton = binding.startPauseButton;
        resetButton = binding.resetButton;
        chronometer = binding.chronometer;
        current_pace = binding.tvPace;
        current_cadence = binding.tvDetectorExists;
        current_heart_rate = binding.tvHeartRate;
        DetectorExists = (TextView) binding.DetectorExists;


        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onToggleClicked(v);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetTimer(v);
            }
        });

        mHandler = new Handler();

        feedbackLoop = new Runnable() {
            @Override
            public void run() {
                try {
                    updateStatus();
                } finally {
                    mHandler.postDelayed(feedbackLoop, mInterval);
                }
            }
        };
    }

    protected void updateStatus() {
        double cadence = cadenceListener.cadenceEstimator.getCadence();
        double heartRate = heartRateListener.getHeartRate();
        double pace = paceEstimator.getPace();

//        if (this.needCorrection == 0) {
//            // No need for correction
//            if (heartRate > hardcore[ageidx]) {
//                this.needCorrection = -1;
//                this.targetCorrection = (int) cadence - correctionInterval;
//                textDetectorExists.setText("Cad target: " + targetCorrection + " (spm)");
//            }
//            else if (heartRate < fatburn[ageidx]){
//                this.needCorrection = 1;
//                this.targetCorrection = (int) cadence + correctionInterval;
//                textDetectorExists.setText("Cad target: " + targetCorrection + " (spm)");
//            }
//
//        } else if (this.needCorrection == -1){
//            // Need correction
//            if (cadence > targetCorrection){
//                // Go slower alarm
//                vibrationFeedback.vibrate(vibrationFeedback.goSlowerPattern);
//            }
//            else {
//                this.needCorrection = 0;
//                this.targetCorrection = 0;
//            }
//        } else{
//            // Need correction
//            if (cadence < targetCorrection){
//                // Go faster alarm
//                vibrationFeedback.vibrate(vibrationFeedback.goFasterPattern);
//
//            }
//            else {
//                this.needCorrection = 0;
//                this.targetCorrection = 0;
//            }
//        }
        if (count_cadence_feedback >= 30 && heartRate > 120) {
            targetCorrection = vibrationFeedback.getTargetCadence() - correctionInterval;
            DetectorExists.setText("Cad target: " + targetCorrection + " (spm)");
            vibrationFeedback.setTargetCadence(targetCorrection);
            count_cadence_feedback = 0;
        }
        else if (count_cadence_feedback >= 30 && heartRate < 80){
            targetCorrection = vibrationFeedback.getTargetCadence() + correctionInterval;
            DetectorExists.setText("Cad target: " + targetCorrection + " (spm)");
            vibrationFeedback.setTargetCadence(targetCorrection);
            count_cadence_feedback = 0;
        }
        else if (count_cadence_feedback >= 30){
            targetCorrection = vibrationFeedback.getTargetCadence();
            DetectorExists.setText("Cad target: " + targetCorrection + " (spm)");
            vibrationFeedback.setTargetCadence(targetCorrection);
            count_cadence_feedback = 0;
        }

        current_cadence.setText("" + (int)cadence);
        current_heart_rate.setText("" + (int) heartRate);
        if (count_voice_feedback >= 5) {
            voice.feedback(pace);
            count_voice_feedback=0;
        }

        if (count_vibration_feedback >= 5) {
            vibrationFeedback.feedback((int) cadence);
            count_vibration_feedback=0;
        }
        count_cadence_feedback ++;
        count_vibration_feedback ++;
        count_voice_feedback ++;
    }


    public void startTimer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
            cadenceListener.startSensor();
            heartRateListener.startSensor();
            current_pace.setText("Ready");
            feedbackLoop.run();
        }
    }

    public void pauseTimer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
            cadenceListener.pauseSensor();

            mHandler.removeCallbacks(feedbackLoop);
        }
    }

    public void resetTimer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        cadenceListener.stepCount = 0;

        current_pace.setText("0");
        current_cadence.setText("0");
        current_heart_rate.setText("0");
    }

    public void onToggleClicked(View view) {
        if (!running) {
            startTimer(view);
            startButton.setText("Pause");
        } else {
            pauseTimer(view);
            startButton.setText("Start");
        }
    }
}