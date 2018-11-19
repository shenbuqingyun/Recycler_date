package com.example.recycler_date.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.recycler_date.activity.RingActivity;

/*本例中的接收者： 闹钟响起时进行界面跳转 跳转到新界面 播放音乐 同时展开通知栏*/
public class Ring extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.zking.g158231_android28.RING".equals(intent.getAction())){
            //闹钟响之后 一般是跳转到新的页面 所以音乐播发MediaPlayer也是在新页面设置 一般不在广播中设置
            Intent intent1=new Intent(context,RingActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }
}

