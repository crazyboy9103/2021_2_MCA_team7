package com.mobilecompute.test.stepcounter;

import java.lang.Math;


public class CadenceLPFEstimator {
    // Cadence estimator using a low-pass filter
    float timeStamp;
    float cadence = 0.0F;
    float beta;
    boolean initialized = false;

    public CadenceLPFEstimator(float beta){
        this.beta = beta;
    }

    public void reset(){
        cadence = 0.0F;
        initialized = false;
    }

    public void update(float newTimeStamp){
        if (!initialized){
            timeStamp = newTimeStamp;
            initialized = true;
            return;
        }

        float elapsedTime = Math.max((newTimeStamp - timeStamp)/1000, 0.01F); // in second (taking maximum is for ensuring not to divide by zero)
        float measurement = 1/elapsedTime * 60; // in steps per minute
        cadence = cadence + beta*(measurement - cadence);
        timeStamp = newTimeStamp;
    }
}
