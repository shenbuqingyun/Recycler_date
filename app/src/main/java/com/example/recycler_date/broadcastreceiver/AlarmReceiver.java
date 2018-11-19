package com.example.recycler_date.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.example.recycler_date.R;

/*本例中的接收者： 闹钟响起时 直接在广播接收者中响起音乐*/
public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmThreeReceiver";
    private MediaPlayer mediaPlayer;
    public void onReceive(Context context, Intent intent) {
        if ("android.alarm.demo.action".equals(intent.getAction())) {
            // 第1步中设置的闹铃时间到，这里可以弹出闹铃提示并播放响铃
            Toast.makeText(context, "hello alarm", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onReceive: hello alarm");
            /*
            * 闹钟响起后 执行的操作在这里定义 常见的有跳转一个界面 同时播发音乐 和发出振动
            * MediaPlayer播发类 音乐资源文件存放在raw文件夹下 */
            mediaPlayer = MediaPlayer.create(context,R.raw.record_start);
            mediaPlayer.start();
            // 可以继续设置下一次闹铃时间;
            return;
        }
    }
}
