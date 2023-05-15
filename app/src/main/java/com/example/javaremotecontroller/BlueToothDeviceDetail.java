package com.example.javaremotecontroller;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.javaremotecontroller.communication.BlueToothHelper;
import com.example.javaremotecontroller.databinding.ActivityBlueToothDeviceDetailBinding;
import com.example.javaremotecontroller.util.util;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class BlueToothDeviceDetail extends AppCompatActivity {

    private BluetoothDevice bluetoothDevice;
    private String TAG = "BLUE_TOOTH_DEVICE_DETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth_device_detail);
        util.immersionStatusBar(this);
        init();
    }

    private void init() {
        Bundle bundle = this.getIntent().getExtras();
        String address = bundle.getString(util.BLUE_TOOTH_DEVICE_CARRY_DATA_KEY);
        bluetoothDevice = BlueToothHelper.getInstance().getRemoteDevice(address);
        connect();
        sendCommand();
    }

    private void connect() {
        BlueToothHelper.getInstance().connectSocket(bluetoothDevice);
    }

    private void sendCommand() {
        byte[] volumeCommand = {0x7E, (byte) 0xFF, 0x06, 0x00, 0x00, 0x05, 0x03, 0x3F, (byte) 0xEF, 0x7E}; // 设置音量指令
        BlueToothHelper.getInstance().sendCommand(volumeCommand);
    }

}