package com.example.healthysteps;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class StepService extends Service implements SensorEventListener {
    private static final String TAG = "StepService";
    private static final String CHANNELID = "channel";
    private final IBinder mBinder = new LocalBinder();
    private int step;
    private int energyCount;
    private String time;


    public class LocalBinder extends Binder {
        StepService getService() {
            // Return this instance of LocalService so clients can call public methods
            return StepService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("StepService", "Service Started");
        Date currentTime = Calendar.getInstance().getTime();
        time = String.valueOf(currentTime);
        time = time.replaceAll("\\s+", "");
        time = time.substring(0, 8);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("DAY_STEPS", 0);

        step = pref.getInt(time, 0);
        energyCount = pref.getInt("energyCount", 0);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        String channelId = "step_channel";
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channelId, "Шаговый канал", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Good Work, keep going!")
                .setContentText("Check your steps today!")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return START_NOT_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (time != null) {
            Date currentTime = Calendar.getInstance().getTime();
            String newTime = String.valueOf(currentTime);
            newTime = newTime.replaceAll("\\s+", "");
            Log.i("StepService", newTime);
            String hour = newTime.substring(8, 10);
            newTime = newTime.substring(0, 8);
            storeHourlyData(newTime, hour);

            if (!newTime.equals(time)) {
                time = newTime;
                step = 0;
            }
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("DAY_STEPS", 0);

            Log.i("StepService", "Walked " + time);
            if( preferences.getBoolean("COUNTER",true)) {
                step += event.values[0];
                SharedPreferences prefs = getSharedPreferences("DAY_STEPS", MODE_PRIVATE);
            }
            Log.i("StepService", String.valueOf(step));

            SharedPreferences pref = getApplicationContext().getSharedPreferences("DAY_STEPS", 0);
            SharedPreferences.Editor editor = pref.edit();

            editor.putInt(time, step);
            editor.apply();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("StepService", "Service Destroyed");
    }

    public int getStep() {

        return this.step;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void storeHourlyData(String date, String hour) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("HOUR_STEPS", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(date + hour, step);
        Log.i("StepService", date + hour + " Stored");
        editor.apply();
    }

}