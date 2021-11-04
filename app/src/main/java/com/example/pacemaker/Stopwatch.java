package com.example.pacemaker;

import android.content.Context;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.appcompat.app.AppCompatActivity;

public class Stopwatch {
    Context context;
    StepCounterListener listener;
    Chronometer chronometer;
    boolean running = false;
    private long pauseOffset;
    Button startButton, resetButton;

    public Stopwatch(Context context, StepCounterListener listener) {
        this.context = context;
        this.listener = listener;

        startButton = (Button) ((AppCompatActivity)context).findViewById(R.id.start_pause_button);
        resetButton = (Button) ((AppCompatActivity)context).findViewById(R.id.resetButton);
        chronometer = (Chronometer) ((AppCompatActivity)context).findViewById(R.id.chronometer);

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
    }

    public void startTimer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            listener.startSensor();
            chronometer.start();
            running = true;
        }
    }

    public void pauseTimer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
            listener.pauseSensor();
        }
    }

    public void resetTimer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        listener.step_count = 0;
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
