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
import com.google.gson.Gson;

import net.irext.webapi.WebAPICallbacks;
import net.irext.webapi.bean.ACStatus;
import net.irext.webapi.model.RemoteIndex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AirConditionPanel extends AppCompatActivity implements View.OnClickListener {

    private BrandModel brandModel;
    private IRApplication mApp;
    private List<RemoteIndex> remoteIndexList = new ArrayList<>();
    private String TAG = "WEB_API_DEBUG";
    private TextView indicator;
    private int POWER = 0;
    private int MODE = 1;
    private int TEMP_PLUS = 2;
    private int TEMP_DECS = 3;
    private int WIND_POWER = 9;
    private int SWEEP_WIND = 10;
    private int currentIndex = 1;
    private boolean decoding = false;
    private ACStatus acStatus = new ACStatus(0, 0, 4, 0, 0, 0, 0, 0);
    /**
     * 按键-红外 map 数组
     */
    private ArrayList<Map<Integer, int[]>> operatorKeyArray = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_condition_panel);

        mApp = (IRApplication) Objects.requireNonNull(this).getApplication();
        util.immersionStatusBar(this);
        init();
        setBackActive();
    }

    private void init() {
        indicator = findViewById(R.id.indicator_ac);

        Bundle bundle = this.getIntent().getExtras();
        brandModel = bundle.getParcelable(util.BRAND_LIST_TO_OPERATION_PANEL_KEY);
        requestIndex();
    }

    private void requestIndex() {
        WebAPICallbacks.ListIndexesCallback listIndexesCallback = new WebAPICallbacks.ListIndexesCallback() {
            @Override
            public void onListIndexesSuccess(List<RemoteIndex> list) {
                remoteIndexList = list;
                indicator.setText(currentIndex + "/" + remoteIndexList.size());
            }

            @Override
            public void onListIndexesFailed() {
                Log.e(TAG, "onListIndexesFailed: ");
            }

            @Override
            public void onListIndexesError() {
                Log.e(TAG, "onListIndexesError: ");
            }
        };
        mApp.mWeAPIs.listRemoteIndexes(brandModel.getCategoryId(), brandModel.getId(), null, null, 0, listIndexesCallback);
    }

    /**
     * 解码
     */
    private void requestDecode(int keyCode) {
        Context context = this;
        ToastUtils.showToast(this, "解码中");
        new Thread() {
            @Override
            public void run() {
                int code[];
                decoding = true;
                RemoteIndex ri = remoteIndexList.get(currentIndex - 1);
                code = mApp.mWeAPIs.decodeIR(ri.getId(), acStatus, keyCode, 0, 0, 0);
                Log.e(TAG, "run: " + ":" + ri.getId());
                InfraredHelper.sendSignal(context, code);

                Looper.prepare();
                ToastUtils.showToast(context, "已发送");
                decoding = false;
                Looper.loop();
            }
        }.start();
    }

    /**
     * 按键处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Log.e(TAG, "onClick: " + new Gson().toJson(acStatus, acStatus.getClass()));
        if (decoding) {
            ToastUtils.showToast(this, "解码中");
            return;
        }
        switch (v.getId()) {
            case R.id.ac_power:
                acStatus.setAcPower(acStatus.getAcPower() == 0 ? 1 : 0);
                requestDecode(POWER);
                break;
            case R.id.temp_incs:
                acStatus.setAcTemp(acStatus.getAcTemp() + 1 > 12 ? 12 : acStatus.getAcTemp() + 1 );
                requestDecode(TEMP_PLUS);
                break;
            case R.id.temp_decs:
                acStatus.setAcTemp(acStatus.getAcTemp() - 1 < 0 ? 0 : acStatus.getAcTemp() - 1);
                requestDecode(TEMP_DECS);
                break;
            case R.id.wind_power_incs:
                acStatus.setAcWindSpeed(acStatus.getAcWindSpeed() + 1);
                requestDecode(WIND_POWER);
                break;
            case R.id.sweep_horizontal:
            case R.id.sweep_vertical:
                requestDecode(SWEEP_WIND);
                break;
            case R.id.pre_btn:
                int index = currentIndex - 1;
                if (index <= 0) {
                    ToastUtils.showToast(v.getContext(), "已是第一个");
                    return;
                }
                currentIndex -= 1;
                indicator.setText(currentIndex + "/" + remoteIndexList.size());
                break;
            case R.id.next_btn:
                int index_2 = currentIndex + 1;
                if (index_2 > remoteIndexList.size()) {
                    ToastUtils.showToast(v.getContext(), "已是最后一个");
                    return;
                }
                currentIndex += 1;
                indicator.setText(currentIndex + "/" + remoteIndexList.size());
                break;
            default:
                break;
        }
    }

    private void setBackActive() {
        Toolbar toolbar = findViewById(R.id.ac_operation_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }
}