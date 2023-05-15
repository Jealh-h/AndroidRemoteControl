package com.example.javaremotecontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.javaremotecontroller.communication.InfraredHelper;
import com.example.javaremotecontroller.model.BrandModel;
import com.example.javaremotecontroller.util.IRApplication;
import com.example.javaremotecontroller.util.ToastUtils;
import com.example.javaremotecontroller.util.util;

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
        Context context = this;
        new Thread() {
            @Override
            public void run() {
                int code[];
                Map<Integer, int[]> map = new HashMap();
                decoding = true;
                Log.e(TAG, "decode " + decoding);
                try {
                    operatorKeyArray.get(currentIndex - 1);
                } catch (IndexOutOfBoundsException  e) {
                    try {
                        RemoteIndex ri = remoteIndexList.get(currentIndex - 1);
                        // 电源
                        code = mApp.mWeAPIs.decodeIR(ri.getId(), new ACStatus(),POWER, 0,0,0);
                        map.put(POWER, code);
                        // 温度 +
                        code = mApp.mWeAPIs.decodeIR(ri.getId(), new ACStatus(),TEMP_PLUS, 0,0,0);
                        map.put(TEMP_PLUS, code);
                        // 温度 -
                        code = mApp.mWeAPIs.decodeIR(ri.getId(), new ACStatus(),TEMP_DECS, 0,0,0);
                        map.put(TEMP_DECS, code);
                        // 模式
                        code = mApp.mWeAPIs.decodeIR(ri.getId(), new ACStatus(),MODE, 0,0,0);
                        map.put(MODE, code);
                        // 风力
                        code = mApp.mWeAPIs.decodeIR(ri.getId(), new ACStatus(),WIND_POWER, 0,0,0);
                        map.put(WIND_POWER, code);
                        // 扫风
                        code = mApp.mWeAPIs.decodeIR(ri.getId(), new ACStatus(),SWEEP_WIND, 0,0,0);
                        map.put(SWEEP_WIND, code);
                        operatorKeyArray.add(map);
                        Looper.prepare();
                        ToastUtils.showToast(context, "解码完成");
                        decoding = false;
                        Looper.loop();
                    }catch (Exception exp) {
                        Log.e(TAG, "run: " + exp.toString() );
                    }
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
        Log.e(TAG, "air_condition_click: " + currentIndex + "|" + operatorKeyArray.size() + decoding );
        if(operatorKeyArray.size() == 0) {
            return;
        }
        if(decoding) {
            ToastUtils.showToast(this, "解码中,请稍等");
            return;
        }
        Map<Integer, int[]> m = operatorKeyArray.get(currentIndex - 1);
        switch (v.getId()){
            case R.id.ac_power:
                int[] code = m.get(POWER);
                Log.e(TAG, "air_condition_click: " + Arrays.toString(code) );
                InfraredHelper.sendSignal(this, code);
                break;
            case R.id.temp_incs:
                InfraredHelper.sendSignal(this, m.get(TEMP_PLUS));
                break;
            case R.id.temp_decs:
                InfraredHelper.sendSignal(this, m.get(TEMP_DECS));
                break;
            case R.id.wind_power_incs:
                InfraredHelper.sendSignal(this, m.get(WIND_POWER));
                break;
            case R.id.sweep_horizontal:
            case R.id.sweep_vertical:
                InfraredHelper.sendSignal(this, m.get(SWEEP_WIND));
                break;
            case R.id.pre_btn:
                int index = currentIndex - 1;
                if(index <= 0) {
                    ToastUtils.showToast(v.getContext(), "已是第一个");
                    return;
                }
                currentIndex -= 1;
                indicator.setText(currentIndex + "/" + remoteIndexList.size());
                break;
            case R.id.next_btn:
                int index_2 = currentIndex + 1;
                if(index_2 > remoteIndexList.size()) {
                    ToastUtils.showToast(v.getContext(), "已是最后一个");
                    return;
                }
                currentIndex += 1;
                requestDecode();
                indicator.setText(currentIndex + "/" + remoteIndexList.size());
                break;
            default:
                break;
        }
    }

    private void setBackActive() {
        Toolbar toolbar = findViewById(R.id.ac_operation_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });
    }
}