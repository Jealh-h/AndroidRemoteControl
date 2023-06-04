package com.example.javaremotecontroller.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.util.Esp8266Helper;
import com.example.javaremotecontroller.util.ToastUtils;
import com.example.javaremotecontroller.util.util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class WiFiStopWatch extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "ESP_DEBUG";
    private EditText editText;

    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_stop_watch);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        init();
    }

    private void init() {
        editText = findViewById(R.id.esp_time_input);
        setBackActive();
        util.immersionStatusBar(this);
    }

    private void setBackActive() {
        Toolbar toolbar = findViewById(R.id.esp_stop_watch_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.esp_start:
                sendToESP(genMsg(Esp8266Helper.START_TIMER, 0));
                break;
            case R.id.esp_set:
                sendToESP(genMsg(Esp8266Helper.SET_TIMER, Integer.parseInt(editText.getText().toString())));
                editText.clearFocus();
                editText.setText("");
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                ToastUtils.showToast(this, "设置成功");
                break;
            case R.id.esp_reset:
                sendToESP(genMsg(Esp8266Helper.RESET_TIMER, 0));
                break;
            case R.id.esp_pause:
                sendToESP(genMsg(Esp8266Helper.PAUSE_TIMER, 0));
                break;
            case R.id.esp_watch_face:
                sendToESP(genMsg(Esp8266Helper.TOGGLE_PAGE, Esp8266Helper.WATCHFACE_PAGE));
                break;
            case R.id.esp_count_down:
                sendToESP(genMsg(Esp8266Helper.TOGGLE_PAGE,Esp8266Helper.COUNTDOWN_PAGE));
                break;
            case R.id.esp_calendar:
                sendToESP(genMsg(Esp8266Helper.TOGGLE_PAGE, Esp8266Helper.NUMBER_CLOCK_PAGE));
                break;
            default:
                JSONObject msg2 = new JSONObject();
                try {
                    msg2.put("operation", Esp8266Helper.SET_TIMER);
                    msg2.put("data", 20);
                }catch (Exception e) {
                    Log.e(TAG, "json parse error" + e.toString());
                }
                sendToESP(msg2.toString());
                break;
        }
    }

    /**
     * 生成给 esp 8266 的消息
     * @return
     */
    private String genMsg(String opr, int data) {
        JSONObject msg = new JSONObject();
        try {
            msg.put(Esp8266Helper.OPERATION_KEY, opr);
            msg.put(Esp8266Helper.DATA_KEY, data);
        }catch (Exception e) {
            Log.e(TAG, "json parse error" + e.toString());
        }
        return msg.toString();
    }


    private void sendToESP(String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress serverAddr = InetAddress.getByName("192.168.4.1"); // 输入ESP8266的IP地址
                    Socket socket = new Socket(serverAddr, 80);
                    // 发送指令
                    OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
                    String message = "GET " + msg + "HTTP/1.1\r\n\r\n";
                    writer.write(message);
                    writer.flush();

                    // 读取响应
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String response = reader.readLine();

                    reader.close();
                    writer.close();
                    socket.close();
                    Log.e(TAG, "response: " + msg + response );
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "run: " + msg + e.toString() );
                }
            }
        }).start();
    }
}