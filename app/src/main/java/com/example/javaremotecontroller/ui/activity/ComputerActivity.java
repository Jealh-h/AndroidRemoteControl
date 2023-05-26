package com.example.javaremotecontroller.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.communication.BluetoothHidHelper;
import com.example.javaremotecontroller.communication.MouseRemoteControl;

import static com.example.javaremotecontroller.communication.BluetoothHidHelper.HID_DEBUG_TAG;

@RequiresApi(api = Build.VERSION_CODES.P)
public class ComputerActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private Button leftMouseBtn, rightMouseBtn;
    private LinearLayout touchBoard;

    private float xStart, yStart;
    private int maxPointerCount;
    private float rate = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer);
        init();
    }

    private void init() {
        leftMouseBtn = findViewById(R.id.btn_mouse_left);
        rightMouseBtn = findViewById(R.id.btn_mouse_right);
        touchBoard = findViewById(R.id.touch_board);

        leftMouseBtn.setOnTouchListener(this);
        rightMouseBtn.setOnTouchListener(this);
        touchBoard.setOnTouchListener(this);
    }


    @Override
    public void onClick(View v) {
        MouseRemoteControl.onMouseLeftDown();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        Log.e(HID_DEBUG_TAG, "onTouch: " + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                switch (id) {
                    case R.id.touch_board:
                        maxPointerCount = Math.max(maxPointerCount, event.getPointerCount());
                        Log.e(HID_DEBUG_TAG, "onTouch: " + event.getX() + "--" + event.getY());
                        if (BluetoothHidHelper.isConnect()) {
                            int deltaX = (int) ((event.getX() - xStart) * rate);
                            int deltaY = (int) ((event.getY() - yStart) * rate);
                            MouseRemoteControl.onMouseMove(deltaX, deltaY, 0);
                        }
                        xStart = event.getX();
                        yStart = event.getY();
                        break;
                    default:
                        break;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                maxPointerCount = event.getPointerCount();
                xStart = event.getX();
                yStart = event.getY();
                break;
            default:
                break;
        }
        return true;
    }
}