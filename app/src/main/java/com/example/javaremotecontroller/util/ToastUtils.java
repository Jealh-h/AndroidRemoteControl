package com.example.javaremotecontroller.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtils {
    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg,Toast.LENGTH_SHORT).show();
    }

    public static void showToastInUiThread(Context ctx, String msg) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
