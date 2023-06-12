package com.example.javaremotecontroller.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.communication.InfraredHelper;
import com.example.javaremotecontroller.model.BrandModel;
import com.example.javaremotecontroller.util.IRApplication;
import com.example.javaremotecontroller.util.ToastUtils;
import com.example.javaremotecontroller.util.util;

import net.irext.webapi.WebAPICallbacks;
import net.irext.webapi.model.RemoteIndex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FanActivity extends AppCompatActivity implements View.OnClickListener {
    private BrandModel brandModel;
    private List<RemoteIndex> remoteIndexList = new ArrayList<>();
    private TextView indicator;
    private IRApplication mApp;
    private String TAG = "FANActivity";
    private boolean decoding = false;
    private int currentIndex = 1;
    private final int POWER = 1;
    private final int WIND_POWER_INCREACE = 6;
    private final int WIND_POWER_DECREACE = 7;
    private final int SHAKE_HEAD = 8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan);

        mApp = (IRApplication) Objects.requireNonNull(this).getApplication();
        Bundle bundle = this.getIntent().getExtras();
        brandModel = bundle.getParcelable(util.BRAND_LIST_TO_OPERATION_PANEL_KEY);

        init();

        util.immersionStatusBar(this);
        setBackActive();
    }

    private void init() {
        indicator = findViewById(R.id.indicator_fan);
        requestIndex();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fan_power:
                requestDecode(POWER);
                break;
            case R.id.fan_shake_head:
                requestDecode(SHAKE_HEAD);
                break;
            case R.id.fan_wind_power_incs:
                requestDecode(WIND_POWER_INCREACE);
                break;
            case R.id.fan_wind_power_decs:
                requestDecode(WIND_POWER_DECREACE);
                break;
            case R.id.pre_bt_fan:
                if(currentIndex <= 1){
                    ToastUtils.showToast(v.getContext(), "已是第一个");
                    return;
                }
                currentIndex -= 1;
                indicator.setText(currentIndex + "/" + remoteIndexList.size());
                break;
            case R.id.next_btn_fan:
                if(currentIndex + 1 > remoteIndexList.size()) {
                    ToastUtils.showToast(v.getContext(), "已是最后一个");
                    return;
                }
                currentIndex += 1;
                indicator.setText(currentIndex + "/" + remoteIndexList.size());
                break;
        }
    }

    private void setBackActive() {
        Toolbar toolbar = findViewById(R.id.fan_operation_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });
    }

    private void requestDecode(int keyCode) {
        Context context = this;
        ToastUtils.showToast(this, "解码中");
        new Thread() {
            @Override
            public void run() {
                int code[];
                decoding = true;
                RemoteIndex ri = remoteIndexList.get(currentIndex - 1);
                code = mApp.mWeAPIs.decodeIR(ri.getId(), null,keyCode, 0,0,0);
                Log.e(TAG, "run: " + Arrays.toString(code));
                InfraredHelper.sendSignal(context, code);
                Looper.prepare();
                ToastUtils.showToast(context, "已发送");
                decoding = false;
                Looper.loop();
            }
        }.start();
    }

    private void requestIndex() {
        Context context = this;
        WebAPICallbacks.ListIndexesCallback listIndexesCallback = new WebAPICallbacks.ListIndexesCallback() {
            @Override
            public void onListIndexesSuccess(List<RemoteIndex> list) {
                remoteIndexList = list;
                indicator.setText(currentIndex + "/" + remoteIndexList.size());
            }

            @Override
            public void onListIndexesFailed() {
                ToastUtils.showToast(context, "获取索引列表失败");
            }

            @Override
            public void onListIndexesError() {
                ToastUtils.showToast(context, "获取索引列表出错");
            }
        };
        mApp.mWeAPIs.listRemoteIndexes(brandModel.getCategoryId(),brandModel.getId(),null,null,0,listIndexesCallback);
    }
}