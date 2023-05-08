package com.example.javaremotecontroller.util;

import android.content.Context;
import android.util.DisplayMetrics;

import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.model.DeviceCategoryModel;

import java.util.ArrayList;
import java.util.Arrays;

public class util {
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
                    new DeviceCategoryModel(R.drawable.ic_baseline_live_tv_24,"电视", 2),
                    new DeviceCategoryModel(R.drawable.ic_baseline_live_air_condition_24,"空调", 1),
                    new DeviceCategoryModel(R.drawable.ic_baseline_toys_24,"风扇", 7),
                    new DeviceCategoryModel(R.drawable.ic_baseline_surround_sound_24,"音响", 9),
                    new DeviceCategoryModel(R.drawable.ic_baseline_table_lamp_24,"灯", 10),
                    new DeviceCategoryModel(R.drawable.ic_baseline_sweeping_robot_24,"扫地机器人", 12)
            ));
}
