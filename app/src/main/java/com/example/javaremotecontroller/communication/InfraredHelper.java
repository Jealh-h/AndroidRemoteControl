package com.example.javaremotecontroller.communication;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.util.Log;
import android.widget.Toast;

//import static androidx.core.content.ContextCompat.getSystemService;

//import static androidx.core.content.ContextCompat.getSystemService;

public class InfraredHelper {
    public boolean sendSignal(Context context) {
        // 获取 ConsumerIrManager 对象
        ConsumerIrManager irManager = (ConsumerIrManager) context.getSystemService(Context.CONSUMER_IR_SERVICE);

        // 如果没有红外发射器，则提示用户
        if (!irManager.hasIrEmitter()) {
          Toast.makeText(context, "您的设备不支持红外发射器！", Toast.LENGTH_LONG).show();
            return false;
        }
        Log.v("紅外發射","1234");
        // 通过 ConsumerIrManager 对象发送红外信号
        int[] pattern = {1000,2000,1000,2000,1000,2000,1000,2000,1000,2000,1000,2000,1000,2000,1000,2000,1000,2000,1000,2000};
        irManager.transmit(38000, pattern);
        return true;
    }
}
