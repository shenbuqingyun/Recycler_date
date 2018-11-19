package com.example.recycler_date.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

import com.example.recycler_date.R;
import com.example.recycler_date.customview.CustomViewL;

public class MainSixActivity extends Activity implements OnTouchListener {

    private static final String TAG = "MainActivity.TAG";
    CustomViewL mCustomViewL;
    String[] name = new String[]{"延时摄影", "慢动作", "视频", "拍照", "正方形", "全景"};

    GestureDetector mGestureDetector;
    RelativeLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_six);
        mCustomViewL = (CustomViewL) findViewById(R.id.mCustomView);
        rootView = (RelativeLayout) findViewById(R.id.ViewRoot);
        rootView.setOnTouchListener(this);
        mCustomViewL.getParent();
        mCustomViewL.addIndicator(name);
        mGestureDetector = new GestureDetector(this, new myGestureDetectorLis());
    }

    class myGestureDetectorLis implements GestureDetector.OnGestureListener {

        private static final int degreeLimit = 30;
        private static final int distanceLimit = 15;

        private boolean isScroll = false;

        @Override
        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            Log.d(TAG, "myGestureDetectorLis onDown");
            isScroll = false;
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                float distanceY) {
            // TODO Auto-generated method stub
            if (isScroll) return false;
            double degree = Math.atan(Math.abs(e2.getY() - e1.getY()) / Math.abs(e2.getX() - e1.getX())) * 180 / Math.PI;
            float delta = e2.getX() - e1.getX();
            if (delta > distanceLimit && degree < degreeLimit) {
                Log.d(TAG, "向右滑");
                isScroll = true;
                mCustomViewL.scrollRight();
            } else if (delta < -distanceLimit && degree < degreeLimit) {
                Log.d(TAG, "向左滑");
                isScroll = true;
                mCustomViewL.scrollLeft();
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            // TODO Auto-generated method stub
            return false;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return mGestureDetector.onTouchEvent(event);
    }


}
