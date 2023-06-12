package com.example.javaremotecontroller.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.communication.BlueToothHelper;
import com.example.javaremotecontroller.communication.BluetoothConnectionManager;
import com.example.javaremotecontroller.communication.BluetoothGattHelper;
import com.example.javaremotecontroller.communication.BluetoothHidHelper;
import com.example.javaremotecontroller.communication.HidMessage;
import com.example.javaremotecontroller.communication.MediaRemoteControl;
import com.example.javaremotecontroller.communication.MouseRemoteControl;
import com.example.javaremotecontroller.model.KeyModel;
import com.example.javaremotecontroller.util.KeyConfig;
import com.example.javaremotecontroller.util.ToastUtils;
import com.example.javaremotecontroller.util.util;

import java.util.Date;

import static com.example.javaremotecontroller.communication.BluetoothHidHelper.HID_DEBUG_TAG;

@RequiresApi(api = Build.VERSION_CODES.P)
public class ComputerActivity extends AppCompatActivity implements View.OnTouchListener {
    private Button leftMouseBtn, rightMouseBtn;
    private LinearLayout touchBoard;
    private Boolean doubleClick = false;
    private BluetoothGattHelper bluetoothGattHelper = new BluetoothGattHelper(this);
    private BluetoothDevice bluetoothDevice;
    private BluetoothConnectionManager bluetoothConnectionManager = new BluetoothConnectionManager();

    private float xStart, yStart;
    private int maxPointerCount;
    private float rate = 1f;
    private long touchBoardContinueTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer);
        init();

        Bundle bundle = this.getIntent().getExtras();
        String address = bundle.getString(util.BLUE_TOOTH_DEVICE_CARRY_DATA_KEY);
        bluetoothDevice = BlueToothHelper.getInstance().getRemoteDevice(address);
    }

    private void init() {
        leftMouseBtn = findViewById(R.id.btn_mouse_left);
        rightMouseBtn = findViewById(R.id.btn_mouse_right);
        touchBoard = findViewById(R.id.touch_board);

        leftMouseBtn.setOnTouchListener(this);
        rightMouseBtn.setOnTouchListener(this);
        touchBoard.setOnTouchListener(this);

        setBackActive();
        util.immersionStatusBar(this);

        for (KeyModel km : KeyConfig.getMediaKeys()) {
            View v = findViewById(km.viewId);
            v.setTag(km.key);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaRemoteControl.onMediaBtnClick(v.getTag().toString(), 20);
                }
            });
        }
    }

    private void setBackActive() {
        Toolbar toolbar = findViewById(R.id.computer_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.computer_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, KeyboardActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                switch (id) {
                    case R.id.touch_board:
                        if (BluetoothHidHelper.isConnect()) {
                            if (event.getPointerCount() == 1) {
                                int deltaX = (int) ((event.getX() - xStart) * rate);
                                int deltaY = (int) ((event.getY() - yStart) * rate);

                                if(doubleClick) {
                                    MouseRemoteControl.onLeftDownMove(deltaX, deltaY);
                                }else {
                                    MouseRemoteControl.onMouseMove(deltaX, deltaY, 0);
                                }

                            } else if (event.getPointerCount() == 2) {
                                int delta = (int) ((event.getY(0) - yStart) * (rate - 0.5));
                                MouseRemoteControl.onMouseMove(0, 0, delta);
                            }
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
                return handleBtnClick(true, id, event);
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                return handleBtnClick(false, id, event);
            default:
                break;
        }
        return true;
    }

    /**
     * @param dir    true 表示向上抬起
     * @param viewId
     * @param event
     */
    private boolean handleBtnClick(boolean dir, int viewId, MotionEvent event) {
        switch (viewId) {
            case R.id.touch_board:
                maxPointerCount = event.getPointerCount();
                xStart = event.getX();
                yStart = event.getY();
                if (!dir) { // 按压
                    // 双击
                    long now = new Date().getTime();
                    Log.e("TIMER_TEST", "handleBtnClick: "+ (now - touchBoardContinueTime) );
                    if(now - touchBoardContinueTime >= 50 && now - touchBoardContinueTime <=250) {
                        doubleClick = true;
                        return true;
                    }
//                    MouseRemoteControl.onMouseLeftDown();
                    touchBoardContinueTime = new Date().getTime();
                }
                else {
                    long timeDiff = new Date().getTime() - touchBoardContinueTime;
                    // 按下抬起时间差距较小，视为点击
                    if (timeDiff >= 50 && timeDiff <= 150) {
                        MouseRemoteControl.simulatedLeftClick(20);
                    }else { // 视为抬起
                        MouseRemoteControl.onMouseLeftUp();
                        doubleClick = false;
                    }
                }
                return true;
            case R.id.btn_mouse_right:
                if (dir)
                    MouseRemoteControl.onMouseRightUp();
                else
                    MouseRemoteControl.onMouseRightDown();
                break;
            case R.id.btn_mouse_left:
                if (dir)
                    MouseRemoteControl.onMouseLeftUp();
                else
                    MouseRemoteControl.onMouseLeftDown();
                break;
            default:
                break;
        }
        return false;
    }
}