package com.example.javaremotecontroller.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.communication.MouseRemoteControl;

@RequiresApi(api = Build.VERSION_CODES.P)
public class WifiDeviceDetail extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_device_detail);
    }

    @Override
    public void onClick(View v) {
        MouseRemoteControl.onMouseLeftDown();
    }
}