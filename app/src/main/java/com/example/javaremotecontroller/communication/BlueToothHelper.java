package com.example.javaremotecontroller.communication;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class BlueToothHelper {
    private BluetoothAdapter bluetoothAdapter;
    private Context context;

    public BlueToothHelper(Context context) {
        this.context = context;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null) {
            Toast.makeText(this.context, "您的设备不支持蓝牙！", Toast.LENGTH_LONG).show();
            return;
        }
        Log.v("藍牙啓用情況：", Boolean.toString(bluetoothAdapter.isEnabled()));
//        // 如果蓝牙未启用，则请求启用蓝牙
//        if (!bluetoothAdapter.isEnabled()) {
//            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
//        }
    }

}
