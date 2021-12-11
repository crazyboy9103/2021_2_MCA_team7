package com.mobilecompute.test.stepcounter;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class Stopwatch2 {
    Context context;

    PaceEstimator paceEstimator;
    VoiceFeedback voice;

    Chronometer chronometer;
    boolean running = false;
    private long pauseOffset;
    Button startButton, resetButton;
    private Handler mHandler;
    Runnable feedbackLoop;
    TextView current_pace;

    private int mInterval = 10000;

    public Stopwatch2(Context context,
                      PaceEstimator paceEstimator,
                      VoiceFeedback voice) {
        this.context = context;
        this.paceEstimator = paceEstimator;
        this.voice = voice;

        startButton = (Button) ((AppCompatActivity) context).findViewById(R.id.start_pause_button2);
        resetButton = (Button) ((AppCompatActivity) context).findViewById(R.id.resetButton2);
        chronometer = (Chronometer) ((AppCompatActivity) context).findViewById(R.id.chronometer2);
        current_pace = (TextView) ((AppCompatActivity) context).findViewById(R.id.current_pace);

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
//        double pace = 10;
        double pace = paceEstimator.getPace();

        current_pace.setText(String.valueOf(pace));
        voice.feedback(pace);
    }

    public void startTimer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            current_pace.setText("0");
            chronometer.start();
            running = true;
            feedbackLoop.run();
        }
    }

    public void pauseTimer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
            mHandler.removeCallbacks(feedbackLoop);
        }
    }

    public void resetTimer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        current_pace.setText("0");
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
