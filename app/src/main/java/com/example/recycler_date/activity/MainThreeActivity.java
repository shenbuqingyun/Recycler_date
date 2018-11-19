package com.example.recycler_date.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.recycler_date.R;
import com.example.recycler_date.broadcastreceiver.AlarmThreeReceiver;

import java.util.Calendar;

public class MainThreeActivity extends AppCompatActivity
        implements View.OnClickListener {
    Button btn_set, btn_cancel;
    AlarmManager am;
    PendingIntent pi;
    long time;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        initView();
    }

    private void initView() {
        btn_set = findViewById(R.id.btn_Set);
        btn_cancel = findViewById(R.id.btn_Cancel);
        btn_set.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        initAlarm();
    }

    @Override

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Set:
                setAlarm();
                break;
            case R.id.btn_Cancel:
                cancelAlarm();
                break;
        }

    }

    // 初始化Alarm
    private void initAlarm() {
        pi = PendingIntent.getBroadcast(this, 0, getMsgIntent(), 0);
        time = System.currentTimeMillis();
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    private Intent getMsgIntent() {
    //AlarmThreeReceiver 为广播在下面代码中 注意 这里亦可以利用Intent实例进行组件中通信 发送数据
        Intent intent = new Intent(this, AlarmThreeReceiver.class);
        intent.setAction(AlarmThreeReceiver.BC_ACTION);
        intent.putExtra("msg", "闹钟开启");
        return intent;
    }

    //设置定时执行的任务
    private void setAlarm() {
    //android Api的改变不同版本中设 置有所不同
        if (Build.VERSION.SDK_INT < 19) {
            am.set(AlarmManager.RTC_WAKEUP, getTimeDiff(), pi);
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+2000, pi);
        }
    }

    public long getTimeDiff() {
    //这里设置的是当天的15：55分，注意精确到秒，时间可以自由设置
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 02);
        ca.set(Calendar.MINUTE, 17);
        ca.set(Calendar.SECOND, 0);
        return ca.getTimeInMillis();
    }

    //取消定时任务的执行
    private void cancelAlarm() {
        am.cancel(pi);
    }

}
