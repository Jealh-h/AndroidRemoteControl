package com.example.javaremotecontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.example.javaremotecontroller.adapter.SingleLineListAdapter;
import com.example.javaremotecontroller.model.DeviceCategoryModel;
import com.example.javaremotecontroller.util.IRApplication;
import com.example.javaremotecontroller.util.ToastUtils;
import com.example.javaremotecontroller.util.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.irext.webapi.model.*;
import net.irext.webapi.WebAPICallbacks.*;


public class BrandListActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Brand> brandList = new ArrayList();
    private String TAG = "WEB_API_DEBUG";
    private IRApplication mApp;
    private DeviceCategoryModel deviceCategoryModel;
    private SingleLineListAdapter singleLineAdapter;
    RecyclerView recyclerView;
    private LinearLayout fallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_list);

        Bundle bundle = this.getIntent().getExtras();
        deviceCategoryModel = bundle.getParcelable(util.DASHBOARD_TO_BRAND_LIST_KEY);
        util.immersionStatusBar(this);
        mApp = (IRApplication) Objects.requireNonNull(this).getApplication();
        init();

        setBackActive();
    }

    private void init() {
        recyclerView = findViewById(R.id.brand_recycler_view);
        Toolbar toolbar = findViewById(R.id.brand_list_toolbar);
        fallback = findViewById(R.id.brand_list_failed_fallback);

        toolbar.setTitle("选择品牌" + "（" + deviceCategoryModel.getCategoryName() + "）");

        singleLineAdapter = new SingleLineListAdapter(BrandListActivity.this,brandList);

        recyclerView.setAdapter(singleLineAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(BrandListActivity.this));

        if(Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    requestBrandList();
                }
            });
        }

    }

    private void setBackActive() {
        Toolbar toolbar = findViewById(R.id.brand_list_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });
    }

    public void requestBrandList() {
        LinearLayout loading = findViewById(R.id.brand_list_loading);
        loading.setVisibility(View.VISIBLE);
        fallback.setVisibility(View.GONE);
        ListBrandsCallback listBrandsCallback = new ListBrandsCallback() {
            @Override
            public void onListBrandsSuccess(List<Brand> list) {
                loading.setVisibility(View.GONE);
                brandList.addAll(list);
                singleLineAdapter.notifyDataSetChanged();
            }

            @Override
            public void onListBrandsFailed() {
                loading.setVisibility(View.GONE);
                fallback.setVisibility(View.VISIBLE);
                ToastUtils.showToast(getApplicationContext(), "获取品牌列表失败,请检查网络");
            }

            @Override
            public void onListBrandsError() {
                loading.setVisibility(View.GONE);
                fallback.setVisibility(View.VISIBLE);
                ToastUtils.showToast(getApplicationContext(), "获取品牌列表失败,请检查网络");
            }
        };
        mApp.mWeAPIs.listBrands(deviceCategoryModel.getId(),0, 50, listBrandsCallback);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reload_brand_list_btn:
                requestBrandList();
                break;
            default:
                break;
        }
    }
}