package com.mobilecompute.test.stepcounter;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class CadenceKFEstimator {
    double timeStamp; // in seconds
    double R_1 = 0.01;
    double R_2 = 0.01;
    double mu_u = 1; // average delay in the measurement
    RealMatrix H = new Array2DRowRealMatrix(new double[][] {{1, -mu_u}});
    RealVector x;
    RealMatrix P;
    int total_step;
    double cadence; // in steps per minute
    boolean initialized = false;

    public CadenceKFEstimator(){
        reset();
    }

    public void reset(){
        x = new ArrayRealVector(new double[] {0, 0});
        P = new Array2DRowRealMatrix(new double[][]{
                {Math.pow(10, 4), 0},
                {0, Math.pow(10, 4)}
        });
        total_step = 0;
        cadence = 0.0;
        initialized = false;
    }

    protected void predict(double newTimeStamp){
        if(!initialized){
            total_step = 0;
            timeStamp = newTimeStamp;
            initialized = true;
            return;
        }

        double T_elapse = newTimeStamp - timeStamp;
        if (T_elapse > 0.001){
            RealMatrix F = new Array2DRowRealMatrix(new double[][] {
                    {1, T_elapse}, {0, 1}
            });
            RealMatrix Q = new Array2DRowRealMatrix(new double[][] {
                    {R_1*Math.pow(T_elapse, 3)/3, R_1*Math.pow(T_elapse, 2)/2},
                    {R_1*Math.pow(T_elapse, 2)/2, R_1*T_elapse}
            });
            x = F.operate(x);
            P = F.multiply(P).multiply(F.transpose()).add(Q);

            timeStamp = newTimeStamp;
        }
    }

    protected void correct(){
        double x_2 = x.getEntry(1);

        RealMatrix R = new Array2DRowRealMatrix(new double[][]{
                {R_2*Math.pow(x_2, 2)}
        });
        RealVector z = new ArrayRealVector(new double[] {total_step});

        RealMatrix temp = MatrixUtils.inverse(H.multiply((P)).multiply(H.transpose()).add(R));
        RealMatrix K = P.multiply(H.transpose()).multiply(temp);
        RealMatrix I = new Array2DRowRealMatrix(new double[][]{
                {1, 0}, {0, 1}
        });

        x = x.add(K.operate(z.subtract(H.operate(x))));
        P = I.subtract(K.multiply(H)).multiply(P);
    }

    public void update(double newTimeStep){
        total_step += 1;

        double newTimeStepInSec =  newTimeStep/1000; // in seconds
        predict(newTimeStepInSec);
        correct();
        cadence = x.getEntry(1) * 60; // in steps per minute
    }
}
