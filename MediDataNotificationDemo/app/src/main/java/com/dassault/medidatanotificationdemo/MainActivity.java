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
import com.dassault.medidatanotificationdemo.notification.model.CustomNotificationAnyTimeFormTemplate;
import com.dassault.medidatanotificationdemo.notification.model.NotificationDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etStartDate, etEndDate, etStartTime, etEndTime, etPercentage, etForm, etRepeatTime, etRepeatCount, etAlarmTrigger;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private boolean shouldNotifyToService = false;
    private final static String TAG = MainActivity.class.getSimpleName();
    private ArrayList<CustomNotificationAnyTimeFormTemplate> dataSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btCreateNotification = findViewById(R.id.chanel1);
        Button btShowData = findViewById(R.id.show_data);
        Button btRepeatAlarm = findViewById(R.id.bt_repeat_alarm);

        Button btSetAlarm = findViewById(R.id.bt_set_alarm);
        Button btAnyTime = findViewById(R.id.bt_any_time);
        etStartDate = findViewById(R.id.et_start_date);
        etEndDate = findViewById(R.id.et_end_date);
        etStartTime = findViewById(R.id.et_start_time);
        etEndTime = findViewById(R.id.et_end_time);
        etPercentage = findViewById(R.id.et_per);
        etForm = findViewById(R.id.et_form);
        etRepeatCount = findViewById(R.id.et_repeat_count);
        etRepeatTime = findViewById(R.id.et_repeat_time);
        etAlarmTrigger = findViewById(R.id.et_alarm_trigger);

        btCreateNotification.setOnClickListener(this);
        btSetAlarm.setOnClickListener(this);
        btRepeatAlarm.setOnClickListener(this);
        btShowData.setOnClickListener(this);
        btAnyTime.setOnClickListener(this);
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
            case R.id.bt_set_alarm:
                setOrUpdateAlarm(0);
                break;
            case R.id.bt_any_time:
                createAnyTimeNotificationDataSet();
                break;
            default://fallout
        }
    }

    private void setOrUpdateAlarm(int FormId) {
        Toast.makeText(this, "Alarm set", Toast.LENGTH_SHORT).show();
        int repeatTime = Integer.parseInt(etAlarmTrigger.getText().toString());
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, NotificationAlarmReceiver.class);
        alarmIntent.putExtra("Message", "Alarm triggered for time " + repeatTime);
        alarmIntent.setAction(NotificationAlarmReceiver.ACTION);
        checkAndCancelIfAlarmExists(FormId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), FormId,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + repeatTime, repeatTime, pendingIntent);

    }

    private void checkAndCancelIfAlarmExists(int formId) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, NotificationAlarmReceiver.class);
        alarmIntent.setAction(NotificationAlarmReceiver.ACTION);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), formId,
                alarmIntent,
                PendingIntent.FLAG_NO_CREATE);
        boolean alarmUp = (pi != null);
        if (alarmUp) {
            Log.d("NotificationAlarmReceiver", "Alarm is already active");
            Log.d("NotificationAlarmReceiver", "Canceling Alarm");
            alarmManager.cancel(pi);
            pi.cancel();
        } else {
            Log.d("NotificationAlarmReceiver", "Alarm is not active so create a new alarm");
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
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, alarmIntent, 0);
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

    private void createAnyTimeNotificationDataSet() {
        String dayOfOccurrence1 = "M,Tu,Sa";
        String dayOfOccurrence2 = "Th";
        String dayOfOccurrence3 = "M,Tu,Sa";
        String dayOfOccurrence4 = "Th";
        CustomNotificationAnyTimeFormTemplate dataSet1 = new CustomNotificationAnyTimeFormTemplate("formOID-1", dayOfOccurrence1, "16:19,18:56,19:00", "Alarm for formOID-1 Type 1");

        CustomNotificationAnyTimeFormTemplate dataSet2 = new CustomNotificationAnyTimeFormTemplate("formOID-1", dayOfOccurrence2, "16:00", "Alarm for formOID-1 Type 2");

        CustomNotificationAnyTimeFormTemplate dataSet3 = new CustomNotificationAnyTimeFormTemplate("formOID-2", dayOfOccurrence3, "10:00,11:00,4:00", "Alarm for formOID-2 Type 1");

        CustomNotificationAnyTimeFormTemplate dataSet4 = new CustomNotificationAnyTimeFormTemplate("formOID-2", dayOfOccurrence4, "16:00", "Alarm for formOID-2 Type 2");
        dataSet.add(dataSet1);
        // dataSet.add(dataSet2);
        // dataSet.add(dataSet3);
        //  dataSet.add(dataSet4);
        triggerAnyTimeNotification();

    }

    private void triggerAnyTimeNotification() {
        Toast.makeText(this, "Any time notification triggered", Toast.LENGTH_SHORT).show();
        for (CustomNotificationAnyTimeFormTemplate obj : dataSet) {
            setAlarm(obj);
        }

    }

    private void setAlarm(CustomNotificationAnyTimeFormTemplate obj) {
        String[] totalAlarm = obj.getWeekDayRecurrences().split(",");
        String[] deliveryTime = obj.getDeliveryTime().split(",");

        for (int i = 0; i < totalAlarm.length; i++) {
            String time = deliveryTime[i];
            String hourMin[] = time.split(":");
            int hour = Integer.parseInt(hourMin[0]);
            int min = Integer.parseInt(hourMin[1]);
            Calendar alarmCalendar = Calendar.getInstance();

            alarmCalendar.set(Calendar.DAY_OF_WEEK, DateUtils.getDay(totalAlarm[i]));

            alarmCalendar.set(Calendar.HOUR_OF_DAY, hour);
            alarmCalendar.set(Calendar.MINUTE, min);
            alarmCalendar.set(Calendar.SECOND, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(this, NotificationAlarmReceiver.class);
            alarmIntent.putExtra("Message", obj.getMessage() + " for time " + time);
            final int id = (int) System.currentTimeMillis();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, alarmIntent, 0);
            Long alarmTime = alarmCalendar.getTimeInMillis();

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, AlarmManager.INTERVAL_DAY * 7, pendingIntent);

        }

    }

}
