package com.example.recycler_date.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.recycler_date.R;
import com.example.recycler_date.utils.UITools;

import java.util.ArrayList;
import java.util.Collections;

public class HorizonActivity extends AppCompatActivity {

    private HorizontalScrollView horizontalScrollView;
    private LinearLayout container;
    private String cities[] = new String[]{"London", "Bangkok", "Paris", "Dubai", "Istanbul", "New York",
            "Singapore", "Kuala Lumpur"};
    private ArrayList<String> data = new ArrayList<>();
    private AnimatorSet mAnimatorSetLeft, mAnimatorSetRight;
    private ObjectAnimator mItemsliding;
    private ObjectAnimator mItemsAlpha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centerlockhorizontalscrollview);

        bindData();
        setUIRef();
        bindHZSWData();
    }

    //将集合中的数据绑定到HorizontalScrollView上
    private void bindHZSWData() {    //为布局中textview设置好相关属性
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMargins(20, 10, 20, 10);

        for (int i = 0; i < data.size(); i++) {
            TextView textView = new TextView(this);
            textView.setText(data.get(i));
            textView.setTextColor(Color.WHITE);
            textView.setLayoutParams(layoutParams);
            container.addView(textView);
            container.invalidate();
        }
    }

    //初始化布局中的控件
    private void setUIRef() {
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        UITools.elasticPadding(horizontalScrollView, 300);
        container = (LinearLayout) findViewById(R.id.horizontalScrollViewItemContainer);

        mAnimatorSetLeft = new AnimatorSet();
        mAnimatorSetRight = new AnimatorSet();
        mItemsliding = ObjectAnimator.ofFloat(container,"translationX",0,-600);
        mItemsAlpha = ObjectAnimator.ofFloat(container,"alpha",0,0);
        mAnimatorSetLeft.setDuration(0);
        mAnimatorSetLeft.play(mItemsliding).with(mItemsAlpha);
        mAnimatorSetLeft.start();
        mAnimatorSetLeft.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mItemsliding = ObjectAnimator.ofFloat(container,"translationX",-600,0);
                mItemsAlpha = ObjectAnimator.ofFloat(container,"alpha",1,1);
                mAnimatorSetRight.setStartDelay(200);
                mAnimatorSetRight.setDuration(1000);
                mAnimatorSetRight.play(mItemsliding).with(mItemsAlpha);
                mAnimatorSetRight.start();
            }
        });

    }

    //将字符串数组与集合绑定起来
    private void bindData() {
        //add all cities to our ArrayList
        Collections.addAll(data, cities);
    }

}

