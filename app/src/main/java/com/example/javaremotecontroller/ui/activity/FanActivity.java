package com.example.javaremotecontroller.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.model.BrandModel;
import com.example.javaremotecontroller.util.IRApplication;
import com.example.javaremotecontroller.util.ToastUtils;
import com.example.javaremotecontroller.util.util;

import net.irext.webapi.WebAPICallbacks;
import net.irext.webapi.model.RemoteIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FanActivity extends AppCompatActivity implements View.OnClickListener {
    private BrandModel brandModel;
    private List<RemoteIndex> remoteIndexList = new ArrayList<>();
    private TextView indicator;
    private IRApplication mApp;
    private int currentIndex = 1;

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
        ToastUtils.showToast(this, "已发送");
    }

    private void setBackActive() {
        Toolbar toolbar = findViewById(R.id.fan_operation_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });
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