package com.example.recycler_date.customview;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomViewL extends LinearLayout {

    private static final String TAG = "CustomViewL.TAG";
    private Matrix mMatrix;
    Camera mCamera;
    private int mCurrentItem = 2;
    private int screenWidth;
    private Paint mPaint;

    public static final float ItemScale = 0.1f;

    public CustomViewL(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    public CustomViewL(Context context, AttributeSet attrs, int defStyleAttr,
                       int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    public CustomViewL(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public CustomViewL(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        screenWidth = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getWidth();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout ");
        super.onLayout(changed, l , t, r, b);
        View v = getChildAt(mCurrentItem);
        int delta = getWidth() / 2 - v.getLeft() - v.getWidth()/2;

        for (int i = 0; i < getChildCount(); i++) {
            View v1 = getChildAt(i);
            if (i == mCurrentItem) {
                v1.layout(v1.getLeft() + delta, v1.getTop(),
                        v1.getRight() + delta, v1.getBottom());
                continue;
            }
            float mScale = Math.abs(i - mCurrentItem) * ItemScale;
            int move = (int)(v1.getWidth() * mScale / 2);
            if (i < mCurrentItem) {
                for (int j = i + 1; j < mCurrentItem; j++) {
                    View v2 = getChildAt(j);
                    move += (int) (v2.getWidth() * Math.abs(j - mCurrentItem) * ItemScale);
                }
            } else {
                for (int j = i - 1; j > mCurrentItem; j--) {
                    View v2 = getChildAt(j);
                    move += (int)(v2.getWidth() * Math.abs(j - mCurrentItem) * ItemScale);
                }
                move = -move;
            }
            v1.layout(v1.getLeft() + delta + move, v1.getTop(),
                    v1.getRight() + delta + move, v1.getBottom());
        }
        mRequstLayout = false;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            updateChildItem(canvas,i);
        }
    }

    public void updateChildItem(Canvas canvas,int item) {
//        Log.d(TAG, "updateChildItem");
        View v = getChildAt(item);
        float desi = 1- Math.abs(item - mCurrentItem) * ItemScale;
        ((TextView)v).setScaleX(desi);
        drawChild(canvas, v, getDrawingTime());
        updateTextColor();
    }
    private void updateTextColor() {
        for (int i =0 ; i < getChildCount(); i++) {
            if (i == mCurrentItem) {
                ((TextView)getChildAt(i)).setTextColor(Color.YELLOW);
            } else {
                ((TextView)getChildAt(i)).setTextColor(Color.WHITE);
            }
        }
    }
    boolean scroolToRight = false;

    public void scrollRight() {
        if (mRequstLayout) return;
        if (mCurrentItem > 0) {
            if (mAnimationRunning) {
                if (AnimationRunningCount < 1) {
                    currentItemCopy = mCurrentItem - 1;
                    AnimationRunningCount++;
                    scroolToRight = true;
                }
                return;
            }
            mCurrentItem--;
            startTraAnimation(mCurrentItem,mCurrentItem + 1);
            updateTextColor();
        }
    }

    private int currentItemCopy;
    public void scrollLeft() {
        if (mRequstLayout) return;
        if (mCurrentItem < getChildCount() - 1) {
            if (mAnimationRunning) {
                if (AnimationRunningCount < 1) {
                    currentItemCopy = mCurrentItem + 1;
                    AnimationRunningCount++;
                    scroolToRight = false;
                }
                return;
            }
            mCurrentItem++;
            startTraAnimation(mCurrentItem,mCurrentItem-1);
            updateTextColor();
        }
    }

    public void addIndicator(String[] name) {
        for (int i=0; i< name.length; i++) {
            TextView mTextView = new TextView(getContext());
            mTextView.setText(name[i]);
            mTextView.setTextColor(Color.WHITE);
            mTextView.setLines(1);
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.setMargins(20, 0, 20, 0);
            addView(mTextView,ll);
        }
    }

    class myAnimationListener implements android.view.animation.Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            Log.d(TAG, "onAnimationStart ");
            mAnimationRunning = true;
        }
        @Override
        public void onAnimationEnd(Animation animation) {
            // TODO Auto-generated method stub
            Log.d(TAG, "onAnimationEnd ");

            for (int i= 0; i < getChildCount(); i++) {
                getChildAt(i).clearAnimation();
            }
            mRequstLayout = true;
            requestLayout();
            mAnimationRunning = false;
            if (AnimationRunningCount > 0) {
                CustomViewL.this.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        AnimationRunningCount--;
                        mCurrentItem = currentItemCopy;
                        int lastItem = scroolToRight ? currentItemCopy + 1 : currentItemCopy - 1;
                        startTraAnimation(currentItemCopy,lastItem);
                        updateTextColor();
                    }
                });
            }
        }
        @Override
        public void onAnimationRepeat(Animation animation) {
        }

    }

    private int AnimitionDurationTime = 300;
    private int AnimationRunningCount = 0;
    private boolean mAnimationRunning = false;
    private boolean mRequstLayout = false;
    public void startTraAnimation(int item,int last) {
        Log.d(TAG, "startTraAnimation item = " + item);
        View v = getChildAt(item);
        final int width = v.getWidth();
        final int childCount = getChildCount();
        int traslate = getWidth()/2 - v.getLeft() - width/2;

        int currentItemWidthScale = (int) (width * ItemScale);

        for (int i = 0; i < childCount; i++) {
            int delta = currentItemWidthScale / 2;
            Log.d(TAG, " i = " + i + "  delta before = " + delta);
            if (i < item) {
                delta = -delta;
                for (int j = i; j < item; j++) {
                    int a;
                    if (i == j) {
                        a = (int)(getChildAt(j).getWidth() * ItemScale / 2);
                    } else {
                        a = (int)(getChildAt(j).getWidth() * ItemScale);
                    }
                    delta = item < last ? delta - a : delta + a;
                }
            } else if (i > item){
                for (int j = item + 1; j <= i; j++) {
                    int a;
                    if (j == i) {
                        a = (int)(getChildAt(j).getWidth() * ItemScale / 2);
                    } else {
                        a = (int)(getChildAt(j).getWidth() * ItemScale);
                    }
                    delta = item < last ? delta - a : delta + a;
                }
            } else {
                delta = 0;
            }
            Log.d(TAG, "delta  = " + delta);
            delta += traslate;
            TranslateAnimation translateAni = new TranslateAnimation(0, delta, 0, 0);
            translateAni.setDuration(AnimitionDurationTime);
            translateAni.setFillAfter(true);
            if (i == item) translateAni.setAnimationListener(new myAnimationListener());
            mAnimationRunning = true;
            getChildAt(i).startAnimation(translateAni);
        }
    }
}
