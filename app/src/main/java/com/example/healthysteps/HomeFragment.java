package com.example.healthysteps;

import static android.content.Context.SENSOR_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment implements SensorEventListener {
    private static final int REQUEST_ACTIVITY_RECOGNITION = 1;
    private final String TAG = "StepCounter";
    private int stepCount;
    private StepService stepService;
    private boolean mBound = false;
    Sensor stepDetectorSensor;
    TextView stepCounterTextView;
    SensorManager sensorManager;
    private int settingSteps;
    private static final String CHANNEL_ID = "channel";
    private static final String CHANNEL_NAME = "Channel Name";
    private static final String CHANNEL_DESCRIPTION = "This channel is for stickers messaging";
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            StepService.LocalBinder binder = (StepService.LocalBinder) service;
            stepService = binder.getService();
            mBound = true;
            sensorManager.registerListener(stepService, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    TextView day1, day2, day3, day4, day5, day6, day7;
    ProgressBar mainBar, bar1, bar2, bar3, bar4, bar5, bar6, bar7;
    Switch mySwitch;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        stepCounterTextView = view.findViewById(R.id.showSteps);
        day1 = view.findViewById(R.id.TX_1);
        day2 = view.findViewById(R.id.TX_2);
        day3 = view.findViewById(R.id.TX_3);
        day4 = view.findViewById(R.id.TX_4);
        day5 = view.findViewById(R.id.TX_5);
        day6 = view.findViewById(R.id.TX_6);
        day7 = view.findViewById(R.id.TX_7);

        mainBar = view.findViewById(R.id.today_step);
        bar1 = view.findViewById(R.id.progressBar_1);
        bar2 = view.findViewById(R.id.progressBar_2);
        bar3 = view.findViewById(R.id.progressBar_3);
        bar4 = view.findViewById(R.id.progressBar_4);
        bar5 = view.findViewById(R.id.progressBar_5);
        bar6 = view.findViewById(R.id.progressBar_6);
        bar7 = view.findViewById(R.id.progressBar_7);

        mySwitch = view.findViewById(R.id.switch1);
        final SharedPreferences myPrefs = getActivity().getSharedPreferences("DAY_STEPS", 0);
        final SharedPreferences.Editor myEditor = myPrefs.edit();

        mySwitch.setChecked(myPrefs.getBoolean("COUNTER", true)); //false default

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //commit prefs on change
                myEditor.putBoolean("COUNTER", isChecked);
                myEditor.apply();

                if (!isChecked) {
                    Intent intent = new Intent(requireActivity().getApplicationContext(), StepService.class);
                    getActivity().unbindService(connection);
                    getActivity().stopService(intent);
                    sensorManager.unregisterListener(stepService);
                    mBound = false;
                    Log.i(TAG, "Stopped");
                }

                if (isChecked) {
                    Intent intent = new Intent(requireActivity().getApplicationContext(), StepService.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        getActivity().startForegroundService(intent);
                    }
                    mBound = getActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setStepDetectorSensor();
        }
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void setStepDetectorSensor() {
        sensorManager = (SensorManager) requireActivity().getSystemService(SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) == null) {
            Toast.makeText(requireContext(), "Null Sensor", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Null Sensor");
            // the step detector sensor does not exist
        } else {
            stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            if (mySwitch.isChecked() && !isMyServiceRunning()) {
                Intent intent = new Intent(requireContext(), StepService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    requireContext().startForegroundService(intent);
                } else {
                    requireContext().startService(intent);
                }
                Log.i(TAG, "Main Activity starting service");
            }
        }

        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, REQUEST_ACTIVITY_RECOGNITION);


        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        Intent intent = new Intent(requireContext(), StepService.class);

        if (mySwitch.isChecked()) {
            mBound = requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
            Log.i(TAG, "onResume()");
        }

        Date currentTime = Calendar.getInstance().getTime();
        Log.i(TAG, String.valueOf(currentTime));
        String time = String.valueOf(currentTime).replaceAll("\\s+", "").substring(0, 8);
        Log.i(TAG, time);
        SharedPreferences sharedpreferences = requireActivity().getSharedPreferences("DAY_STEPS", 0);
        stepCount = sharedpreferences.getInt(time, 0);
        stepCounterTextView.setText(Integer.toString(stepCount));
        settingSteps = sharedpreferences.getInt("SETTING_STEPS", 2000);


        mainBar.setProgress(stepCount * 100 / settingSteps);
        String[] dayOfWeek = calculateWeek();
        for (int i = 0; i < 7; i++) {
            TextView textView = new TextView[]{day1, day2, day3, day4, day5, day6, day7}[i];
            textView.setText(dayOfWeek[i]);
        }

        ProgressBar[] progressBars = new ProgressBar[]{bar1, bar2, bar3, bar4, bar5, bar6, bar7};
        for (int i = 6; i >= 0; i--) {
            long DAY_IN_MS = 1000 * 60 * 60 * 24;
            Date date = new Date(System.currentTimeMillis() - ((6 - i) * DAY_IN_MS));

            String barTime = String.valueOf(date).replaceAll("\\s+", "").substring(0, 8);
            progressBars[i].setProgress(sharedpreferences.getInt(barTime, 0) * 100 / settingSteps);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
        sensorManager.unregisterListener(this);
        if (mBound) {
            requireActivity().unbindService(connection);
            mBound = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mBound && event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            Log.d(TAG, "onSensorChanged called");
            stepCount = stepService.getStep();
            int remainingSteps = settingSteps - stepCount;

            stepCounterTextView.setText(String.valueOf(stepCount));
            mainBar.setProgress(stepCount * 100 / settingSteps);
            bar7.setProgress(stepCount * 100 / settingSteps);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private String[] calculateWeek() {
        LocalDate localDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localDate = LocalDate.now();
        }

        // Find the day from the local date
        DayOfWeek dayOfWeek = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dayOfWeek = DayOfWeek.from(localDate);
        }

        int val = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val = dayOfWeek.getValue();
        }

        assert dayOfWeek != null;
        Log.d(TAG, "Int Value of " + dayOfWeek.name() + " - " + val);

        String[] daysOfWeekArray = new String[7];

        for (int i = 0; i < 7; i++) {
            int index = (val + i) % 7;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                daysOfWeekArray[i] = DayOfWeek.of(index + 1).getDisplayName(TextStyle.SHORT, Locale.US);
            }
        }

        return daysOfWeekArray;
    }


    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) requireActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (StepService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void createNotificationChannel() {
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setDescription(CHANNEL_DESCRIPTION);
        }
        NotificationManager notificationManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager = requireActivity().getSystemService(NotificationManager.class);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
    }


}
