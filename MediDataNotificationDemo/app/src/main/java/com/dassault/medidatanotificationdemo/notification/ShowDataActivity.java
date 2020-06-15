package com.dassault.medidatanotificationdemo.notification;

import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dassault.medidatanotificationdemo.R;
import com.dassault.medidatanotificationdemo.notification.model.CustomNotification;
import com.dassault.medidatanotificationdemo.notification.model.NotificationDataSet;

import java.util.ArrayList;
import java.util.Date;

public class ShowDataActivity extends AppCompatActivity {
    private RadioGroup rb;
    private TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_data);
        rb = findViewById(R.id.radioGroup);
        tvData = findViewById(R.id.tv_show);
        rb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton:
                        ArrayList<CustomNotification> list = NotificationDataSet.notificationMap.get("Form1");

                        showData(list);

                        break;
                    case R.id.radioButton2:
                        ArrayList<CustomNotification> list1 = NotificationDataSet.notificationMap.get("Form2");
                        showData(list1);
                        break;
                }
            }
        });
    }

    private void showData(ArrayList<CustomNotification> list) {
        String str = "";
        for (CustomNotification objCustomNotification : list) {
            Date completeDate = DateUtils.getDateAfterCompletion(objCustomNotification.getStartDate(), objCustomNotification.getEndDate(), objCustomNotification.getPercentage());
            String finalDate = DateUtils.format.format(completeDate);
            str = str + "\n"  + objCustomNotification.getMessage() + " will show at \n" + finalDate;
            str=str+"\n"+"-------------------------------------------------";
        }
        tvData.setText(str);
    }
}
