package com.example.recycler_date.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.example.recycler_date.R;

/*初级秒表 使用系统类Chronometer 计时最小单位是秒*/
public class MainFourActivity extends AppCompatActivity implements View.OnClickListener
        ,Chronometer.OnChronometerTickListener{
    private Chronometer chronometer;
    private Button btn_start,btn_stop,btn_base,btn_format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);
        initView();
    }

    private void initView() {
        chronometer = findViewById(R.id.chronometer);
        btn_start = findViewById(R.id.btnStart);
        btn_stop = findViewById(R.id.btnStop);
        btn_base = findViewById(R.id.btnReset);
        btn_format = findViewById(R.id.btn_format);

        chronometer.setOnChronometerTickListener(this);
        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_base.setOnClickListener(this); //重置
        btn_format.setOnClickListener(this); //格式化

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnStart:
                chronometer.start();// 开始计时
                break;
            case R.id.btnStop:
                chronometer.stop();// 停止计时
                break;
            case R.id.btnReset:
                chronometer.setBase(SystemClock.elapsedRealtime());// 复位
                break;
            case R.id.btn_format:
                chronometer.setFormat("T：%s");// 更改时间显示格式
                break;
        }
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        String time = chronometer.getText().toString();
        if(time.equals("00:00")){
            Toast.makeText(MainFourActivity.this,"时间到了~", Toast.LENGTH_SHORT).show();
        }
    }
}
