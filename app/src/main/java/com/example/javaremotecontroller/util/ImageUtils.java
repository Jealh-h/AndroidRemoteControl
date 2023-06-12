package com.example.javaremotecontroller.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Log;

public class ImageUtils {
    public static Bitmap drawable2Bitmap(Context ctx, int drawableId) {
        Drawable drawable = ctx.getDrawable(drawableId);

        if (drawable == null
                && drawable.getIntrinsicWidth() > 0
                && drawable.getIntrinsicHeight() > 0) {
            return null;
        }
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        Log.i("Utilities",
                "drawableToBitmap drawable.getIntrinsicWidth()=" + drawable.getIntrinsicWidth()
                        + ",drawable.getIntrinsicHeight()="
                        + drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;

////        Resources resources = ctx.getResources();
//        Drawable drawable = ctx.getDrawable(drawableId);
//
//        if (drawable instanceof BitmapDrawable) {
//            return ((BitmapDrawable) drawable).getBitmap();
//        } else if (drawable instanceof NinePatchDrawable) {
//            // 取 drawable 的长宽
//            int w = drawable.getIntrinsicWidth();
//            int h = drawable.getIntrinsicHeight();
//
//            // 取 drawable 的颜色格式
//            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//                    : Bitmap.Config.RGB_565;
//            // 建立对应 bitmap
//            Bitmap bitmap = Bitmap.createBitmap(w, h, config);
//
//            // 建立对应 bitmap 的画布
//            Canvas canvas = new Canvas(bitmap);
//            drawable.setBounds(0, 0, w, h);
//            // 把 drawable 内容画到画布中
//            drawable.draw(canvas);
//
//            return bitmap;
//        } else {
//            return null;
//        }
    }
}
