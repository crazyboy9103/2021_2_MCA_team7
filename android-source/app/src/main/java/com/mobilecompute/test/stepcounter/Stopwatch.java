package com.mobilecompute.test.stepcounter;

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
    CadenceLPFEstimator cadenceLPFEstimator;
    CadenceKFEstimator cadenceKFEstimator;
    VibrationFeedback vibrator;
    Chronometer chronometer;
    boolean running = false;
    private long pauseOffset;
    Button startButton, resetButton;
    private Handler mHandler;
    Runnable feedbackLoop;
    TextView tvStepRateLPF;
    TextView tvStepRateKF;

    private int mInterval = 1000;

    public Stopwatch(Context context, StepCounterListener listener,
                     CadenceLPFEstimator cadenceLPFEstimator,
                     CadenceKFEstimator cadenceKFEstimator,
                     VibrationFeedback vibrator) {
        this.context = context;
        this.listener = listener;
        this.cadenceLPFEstimator = cadenceLPFEstimator;
        this.cadenceKFEstimator = cadenceKFEstimator;
        this.vibrator = vibrator;

        startButton = (Button) ((AppCompatActivity) context).findViewById(R.id.start_pause_button);
        resetButton = (Button) ((AppCompatActivity) context).findViewById(R.id.resetButton);
        chronometer = (Chronometer) ((AppCompatActivity) context).findViewById(R.id.chronometer);
        tvStepRateLPF = (TextView) ((AppCompatActivity) context).findViewById(R.id.tvStepRateLPF);
        tvStepRateKF = (TextView) ((AppCompatActivity) context).findViewById(R.id.tvStepRateKF);

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
        double cadenceLPF = cadenceLPFEstimator.cadence;
        double cadenceKF = cadenceKFEstimator.cadence;

        tvStepRateLPF.setText(String.valueOf((int) cadenceLPF));
        tvStepRateKF.setText(String.valueOf((int) cadenceKF));
        vibrator.feedback((int) cadenceKF);
    }

    public void startTimer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            listener.startSensor();
            cadenceLPFEstimator.reset();
            cadenceKFEstimator.reset();
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
        listener.stepCount = 0;
        tvStepRateLPF.setText(0 + "");
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
