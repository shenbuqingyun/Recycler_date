package com.example.recycler_date.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TimePicker;

import com.example.recycler_date.R;

import java.util.Calendar;

public class MainTwoActivity extends AppCompatActivity {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    /*实现用户设置好某一个时间点响起闹钟 需要结合DatePickerDialog和TimaPickerDialog 来选定时间*/
    public void setAlarmone(View view) {
        Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);
                Intent intent = new Intent();
                intent.setAction("com.zking.g158231_android28.RING");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainTwoActivity.this, 0x100, intent, 0);
                //设置好闹钟后 只响一次
                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    public void setAlarmcycle(View view) {
        Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);
                Intent intent = new Intent();
                intent.setAction("com.zking.g158231_android28.RING");
                pendingIntent = PendingIntent.getBroadcast(MainTwoActivity.this, 0x102, intent, 0);
                //设置好闹钟后 每5秒响一次闹钟
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),5000, pendingIntent);
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    public void mcycle(View view){
        alarmManager.cancel(pendingIntent);
    }
}

