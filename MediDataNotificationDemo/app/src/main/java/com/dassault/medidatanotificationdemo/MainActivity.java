package com.dassault.medidatanotificationdemo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dassault.medidatanotificationdemo.notification.DateUtils;
import com.dassault.medidatanotificationdemo.notification.NotificationAlarmReceiver;
import com.dassault.medidatanotificationdemo.notification.NotificationService;
import com.dassault.medidatanotificationdemo.notification.ShowDataActivity;
import com.dassault.medidatanotificationdemo.notification.model.CustomNotification;
import com.dassault.medidatanotificationdemo.notification.model.NotificationDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etStartDate, etEndDate, etStartTime, etEndTime, etPercentage, etForm, etRepeatTime, etRepeatCount;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private boolean shouldNotifyToService = false;
    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btCreateNotification = findViewById(R.id.chanel1);
        Button btShowData = findViewById(R.id.show_data);
        Button btRepeatAlarm = findViewById(R.id.bt_repeat_alarm);
        etStartDate = findViewById(R.id.et_start_date);
        etEndDate = findViewById(R.id.et_end_date);
        etStartTime = findViewById(R.id.et_start_time);
        etEndTime = findViewById(R.id.et_end_time);
        etPercentage = findViewById(R.id.et_per);
        etForm = findViewById(R.id.et_form);
        etRepeatCount = findViewById(R.id.et_repeat_count);
        etRepeatTime = findViewById(R.id.et_repeat_time);

        btCreateNotification.setOnClickListener(this);
        btRepeatAlarm.setOnClickListener(this);
        btShowData.setOnClickListener(this);
        etStartDate.setOnClickListener(this);
        etEndDate.setOnClickListener(this);
        etStartTime.setOnClickListener(this);
        etEndTime.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chanel1:
                checkMapDataSet();
                break;
            case R.id.et_start_date:
                showDatePicker(etStartDate);

                break;
            case R.id.et_end_date:
                showDatePicker(etEndDate);
                break;
            case R.id.et_start_time:
                showTimePicker(etStartTime);
                break;
            case R.id.et_end_time:
                showTimePicker(etEndTime);
                break;
            case R.id.show_data:
                startActivity(new Intent(this, ShowDataActivity.class));
                break;
            case R.id.bt_repeat_alarm:
                setRepeatAlarm();
                break;

            default://fallout
        }
    }

    private void setRepeatAlarm() {
        int count = Integer.parseInt(etRepeatCount.getText().toString());
        int repeatAfter = Integer.parseInt(etRepeatTime.getText().toString());

        long alarmTimestamp = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(this, NotificationAlarmReceiver.class);
            alarmIntent.putExtra("Message", "Alarm count " + i);
            final int id = (int) System.currentTimeMillis();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, alarmIntent, 0);
            alarmTimestamp = alarmTimestamp + repeatAfter;
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimestamp, pendingIntent);
        }

    }

    private void showDatePicker(final EditText et) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        final DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(0);
                        cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                        Date chosenDate = cal.getTime();


                        String df_medium_us_str = DateUtils.dateFormat.format(chosenDate);
                        et.setText(df_medium_us_str);

                    }
                }, mYear, mMonth, mDay);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                datePickerDialog.show();

            }
        }, 300);

    }

    private void showTimePicker(final EditText et) {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String min = "";
                        if (minute < 10) {
                            min = "0";
                        }
                        min = min + minute;
                        et.setText(hourOfDay + ":" + min + ":" + "00");
                    }
                }, mHour, mMinute, true);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                timePickerDialog.show();

            }
        }, 300);

    }

    private void checkMapDataSet() {
        String[] keyArray = etForm.getText().toString().split(",");
        if (keyArray.length == 0) {
            keyArray[0] = etForm.getText().toString();
        }
        for (String key : keyArray) {
            if (!NotificationDataSet.notificationMap.containsKey(key)) {
                NotificationDataSet.notificationMap.put(key, new ArrayList<CustomNotification>());
            }
            insertDataInMap(key);
        }
        syncDataToService();
    }

    private void insertDataInMap(String key) {
        String startDate = etStartDate.getText().toString() + " " + etStartTime.getText().toString();
        String endDate = etEndDate.getText().toString() + " " + etEndTime.getText().toString();
        String[] percentage = etPercentage.getText().toString().split(",");
        if (percentage.length == 0) {
            percentage[0] = etPercentage.getText().toString();
        }
        ArrayList<CustomNotification> list = NotificationDataSet.notificationMap.get(key);
        if (!list.isEmpty()) {
            for (String per : percentage) {
                boolean isPercentageHaving = isMapContains(list, per);
                if (!isPercentageHaving) {
                    shouldNotifyToService = true;
                    list.add(new CustomNotification(Integer.parseInt(per), "Schedule notification  for" + key + " At " + per + "%", startDate, endDate, false));
                }
            }
        } else {
            shouldNotifyToService = true;
            for (String per : percentage) {
                list.add(new CustomNotification(Integer.parseInt(per), "Schedule notification  for " + key + " At " + per + "%", startDate, endDate, false));
            }
        }

    }

    private boolean isMapContains(ArrayList<CustomNotification> list, String percentage) {
        boolean flag = false;
        for (CustomNotification objCustomNotification : list) {
            if (objCustomNotification.getPercentage() == Integer.parseInt(percentage)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private void syncDataToService() {
        if (shouldNotifyToService) {
            shouldNotifyToService = false;
            Log.d(TAG, "Sync Service init called");
            Intent intent = new Intent(MainActivity.this, NotificationService.class);
            startService(intent);
            Toast.makeText(this, "Notification created successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Notification is already triggered", Toast.LENGTH_SHORT).show();
        }

    }

}
