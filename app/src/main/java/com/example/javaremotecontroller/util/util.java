package com.example.javaremotecontroller.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.model.DeviceCategoryModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class util {
    /**
     * 设备种类 id
     */
    public static final int CATEGORY_ID_AIR_CONDITION = 1;
    public static final int CATEGORY_ID_TV= 2;
    public static final int CATEGORY_ID_FAN = 7;
    public static final int CATEGORY_ID_SOUND = 9;
    public static final int CATEGORY_ID_LAMP = 10;
    public static final int CATEGORY_ID_SWEEP = 12;

    /**
     * intent 传值的 key
     */
    public static String DASHBOARD_TO_BRAND_LIST_KEY = "CATEGORY_MODEL";
    public static String BRAND_LIST_TO_OPERATION_PANEL_KEY = "BRAND_MODEL";
    public static String BLUE_TOOTH_DEVICE_CARRY_DATA_KEY = "ADDRESS_BLUE_DEVICE";

    /**
     * px 转换成 dip
     */
    public static int pxToDp(int px, Context context) {
        try {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        }catch (Exception e) {
            return 0;
        }
    }

    /**
     * dip转化成px
     */
    public static int dpToPx(int dpValue, Context context) {
        try {
            float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }catch (Exception e) {
            return 0;
        }
    }

    /**
     * 红外遥控设备种类
     */
    public static ArrayList<DeviceCategoryModel> deviceCategory = new ArrayList(
            Arrays.asList(
                    new DeviceCategoryModel(R.drawable.ic_baseline_live_tv_24,"电视", CATEGORY_ID_TV),
                    new DeviceCategoryModel(R.drawable.ic_baseline_live_air_condition_24,"空调", CATEGORY_ID_AIR_CONDITION),
                    new DeviceCategoryModel(R.drawable.ic_baseline_toys_24,"风扇", CATEGORY_ID_FAN),
                    new DeviceCategoryModel(R.drawable.ic_baseline_surround_sound_24,"音响", CATEGORY_ID_SOUND),
                    new DeviceCategoryModel(R.drawable.ic_baseline_table_lamp_24,"灯", CATEGORY_ID_LAMP),
                    new DeviceCategoryModel(R.drawable.ic_baseline_sweeping_robot_24,"扫地机器人", CATEGORY_ID_SWEEP)
            ));

    public static String int2Decimal(long address) {
        long a = address & 0xFF;
        long b = (address >> 8) & 0xFF;
        long c = (address >> 16) & 0xFF;
        long d = (address >> 24) & 0xFF;
//        return (d + "." + c + "." + b + "." + a);
        return (a + "." + b + "." + c + "." + d);
    }

    public static boolean LocationStateCheck(Context context) {
        // 检查定位是否已打开
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return isLocationEnabled;
    }

    public static void openLocationSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 沉浸式状态栏，传入 Activity
     * @param context
     */
    public static void immersionStatusBar(Activity context) {
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        context.getWindow().setStatusBarColor(Color.TRANSPARENT);
        context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        context.getWindow()
                .getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        // 安卓 6.0+
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 状态栏的图标显示为黑色
            context.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b)); // 将每个字节转换为两位十六进制数
        }
        return sb.toString();
    }

    public static boolean isNull(String s) {
        return s.trim() == "" || s == null;
    }

    public static TimerTask startDelayTask(final Runnable runnable, int delay) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runnable.run();
                this.cancel();
            }
        };
        timer.schedule(task, delay);
        return task;
    }
}
