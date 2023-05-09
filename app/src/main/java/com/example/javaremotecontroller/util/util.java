package com.example.javaremotecontroller.util;

import android.content.Context;
import android.util.DisplayMetrics;

import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.model.DeviceCategoryModel;

import java.util.ArrayList;
import java.util.Arrays;

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

    /**
     * px 转换成 dip
     */
    public static int pxToDp(int px, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * dip转化成px
     */
    public static int dpToPx(int dpValue, Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
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
}
