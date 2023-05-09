package com.example.javaremotecontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.javaremotecontroller.model.BrandModel;
import com.example.javaremotecontroller.util.IRApplication;
import com.example.javaremotecontroller.util.util;

import net.irext.webapi.WebAPICallbacks;
import net.irext.webapi.bean.ACStatus;
import net.irext.webapi.model.RemoteIndex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AirConditionPanel extends AppCompatActivity implements View.OnClickListener {

    private BrandModel brandModel;
    private IRApplication mApp;
    private List<RemoteIndex> remoteIndexList = new ArrayList<>();
    private String TAG = "WEB_API_DEBUG";
    private int POWER = 0;
    private int MODE = 1;
    private int TEMP_PLUS = 2;
    private int TEMP_DECS = 3;
    private int WIND_POWER = 9;
    private int SWEEP_WIND = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_condition_panel);

        mApp = (IRApplication) Objects.requireNonNull(this).getApplication();
        init();
    }

    private void init() {
        Bundle bundle = this.getIntent().getExtras();
        brandModel = bundle.getParcelable(util.BRAND_LIST_TO_OPERATION_PANEL_KEY);
        requestIndex();
    }

    private void requestIndex() {
        WebAPICallbacks.ListIndexesCallback listIndexesCallback = new WebAPICallbacks.ListIndexesCallback() {
            @Override
            public void onListIndexesSuccess(List<RemoteIndex> list) {
                remoteIndexList = list;
                for(RemoteIndex ri : list) {
                    Log.e(TAG, "onListIndexesSuccess: " + ri.getId());
                }
                requestDecode();
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
        mApp.mWeAPIs.listRemoteIndexes(brandModel.getCategoryId(),brandModel.getId(),null,null,0,listIndexesCallback);
    }

    /**
     * 解码
     */
    private void requestDecode() {
        new Thread() {
            @Override
            public void run() {
                int code[];
                for(RemoteIndex ri : remoteIndexList) {
                    // 电源
                    code = mApp.mWeAPIs.decodeIR(ri.getId(), new ACStatus(),POWER, 0,0,0);
//                    // 温度 +
//                    code = mApp.mWeAPIs.decodeIR(ri.getId(), new ACStatus(),TEMP_PLUS, 0,0,0);
//                    // 温度 -
//                    code = mApp.mWeAPIs.decodeIR(ri.getId(), new ACStatus(),TEMP_DECS, 0,0,0);
//                    // 模式
//                    code = mApp.mWeAPIs.decodeIR(ri.getId(), new ACStatus(),MODE, 0,0,0);
//                    // 风力
//                    code = mApp.mWeAPIs.decodeIR(ri.getId(), new ACStatus(),WIND_POWER, 0,0,0);
//                    // 扫风
//                    code = mApp.mWeAPIs.decodeIR(ri.getId(), new ACStatus(),SWEEP_WIND, 0,0,0);

                    Log.e(TAG, "requestDecode: " + Arrays.toString(code));
                }
            }
        }.start();

    }

    /**
     * 按键处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        Log.e(TAG, "air_condition_click: " + v.getId() );
        switch (v.getId()){
            case R.id.ac_power:
                break;
            default:
                break;
        }
    }
}