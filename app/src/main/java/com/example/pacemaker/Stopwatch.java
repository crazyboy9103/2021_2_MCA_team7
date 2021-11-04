package com.example.pacemaker;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Stopwatch {
    Context context;
    StepCounterListener listener;
    VibrationFeedback vibrator;
    Chronometer chronometer;
    boolean running = false;
    private long pauseOffset;
    Button startButton, resetButton;
    private Handler mHandler;
    Runnable feedbackLoop;
    TextView tvStepRate;

    private int mInterval = 1000;
    private int cadence;

    public Stopwatch(Context context, StepCounterListener listener, VibrationFeedback vibrator) {
        this.context = context;
        this.listener = listener;
        this.vibrator = vibrator;

        startButton = (Button) ((AppCompatActivity)context).findViewById(R.id.start_pause_button);
        resetButton = (Button) ((AppCompatActivity)context).findViewById(R.id.resetButton);
        chronometer = (Chronometer) ((AppCompatActivity)context).findViewById(R.id.chronometer);
        tvStepRate = (TextView) ((AppCompatActivity)context).findViewById(R.id.tvStepRate);

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

    protected  void updateStatus() {
        long elps = SystemClock.elapsedRealtime() - chronometer.getBase();
        cadence = (int) (listener.step_count * 60 * 1000 / elps);
        tvStepRate.setText(cadence+ "");
        vibrator.feedback(cadence);
    }

    public void startTimer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            listener.startSensor();
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
            listener.pauseSensor();
            mHandler.removeCallbacks(feedbackLoop);
        }
    }

    public void resetTimer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        listener.step_count = 0;
        tvStepRate.setText(0+"");
    }

    public void onToggleClicked(View view) {
        if (!running) {
            startTimer(view);
            startButton.setText("Pause");
        }
        else {
            pauseTimer(view);
            startButton.setText("Start");
        }
    }
}
