package com.dassault.medidatanotificationdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class PreferenceUtils {
    public static void setAlarmId(Context context, String formId, long alarmId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String key = context.getString(R.string.alarm_id_key);
        String alarmData = storeDataInMap(context, formId, alarmId);
        prefs.edit().putString(key, alarmData).apply();
    }

    public static ArrayList<Long> getAlarmIdList(Context context, String formId) {
        ArrayList<Long> alarmIdList = new ArrayList<>();
        String data = getAlarmData(context);
        if (!TextUtils.isEmpty(data)) {
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, ArrayList<Long>>>() {
            }.getType();
            HashMap<String, ArrayList<Long>> alarmMap = gson.fromJson(data, type);
            if (alarmMap.containsKey(formId)) {
                alarmIdList = alarmMap.get(formId);
            }
        }

        return alarmIdList;
    }

    private static String getAlarmData(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.alarm_id_key), "");
    }

    private static String storeDataInMap(Context context, String formId, long alarmId) {
        String dataToStore = "";
        String data = getAlarmData(context);
        Gson gson = new Gson();
        if (TextUtils.isEmpty(data)) {
            HashMap<String, ArrayList<Long>> map = new HashMap();
            ArrayList<Long> alarmList = new ArrayList<>();
            alarmList.add(alarmId);
            map.put(formId, alarmList);
            dataToStore = gson.toJson(map);

        } else {
            Type type = new TypeToken<HashMap<String, ArrayList<Long>>>() {
            }.getType();
            HashMap<String, ArrayList<Long>> alarmMap = gson.fromJson(data, type);
            if (alarmMap.containsKey(formId)) {
                ArrayList<Long> list = alarmMap.get(formId);
                list.add(alarmId);
                alarmMap.put(formId, list);
            } else {
                ArrayList<Long> alarmDataList = new ArrayList<>();
                alarmDataList.add(alarmId);
                alarmMap.put(formId, alarmDataList);

            }
            dataToStore = gson.toJson(alarmMap);
        }
        return dataToStore;
    }


}
