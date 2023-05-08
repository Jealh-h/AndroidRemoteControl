package com.example.javaremotecontroller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.javaremotecontroller.adapter.SingleLineListAdapter;
import com.example.javaremotecontroller.util.IRApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.irext.webapi.model.*;
import net.irext.webapi.WebAPIs;
import net.irext.webapi.WebAPICallbacks.*;


public class BrandListActivity extends AppCompatActivity {

    private ArrayList brandList = new ArrayList();
    private WebAPIs webAPIs = WebAPIs.getInstance("http://site.irext.net","/irext-server");
    private String TAG = "WEB_API_DEBUG";
    private IRApplication mApp;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_list);

        mApp = (IRApplication) Objects.requireNonNull(this).getApplication();
        init();
    }

    private void init() {
        recyclerView = findViewById(R.id.brand_recycler_view);

        brandList.add("格力");
        brandList.add("美的");

        SingleLineListAdapter singleLineAdapter = new SingleLineListAdapter(BrandListActivity.this,brandList);
        recyclerView.setAdapter(singleLineAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(BrandListActivity.this));

        if(Build.VERSION.SDK_INT > 9) {
            new Thread() {
                @Override
                public void run() {
//                    signIn();
                    mApp.mWeAPIs.listCategories(0, 20, new ListCategoriesCallback() {
                        @Override
                        public void onListCategoriesSuccess(List<Category> list) {
                            for(Category c : list) {
                                Log.e(TAG, "onListCategoriesSuccess: " + c.getName() + "--" + c.getId() );
                            }
                        }

                        @Override
                        public void onListCategoriesFailed() {
                            Log.e(TAG, "onListCategoriesFailed: ");
                        }

                        @Override
                        public void onListCategoriesError() {
                            Log.e(TAG, "onListCategoriesError: ");
                        }
                    });
                }
            }.start();
        }

    }

    private void signIn() {
        ListCategoriesCallback listCategoriesCallback = new ListCategoriesCallback() {
            @Override
            public void onListCategoriesSuccess(List<Category> categories) {
                Log.e(TAG, "onListCategoriesSuccess: \n" + categories.toString() );
            }

            @Override
            public void onListCategoriesFailed() {
                Log.e(TAG, "onListCategoriesFailed: ");
            }

            @Override
            public void onListCategoriesError() {
                Log.e(TAG, "onListCategoriesError: ");
            }
        };
        mApp.mWeAPIs.listCategories(0, 10, listCategoriesCallback);
    }
}