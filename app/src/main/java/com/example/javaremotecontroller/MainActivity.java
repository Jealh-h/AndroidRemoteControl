package com.example.javaremotecontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.javaremotecontroller.adapter.FragmentPageAdapter;
import com.example.javaremotecontroller.adapter.ViewPagerAdapter;
import com.example.javaremotecontroller.fragments.BlankFragment;

import java.util.ArrayList;

/**
 * 底部导航按钮枚举
 */
enum BottomNavMenu {
    HOME,
    DASHBOARD,
    PERSON
}

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager2 viewPager;
    private FragmentPageAdapter viewPagerAdapter;
    private ImageView bottomNavHome, bottomNavDashboard, bottomNavMine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPage();
        initBottomNav();
    }

    private void initPage() {
        viewPager = findViewById(R.id.view_pager);
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(BlankFragment.newInstance("p1"));
        fragmentList.add(BlankFragment.newInstance("p3"));
        fragmentList.add(BlankFragment.newInstance("p5"));
        // 定义 viewpager adapter
        viewPagerAdapter = new FragmentPageAdapter(getSupportFragmentManager(), getLifecycle(), fragmentList);
        // 设置 adapter
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                Log.e("TAG", "onPageScrolled:" + position );
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
//                changeTab(position);
                Log.e("TAG", "onPageSelected:" + position );
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                Log.e("TAG", "onPageScrollStateChanged:" + state );
            }
        });
        Log.d("MainActivity", "Hello world");
    }

    private void initBottomNav() {
        bottomNavHome = findViewById(R.id.bottom_nav_home_btn);
        bottomNavDashboard = findViewById(R.id.bottom_nav_dashboard_btn);
        bottomNavMine = findViewById(R.id.bottom_nav_mine_btn);
        // set bottom btn click event listener
        bottomNavHome.setOnClickListener(this);
        bottomNavDashboard.setOnClickListener(this);
        bottomNavMine.setOnClickListener(this);
        // set initial active bottom btn
        bottomNavHome.setSelected(true);
    }

    private void changeTab(int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        clearBottomNavBtnState();
        switch (v.getId()) {
            case R.id.bottom_nav_home_btn:
                bottomNavHome.setSelected(true);
                viewPager.setCurrentItem(BottomNavMenu.HOME.ordinal());
                break;
            case R.id.bottom_nav_dashboard_btn:
                bottomNavDashboard.setSelected(true);
                viewPager.setCurrentItem(BottomNavMenu.DASHBOARD.ordinal());
                break;
            case R.id.bottom_nav_mine_btn:
                bottomNavMine.setSelected(true);
                viewPager.setCurrentItem(BottomNavMenu.PERSON.ordinal());
                break;
        }
    }

    private void clearBottomNavBtnState() {
        bottomNavMine.setSelected(false);
        bottomNavHome.setSelected(false);
        bottomNavDashboard.setSelected(false);
    }
}