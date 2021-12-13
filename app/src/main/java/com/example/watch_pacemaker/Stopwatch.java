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
    CadenceListener listener;
    VibrationFeedback vibrationFeedback;


    boolean running = false;
    private long pauseOffset;

    Button startButton, resetButton;
    TextView current_pace, current_cadence;

    private Handler mHandler;
    Runnable feedbackLoop;

    final private int mInterval = 2000;

    public Stopwatch(Context context,
                     ActivityMainBinding binding,
                     CadenceListener listener,
                     PaceEstimator paceEstimator,
                     VoiceFeedback voice,
                     VibrationFeedback vibrationFeedback
    ) {
        this.context = context;
        this.binding = binding;
        this.voice = voice;
        this.paceEstimator = paceEstimator;
        this.listener = listener;
        this.vibrationFeedback = vibrationFeedback;

        startButton = binding.startPauseButton;
        resetButton = binding.resetButton;
        chronometer = binding.chronometer;
        current_pace = binding.tvPace;
        current_cadence = binding.tvDetectorExists;



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
        double cadence = listener.cadenceEstimator.getCadence();
        double pace = paceEstimator.getPace();

        current_cadence.setText("Cadence: " + cadence);
        voice.feedback(pace);
        vibrationFeedback.feedback((int)cadence);
    }


    public void startTimer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
            listener.startSensor();
            current_pace.setText("Speed: 0.0");
            feedbackLoop.run();
        }
    }

    public void pauseTimer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
            listener.pauseSensor();

            mHandler.removeCallbacks(feedbackLoop);
        }
    }

    public void resetTimer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        listener.stepCount = 0;

        current_pace.setText("Speed: 0.0");
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