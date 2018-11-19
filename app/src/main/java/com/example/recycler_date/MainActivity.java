package com.example.recycler_date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.example.recycler_date.activity.CoordinateActivity;
import com.example.recycler_date.activity.EndGuideActivity;
import com.example.recycler_date.activity.HorizonActivity;
import com.example.recycler_date.activity.MainFiveActivity;
import com.example.recycler_date.activity.MainFourActivity;
import com.example.recycler_date.activity.MainSixActivity;
import com.example.recycler_date.activity.MainThreeActivity;
import com.example.recycler_date.activity.MainTwoActivity;
import com.example.recycler_date.broadcastreceiver.AlarmReceiver;
import com.example.recycler_date.calendar.DingdingActivity;
import com.example.recycler_date.calendar.SyllabusActivity;
import com.example.recycler_date.utils.DateModel;
import com.example.recycler_date.utils.LogUtils;
import com.example.recycler_date.utils.SharePerferenceModel;
import com.example.recycler_date.utils.StatusBarUtils;
import com.example.recycler_date.utils.ToastUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import tyrantgit.explosionfield.ExplosionField;

/**
 * 作者： cpf
 * 时间： 2018/11
 * 说明： 为闹钟开发而做的测试
 */
public class MainActivity extends AppCompatActivity implements Handler.Callback, SensorEventListener {
    private static final String TAG = "MainActivity";
    private EditText edit;
    private Button saveBtn, deleteBtn, alarmBtn, datePickerBtn, timePickerBtn,
            blog1Btn, blog2Btn, blog3Btn, blog4Btn,calendar_superBtn,calendar_xiaomiBtn,
            horizonBtn,iOS_scrollBtn,shapeShadowBtn,destoryBtn,end_guideBtn;
    /*测试近距离感应器*/
    private TextView tv, tv_clock;
    private SensorManager sensorManager;
    private PowerManager localPowerManager = null;//电源管理对象
    private PowerManager.WakeLock localWakeLock = null;//电源锁
    /*测试时间流逝效果*/
    private SharePerferenceModel model;
    private Timer timer;
    private Handler handler;
    private final static int UPDATE_TIME = 100;
    private static final int INTERVAL = 1000 * 60 * 60 * 24;// 24h
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtils.StatusBarLightMode(MainActivity.this); //将状态栏文字图标设为黑色
        if (MarshmallowPermissions.checkPermissionForCamera(MainActivity.this)) {
            MarshmallowPermissions.requestPermissionForCamera(MainActivity.this);
        }
        /*测试Bitmap文件存储和删除 File存储到指定位置*/
        edit = findViewById(R.id.edit);
        saveBtn = findViewById(R.id.save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImageToGallery(MainActivity.this, "SI1");
            }
        });
        deleteBtn = findViewById(R.id.delete);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appDir != null) {
                    deleteDirWihtFile(appDir);
                }
            }
        });

        /*测试近距离感应器*/
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/ds_digi.ttf");
        tv = findViewById(R.id.text_sensor);
        tv.setTypeface(typeFace);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        localPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        localWakeLock = this.localPowerManager.newWakeLock(32, "super");// 第一个参数为电源锁级别，第二个是日志tag

        handler = new Handler(this);
        tv_clock = findViewById(R.id.move_clock);
        tv_clock.setTypeface(typeFace);
        model = new SharePerferenceModel();
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(UPDATE_TIME);
            }
        };
        timer.schedule(timerTask, 1000, 1000);

        /*测试闹钟*/
        final long time = System.currentTimeMillis();
        //  setAlarmTime(MainActivity.this, SystemClock.elapsedRealtime()+5000);
        setAlarmTime(MainActivity.this, time + 5000);
        alarmBtn = findViewById(R.id.alarm);
        alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                am.setRepeating(AlarmManager.RTC_WAKEUP, time,
                        5 * 1000, sender); //间隔5秒再次执行闹钟程序 会不断地执行 退出应用也会执行
            }
        });

        setNineClockAlarmTime(MainActivity.this);

        /*测试 DatePickerDialog 和 TimePickerDialog 实测会随着版本有变化*/
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        datePickerBtn = findViewById(R.id.date_picker);
        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*DatePickerDialog 构造方法接收五个参数①上下文②接口实例③year④month⑤day*/
                android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(MainActivity.this, new android.app.DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker datePicker, int chooseY, int chooseM, int chooseD) {
                        //设置储存数据的年月日
//                        CalendarLab.get(getActivity()).setCalendarYMD(chooseY,chooseM,chooseD);
//                        mYMDText.setText(chooseY + "年"+chooseM + "月" +chooseD);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });
        timePickerBtn = findViewById(R.id.time_picker);
        timePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*TimePickerDialog构造方法接收四个构造参数①上下文②接口实例③hour④minute*/
                android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(MainActivity.this,
                        new android.app.TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(android.widget.TimePicker timePicker, int chooseH, int chooseM) {
                                //这里的chooseH和chooseM就是选中的小时和分钟
//                        CalendarLab.get(getActivity()).setCalendarHM(chooseH,chooseM);
//                        String s1 = "";
//                        String s2 = "";
//                        if (chooseH<10){
//                            s1 = "0";
//                        }
//                        if (chooseM<10){
//                            s2 = "0";
//                        }
//                        mHMText.setText(s1 + chooseH + ":" + s2 + chooseM);
                            }
                        }, hour, minute, true);  //最后一个参数表示是否使用24小时制
                timePickerDialog.show();
            }
        });
        blog1Btn = findViewById(R.id.test_blog1);
        blog1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainTwoActivity.class);
                startActivity(intent);
            }
        });
        blog2Btn = findViewById(R.id.test_blog2);
        blog2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainThreeActivity.class);
                startActivity(intent);
            }
        });
        blog3Btn = findViewById(R.id.test_blog3);
        blog3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainFourActivity.class);
                startActivity(intent);
            }
        });
        blog4Btn = findViewById(R.id.test_blog4);
        blog4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainFiveActivity.class);
                startActivity(intent);
            }
        });
        calendar_superBtn = findViewById(R.id.calendar_super);
        calendar_superBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SyllabusActivity.class);
                startActivity(intent);
            }
        });
        calendar_xiaomiBtn = findViewById(R.id.calendar_xiaomi);
        calendar_xiaomiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DingdingActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "测试toast", Toast.LENGTH_SHORT).show();
            }
        });
        horizonBtn = findViewById(R.id.horizon);
        horizonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HorizonActivity.class);
                startActivity(intent);
                ToastUtils.showToast(MainActivity.this,"测试toast");
            }
        });
        iOS_scrollBtn = findViewById(R.id.iOS_scroll);
        iOS_scrollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainSixActivity.class);
                startActivity(intent);
                ToastUtils.showToast(MainActivity.this,"测试toast");
            }
        });
        shapeShadowBtn = findViewById(R.id.shape_shadow);
        shapeShadowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CoordinateActivity.class);
                startActivity(intent);
                ToastUtils.showToast(MainActivity.this,"测试toast");
            }
        });
        final RippleView rippleView = (RippleView) findViewById(R.id.more);
        rippleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: onRippleViewClick
            }
        });
        imageView = findViewById(R.id.kill);
        destoryBtn = findViewById(R.id.destory);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExplosionField explosionField = ExplosionField.attach2Window(MainActivity.this);
                explosionField.explode(destoryBtn);
            }
        });
        destoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExplosionField explosionField = ExplosionField.attach2Window(MainActivity.this);
                explosionField.explode(imageView);
            }
        });
        end_guideBtn = findViewById(R.id.end_guide);
        end_guideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EndGuideActivity.class);
                startActivity(intent);
            }
        });
        getStatusBarHeight(); // 小米4状态栏 60像素
        getNavigationBarHeight(); //小米4导航栏 130像素 小米4没有内部虚拟按键为何 还有导航栏
    }

    public float getStatusBarHeight() {
        float result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimension(resourceId);
        }
        Log.d(TAG, "getStatusBarHeight: getStatusBarHeight()" + result);
        return result;
    }   //返回值就是状态栏的高度,得到的值是像素

    public float getNavigationBarHeight() {
        float result = 0;
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimension(resourceId);
        }
        Log.d(TAG, "getNavigationBarHeight: NavigationBarHeight" + result);
        return result;
    }   //返回值就是导航栏的高度,得到的值是像素

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String inputText = edit.getText().toString();
        save(inputText);
    }

    private void save(String string) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(string);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*测试Bitmap文件存储和删除 File存储到指定位置*/
    private File file;
    private File appDir;

    public void saveImageToGallery(Context context, String fileName) {
        // 保存图片
        appDir = new File(context.getExternalFilesDir(""), "测试文件夹");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            Bitmap btImage = BitmapFactory.decodeResource(getResources(), R.drawable.filter_lut_portrait_m10);
            btImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Log.d(TAG, "saveImageToGallery_Path: " + file.getAbsolutePath());
            Toast.makeText(context, "save success", Toast.LENGTH_SHORT).show();
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteDirWihtFile(File dir) {
        Log.d(TAG, "deleteDirWihtFile: " + dir.getPath());
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.getName().equals("SI1"))
                file.delete(); // 删除所有文件
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
        }
    }

    /*测试近距离感应器*/
    @Override
    protected void onResume() {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_UI);
        super.onResume();
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    /*
     * 实现SensorEventListener接口 需要重写两个方法onSensorChanged()和onAccuracyChanged()
     * 不要随便使用传感器 耗电
     * */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
