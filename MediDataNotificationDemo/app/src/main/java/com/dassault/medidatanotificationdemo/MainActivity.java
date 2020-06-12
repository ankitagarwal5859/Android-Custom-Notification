package com.dassault.medidatanotificationdemo;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dassault.medidatanotificationdemo.notification.DateUtils;
import com.dassault.medidatanotificationdemo.notification.NotificationAlarmReceiver;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etStartDate, etEndDate, etStartTime, etEndTime, etPercentage;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btCreateNotification = findViewById(R.id.chanel1);
        etStartDate = findViewById(R.id.et_start_date);
        etEndDate = findViewById(R.id.et_end_date);
        etStartTime = findViewById(R.id.et_start_time);
        etEndTime = findViewById(R.id.et_end_time);
        etPercentage = findViewById(R.id.et_per);

        btCreateNotification.setOnClickListener(this);
        etStartDate.setOnClickListener(this);
        etEndDate.setOnClickListener(this);
        etStartTime.setOnClickListener(this);
        etEndTime.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chanel1:
                createNotification();
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

            default://fallout
        }
    }

    private void createNotification() {
        String startDate = etStartDate.getText().toString() + " " + etStartTime.getText().toString();
        String endDate = etEndDate.getText().toString() + " " + etEndTime.getText().toString();
        int percentage = Integer.parseInt(etPercentage.getText().toString());
        Date completeDate = DateUtils.getDateAfterCompletion(startDate, endDate, percentage);
        String finalDate = DateUtils.format.format(completeDate);
        Toast.makeText(this, "Notification created for date " + finalDate, Toast.LENGTH_SHORT).show();
        setAlarm(completeDate);
    }


    public void setAlarm(Date completeDate) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, NotificationAlarmReceiver.class);
        alarmIntent.putExtra("TIME", etPercentage.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, completeDate.getTime(), 0, pendingIntent);// Millisec * Second * Minute
    }

    private void showDatePicker(final EditText et) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
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
        datePickerDialog.show();
    }

    private void showTimePicker(final EditText et) {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
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
        timePickerDialog.show();
    }

}
