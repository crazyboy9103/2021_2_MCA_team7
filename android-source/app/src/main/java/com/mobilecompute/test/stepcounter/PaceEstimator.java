package com.mobilecompute.test.stepcounter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;


public class PaceEstimator implements LocationListener {
    private final Context context;
    // Context context;
    private LocationManager locationManager;
    private Location mLastlocation = null;
    private double speed;
    private TextView tvGetSpeed, tvCalSpeed, tvTime, tvLastTime, tvGpsEnable, tvTimeDif, tvDistDif;

    double pace=0; // in m/sec
    LinkedList<Double> interval = new LinkedList<Double>();
    LinkedList<Double> interval_distance = new LinkedList<Double>();
    private double total_interval = 0;
    private double total_interval_distance = 0;

//    private int mInterval = 1000;

    public PaceEstimator(Context context) {
         this.context = context;


        tvGetSpeed = (TextView) ((AppCompatActivity) context).findViewById(R.id.tvGetSpeed);
        tvCalSpeed = (TextView) ((AppCompatActivity) context).findViewById(R.id.tvCalSpeed);
        tvTime = (TextView) ((AppCompatActivity) context).findViewById(R.id.tvTime);
        tvLastTime = (TextView) ((AppCompatActivity) context).findViewById(R.id.tvLastTime);
        tvGpsEnable = (TextView) ((AppCompatActivity) context).findViewById(R.id.tvGpsEnable);
        tvTimeDif = (TextView) ((AppCompatActivity) context).findViewById(R.id.tvTimeDif);
        tvDistDif = (TextView) ((AppCompatActivity) context).findViewById(R.id.tvDistDif);

        //권한 체크
        if (ActivityCompat.checkSelfPermission(this.context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager = (LocationManager)this.context.getSystemService(Context.LOCATION_SERVICE);

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String formatDate = sdf.format(new Date(lastKnownLocation.getTime()));
            tvTime.setText(": " + formatDate);  //Time
        }
        // GPS 사용 가능 여부 확인
        boolean isEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        tvGpsEnable.setText(": " + isEnable);  //GPS Enable
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,0, this);
    }

    public double getPace(){
        if(interval.size() < 10 || interval_distance.size()<10){
            return -1;
        }
        else{
            for(int i = 0 ; i<10; i++) {
                total_interval += interval.get(i);
                total_interval_distance += interval_distance.get(i);
            }
            pace = total_interval_distance / total_interval;
            total_interval = 0;
            total_interval_distance = 0;
            return Math.max(pace, 0);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        double deltaTime = 0;

        //  getSpeed() 함수를 이용하여 속도를 계산
        double getSpeed = Double.parseDouble(String.format("%.3f", location.getSpeed()));
        tvGetSpeed.setText(": " + getSpeed);  //Get Speed
        String formatDate = sdf.format(new Date(location.getTime()));
        tvTime.setText(": " + formatDate);  //Time

        // 위치 변경이 두번째로 변경된 경우 계산에 의해 속도 계산
        if(mLastlocation != null) {
            //시간 간격
            deltaTime = (location.getTime() - mLastlocation.getTime()) / 1000.0;
            tvTimeDif.setText(": " + deltaTime + " sec");  // Time Difference
            tvDistDif.setText(": " + mLastlocation.distanceTo(location) + " m");  // Time Difference
            // 속도 계산
            speed = mLastlocation.distanceTo(location) / deltaTime;

            String formatLastDate = sdf.format(new Date(mLastlocation.getTime()));
            tvLastTime.setText(": " + formatLastDate);  //Last Time

            double calSpeed = Double.parseDouble(String.format("%.3f", speed));
            tvCalSpeed.setText(": " + calSpeed);  //Cal Speed

            interval.addLast(deltaTime);
            interval_distance.addLast((double) mLastlocation.distanceTo(location));

            if(interval.size() > 10){
                interval.removeFirst();
            }
            if(interval_distance.size() > 10){
                interval_distance.removeFirst();
            }
        }
        // 현재위치를 지난 위치로 변경
        mLastlocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        //권한 체크
        if (ActivityCompat.checkSelfPermission(this.context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // 위치정보 업데이트
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,0, this);
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