/*        float[] values = sensorEvent.values;
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_PROXIMITY:
                if (values[0] == 0.0){
                    //贴近手机
                    Log.d(TAG, "onSensorChanged: close to face");
                    if (localWakeLock.isHeld()){
                        return;
                    } else {
                        localWakeLock.acquire(); //申请设备电源锁
                    }
                } else { //远离手机
                    Log.d(TAG, "onSensorChanged: depart from face");
                    if (localWakeLock.isHeld()){
                        return;
                    } else {
                        localWakeLock.setReferenceCounted(false);
                        localWakeLock.release(); //释放设备电源锁
                    }
                    break;
                }
        }*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case UPDATE_TIME:
                updateTime();
                break;
        }
        return true;
    }

    /*需要做一个计时器 才能不断更新时间要不能就只有一次时间*/
    private void updateTime() {
        DateModel date = new DateModel();
        String timeString = model.isDisplaySecond() ? date.getTimeString() : date.getShortTimeString();
        if (timeString.startsWith(" "))
            timeString = timeString.substring(1, timeString.length());
        LogUtils.d(this,"updateTime: " + timeString);
        tv_clock.setText(timeString);
    }

    /*测试实现闹钟 时间到时弹出Toast*/
    private AlarmManager am;
    private PendingIntent sender;

    private void setAlarmTime(Context context, long triggerAtMillis) {
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("android.alarm.demo.action");
        sender = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        //闹铃间隔， 这里设为1分钟闹一次，在第2步我们将每隔1分钟收到一次广播
        //int interval = 60 * 1000;
        //am.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, interval, sender); interval 表示执行的时间间隔 单位毫秒
        am.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, sender);
        /*set()方法接收三个参数
        *type 四种类型 区分的是时间标准和是否在睡眠状态下唤醒设备。
        * ①ELAPSED_REALTIME-3 -睡眠状态 无法唤醒
        * ②ELAPSED_REALTIME_WAKEUP-2  -能唤醒系统
        * 如果使用ELAPSED_REALTIME_WAKEUP类型 应该调用SystemClock.elapsedRealtime()获取相对时间在加上你设定的延迟时间。
        * alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+5000, sender);
        * ③RTC-1  -睡眠状态 无法唤醒
        * ④RTC_WAKEUP-0  -能唤醒系统 选用此参数，除了有定时器的功能外，还会发出警报声（例如，响铃、震动）
        * 如果使用RTC_WAKEUP类型 应该调用System.currentTimeMillis()获取从1970.1.1号以来的绝对时间在加上你设定的延迟时间。
        * alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5000, sender);
        *triggerAtMillis long型数据 注定存在转化 第一次运行时要等待的时间，也就是执行延迟时间，单位是毫秒
        *PendingIntent getBroadcast()接收四个参数获取实例对象 表示到时间后要执行的操作
        public void set(int type, long triggerAtMillis, PendingIntent operation) {
                    throw new RuntimeException("Stub!");
                }*/

        //        am.cancel(sender);  取消闹钟 注意要是同一个PendingIntent实例
    }

    /*设置一个每晚午夜都唤醒的闹钟
     * 设置为固定时间需要借助Calendar日期类
     * */

    private void setNineClockAlarmTime(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Schedule the alarm!
        AlarmManager am = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 15);
        calendar.set(Calendar.SECOND, 10);
        calendar.set(Calendar.MILLISECOND, 0);

        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                INTERVAL, sender);
    }

    /*难点——设置一个定时响起的闹钟 数据库存储 服务 通知
     * 由用户进行定制 DatePickerDialog TimePickerDialog
     * 选择时间 -> 确定后启动服务
     * -> 服务开启后设置AlarmManager -> AlarmManager设置到点发送一条广播
     * -> 广播接收器接收到广播后发送一条通知 -> 通知发出铃声、震动、文字或图片通知*/


}

