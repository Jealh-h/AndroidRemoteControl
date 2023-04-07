package com.example.javaremotecontroller.util;

import android.content.Context;
import android.util.DisplayMetrics;

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
}
