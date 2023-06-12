package com.example.javaremotecontroller;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.javaremotecontroller.communication.BlueToothHelper;
import com.example.javaremotecontroller.communication.BluetoothHidHelper;
import com.example.javaremotecontroller.communication.MouseRemoteControl;
import com.example.javaremotecontroller.databinding.ActivityBlueToothDeviceDetailBinding;
import com.example.javaremotecontroller.util.util;

@RequiresApi(api = Build.VERSION_CODES.P)
public class BlueToothDeviceDetail extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener {

    private BluetoothDevice bluetoothDevice;
    private String TAG = "BLUE_TOOTH_DEVICE_DETAIL";
    private Button leftBtn,connectBtn;

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
        leftBtn = findViewById(R.id.btn_mouse_left);
        connectBtn = findViewById(R.id.connect_device);
        leftBtn.setOnTouchListener(this::onTouch);
        connectBtn.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        BluetoothHidHelper.connect(bluetoothDevice);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            MouseRemoteControl.onMouseLeftUp();
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            MouseRemoteControl.onMouseLeftDown();
        }
        return true;
    }
}