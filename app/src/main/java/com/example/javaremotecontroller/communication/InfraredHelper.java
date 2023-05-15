package com.example.javaremotecontroller.communication;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;

public class InfraredHelper {
    private static String TAG = "红外";

    public static boolean sendSignal(Context context, int[] pattern) {
        // 获取 ConsumerIrManager 对象
        ConsumerIrManager irManager = (ConsumerIrManager) context.getSystemService(Context.CONSUMER_IR_SERVICE);

        // 如果没有红外发射器，则提示用户
        if (!irManager.hasIrEmitter()) {
          Toast.makeText(context, "您的设备不支持红外发射器！", Toast.LENGTH_LONG).show();
            return false;
        }
        Log.e(TAG, Arrays.toString(pattern));
        // 通过 ConsumerIrManager 对象发送红外信号
        irManager.transmit(38000, pattern);
        irManager.transmit(56000, pattern);
        return true;
    }
}
