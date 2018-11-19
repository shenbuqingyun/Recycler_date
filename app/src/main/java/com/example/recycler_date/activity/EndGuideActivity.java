package com.example.recycler_date.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.recycler_date.R;
import com.example.recycler_date.fragment.ImageFragment;
import com.example.recycler_date.fragment.SimpleFragment;

import java.util.ArrayList;
import java.util.Random;

/**
 * 作者    cpf
 * 时间    2018/11/16 17:30
 * 文件    Recycler_date
 * 描述
 */
public class EndGuideActivity extends AppCompatActivity {
    private ViewPager mVpHome;
    private BottomNavigationBar mBottomNavigationBar;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_guide);
        hideStatusBar(); //隐藏状态栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide(); //隐藏标题栏
        }
        mVpHome = (ViewPager) findViewById(R.id.vp_home);
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_favorite, "One"))
                .addItem(new BottomNavigationItem(R.drawable.ic_gavel, "Two"))
                .addItem(new BottomNavigationItem(R.drawable.ic_grade, "Three"))
                .addItem(new BottomNavigationItem(R.drawable.ic_group_work, "Four"))
                .addItem(new BottomNavigationItem(R.drawable.ic_favorite, "Five"))
                .addItem(new BottomNavigationItem(R.drawable.ic_gavel, "Six"))
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mVpHome.setCurrentItem(position); //下面动 上面跟着动走的是这个方法
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });

        mFragmentList.add(new ImageFragment());
        mFragmentList.add(new SimpleFragment());
        mFragmentList.add(new SimpleFragment());
        mFragmentList.add(new SimpleFragment());
        mFragmentList.add(new SimpleFragment());
        mFragmentList.add(new SimpleFragment());

        mVpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottomNavigationBar.selectTab(position);//上面移动 下面跟着动走的是这个方法
                switch (position) {
                    case 0:
                        break;
                    default:
                        //随机取颜色
                        Random random = new Random();
                        int color = 0xff000000 | random.nextInt(0xffffff);
                        if (mFragmentList.get(position) instanceof SimpleFragment) {
                            ((SimpleFragment) mFragmentList.get(position)).setTvTitleBackgroundColor(color);
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mVpHome.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

        });
    }

    /*
    * 隐藏状态栏
    * */
    public void hideStatusBar() {
        if (Build.VERSION.SDK_INT < 30) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
