package com.example.javaremotecontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.javaremotecontroller.adapter.FragmentPageAdapter;
import com.example.javaremotecontroller.communication.BluetoothHidHelper;
import com.example.javaremotecontroller.fragments.BlankFragment;
import com.example.javaremotecontroller.ui.dashboard.DashboardFragment;
import com.example.javaremotecontroller.ui.home.HomeFragment;
import com.example.javaremotecontroller.ui.notifications.NotificationsFragment;
import com.example.javaremotecontroller.util.IRApplication;
import com.example.javaremotecontroller.util.SqliteHelper;
import com.example.javaremotecontroller.util.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 底部导航按钮枚举
 */
enum BottomNavMenu {
    HOME(0),
    DASHBOARD(1),
    PERSON(2);
    private final int value;
    private BottomNavMenu(int value) {
        this.value = value;
    }
    public int valueOf() {
        return this.value;
    }
}

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager2 viewPager;
    private FragmentPageAdapter viewPagerAdapter;
    private ImageView bottomNavHome, bottomNavDashboard, bottomNavMine;
    private LinearLayout bottomNavHomeWrapper, bottomNavDashboardWrapper, bottomNavMineWrapper;
    private CardView indicator;
    private IRApplication mApp;
    private List<Integer> offsetArray = new ArrayList<>();
    private Integer currentIndex = 0;
    private Integer innerPadding  = 8;
    private static String TAG = "DEBUG";
    private SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPage();
        initBottomNav();
        util.immersionStatusBar(this);
        sqliteHelper = new SqliteHelper(this, "REMOTE_CONTROLER", 1);
        new BluetoothHidHelper(this);
        mApp = (IRApplication) Objects.requireNonNull(this).getApplication();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            offsetArray.add(innerPadding);
            offsetArray.add(bottomNavHomeWrapper.getWidth());
            offsetArray.add(bottomNavHomeWrapper.getWidth() +
                    bottomNavDashboardWrapper.getWidth());
            CardView.LayoutParams lp = (CardView.LayoutParams) indicator.getLayoutParams();
            lp.width = bottomNavHomeWrapper.getWidth()  - util.dpToPx(innerPadding * 2, this);
            Log.e(TAG, "onWindowFocusChanged: " + bottomNavHomeWrapper.getWidth() + lp.width);
            indicator.setLayoutParams(lp);
        }
    }

    private void initPage() {
        viewPager = findViewById(R.id.view_pager);
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new DashboardFragment());
        fragmentList.add(new NotificationsFragment());
        // 定义 viewpager adapter
        viewPagerAdapter = new FragmentPageAdapter(getSupportFragmentManager(), getLifecycle(), fragmentList);
        // 设置 adapter
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                changeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private void initBottomNav() {
        indicator = findViewById(R.id.bottom_nav_indicator);
        bottomNavHome = findViewById(R.id.bottom_nav_home_btn);
        bottomNavDashboard = findViewById(R.id.bottom_nav_dashboard_btn);
        bottomNavMine = findViewById(R.id.bottom_nav_mine_btn);

        bottomNavHomeWrapper = findViewById(R.id.bottom_nav_home_wrapper);
        bottomNavDashboardWrapper = findViewById(R.id.bottom_nav_dashboard_wrapper);
        bottomNavMineWrapper = findViewById(R.id.bottom_nav_personal_wrapper);

        // set bottom btn click event listener
        bottomNavHomeWrapper.setOnClickListener(this);
        bottomNavDashboardWrapper.setOnClickListener(this);
        bottomNavMineWrapper.setOnClickListener(this);

        // set initial active bottom btn
        bottomNavHome.setSelected(true);
    }

    private void changeTab(int position) {
        clearBottomNavBtnState();
        switch (position) {
            case R.id.bottom_nav_home_wrapper:
            case 0:
                startIndicatorTranslate(0);
                bottomNavHome.setSelected(true);
                viewPager.setCurrentItem(BottomNavMenu.HOME.valueOf());
                break;
            case R.id.bottom_nav_dashboard_wrapper:
            case 1:
                startIndicatorTranslate(1);
                bottomNavDashboard.setSelected(true);
                viewPager.setCurrentItem(BottomNavMenu.DASHBOARD.valueOf());
                break;
            case R.id.bottom_nav_personal_wrapper:
            case 2:
                startIndicatorTranslate(2);
                bottomNavMine.setSelected(true);
                viewPager.setCurrentItem(BottomNavMenu.PERSON.valueOf());
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.e("MAIN_ACTIVITY", "onResume: ");
//        if(IRApplication.mUserApp==null) {
//            try {
//                mApp.signIn();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void onClick(View v) {
        // 会触发 viewPager 的 onPageSelected
        switch (v.getId()){
            case R.id.bottom_nav_home_wrapper:
                viewPager.setCurrentItem(BottomNavMenu.HOME.valueOf());
                break;
            case R.id.bottom_nav_dashboard_wrapper:
                viewPager.setCurrentItem(BottomNavMenu.DASHBOARD.valueOf());
                break;
            case R.id.bottom_nav_personal_wrapper:
                viewPager.setCurrentItem(BottomNavMenu.PERSON.valueOf());
                break;
            default:
                break;
        }
    }

    private void clearBottomNavBtnState() {
        bottomNavMine.setSelected(false);
        bottomNavHome.setSelected(false);
        bottomNavDashboard.setSelected(false);
    }

    private void startIndicatorTranslate(int nextIndex) {
        if(offsetArray.size() == 0){
            return;
        }
        TranslateAnimation animation = new TranslateAnimation(offsetArray.get(currentIndex),
                offsetArray.get(nextIndex),0,0);
        animation.setFillAfter(true);
        animation.setDuration(200);
        indicator.startAnimation(animation);
        currentIndex = nextIndex;
    }
}