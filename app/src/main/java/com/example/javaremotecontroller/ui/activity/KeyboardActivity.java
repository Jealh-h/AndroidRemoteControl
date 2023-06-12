package com.example.javaremotecontroller.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.communication.MediaRemoteControl;
import com.example.javaremotecontroller.model.KeyModel;
import com.example.javaremotecontroller.util.KeyConfig;
import com.example.javaremotecontroller.util.util;

import static com.example.javaremotecontroller.communication.BluetoothHidHelper.HID_DEBUG_TAG;

public class KeyboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        util.immersionStatusBar(this);
        setBackActive();
        init();
    }

    private void init() {
        for (KeyModel km : KeyConfig.getCommonKeys()) {
            View v = findViewById(km.viewId);
            v.setTag(km.key);
            v.setOnTouchListener(new View.OnTouchListener() {
                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    Log.e(HID_DEBUG_TAG, action + "---" + v.getTag());
                    if (action == MotionEvent.ACTION_DOWN) {
                        MediaRemoteControl.onKeyBoardKeyDown(v.getTag().toString());
                    } else if (action == MotionEvent.ACTION_UP) {
                        MediaRemoteControl.onKeyBoardKeyUp(v.getTag().toString());
                    }
                    return false;
                }
            });
        }

        for (KeyModel km : KeyConfig.getModifierKeys()){
            View v = findViewById(km.viewId);
            v.setTag(km.key);
            v.setOnTouchListener(new View.OnTouchListener() {
                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    Log.e(HID_DEBUG_TAG, action + "---" + v.getTag());
                    if (action == MotionEvent.ACTION_DOWN) {
                        MediaRemoteControl.onModifierDown(v.getTag().toString());
                    } else if (action == MotionEvent.ACTION_UP) {
                        MediaRemoteControl.onModifierUp(v.getTag().toString());
                    }
                    return false;
                }
            });
        }
    }

    private void setBackActive() {
        Toolbar toolbar = findViewById(R.id.keyboard_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }
}