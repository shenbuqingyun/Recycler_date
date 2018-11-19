package com.example.recycler_date.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.recycler_date.R;

public class RingActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring);
        //播放音乐
        mediaPlayer = MediaPlayer.create(this, R.raw.record_start);
        mediaPlayer.start();
        //在闹钟响起之后开启通知栏 闹钟响后开启通知栏 也可通知下一次响起的时间
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);//默认的通知手机声音
        builder.setContentTitle("闹钟响了!");
        builder.setSmallIcon(android.R.drawable.ic_lock_idle_alarm); //系统自带图标
        builder.setContentText("记得吃早餐噢!");

        Intent intent=new Intent();
        intent.setAction("com.zking.g158231_android28.RING");
        PendingIntent pendingIntent= PendingIntent.getBroadcast(RingActivity.this,0x105,intent,0);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        //发送通知
        notificationManager.notify(0x104,notification); //notify()接收两个参数 第一个是id 用来标识notification的
    }

    public void close(View view){
        mediaPlayer.stop();
        finish();
    }
}
