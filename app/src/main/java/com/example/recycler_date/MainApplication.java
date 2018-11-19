package com.example.recycler_date;

import android.app.Application;

import com.example.recycler_date.utils.LogUtils;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * BuildConfig.LOG_DEBUG 获取build.gradle中的自定义的log控制变量
         */
         if (BuildConfig.LOG_DEBUG) {
            LogUtils.isShowLog = true;
        } else {
            LogUtils.isShowLog = false;
        }
    }

}
