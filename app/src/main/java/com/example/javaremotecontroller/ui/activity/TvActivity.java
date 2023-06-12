package com.example.javaremotecontroller.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.communication.InfraredHelper;
import com.example.javaremotecontroller.model.BrandModel;
import com.example.javaremotecontroller.util.ColorUtils;
import com.example.javaremotecontroller.util.IRApplication;
import com.example.javaremotecontroller.util.ImageUtils;
import com.example.javaremotecontroller.util.ToastUtils;
import com.example.javaremotecontroller.util.util;
import com.example.javaremotecontroller.view.NumberNineGrid;
import com.example.javaremotecontroller.view.RoundMenuView;

import net.irext.webapi.WebAPICallbacks;
import net.irext.webapi.model.RemoteIndex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TvActivity extends AppCompatActivity implements View.OnClickListener {

    private RoundMenuView roundMenuView;
    private BrandModel brandModel;
    private IRApplication mApp;
    private List<RemoteIndex> remoteIndexList = new ArrayList<>();
    private String TAG = "TV_ACTIVITY";
    private TextView indicator;
    private ArrayList<Map<Integer, int[]>> operatorKeyArray = new ArrayList();
    private int currentIndex = 1;
    private static final int NumberOffset = 14;
    private Boolean decoding = false;
    private final int UP = 2;
    private final int DOWN = 3;
    private final int LEFT = 4;
    private final int RIGHT = 5;
    private final int OK = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv);

        mApp = (IRApplication) Objects.requireNonNull(this).getApplication();
        Bundle bundle = this.getIntent().getExtras();
        brandModel = bundle.getParcelable(util.BRAND_LIST_TO_OPERATION_PANEL_KEY);
        init();

        requestIndex();
        util.immersionStatusBar(this);
        setBackActive();
    }

    private void init() {

        roundMenuView = findViewById(R.id.round_menu_view);
        indicator = findViewById(R.id.indicator_tv);
        LinearLayout numPanel = findViewById(R.id.tv_number_panel);

        NumberNineGrid numberNineGrid = new NumberNineGrid(this);
        numberNineGrid.setOnNumKeyBoardLister(numKeyBoardLister);
        numPanel.addView(numberNineGrid);

        RoundMenuView.RoundMenu roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = ColorUtils.getColor(this, R.color.gray_9999);
        roundMenu.strokeColor = ColorUtils.getColor(this, R.color.gray_9999);
        roundMenu.icon = ImageUtils.drawable2Bitmap(this, R.drawable.ic_baseline_keyboard_arrow_right_24);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestDecode(DOWN);
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = ColorUtils.getColor(this, R.color.gray_9999);
        roundMenu.strokeColor = ColorUtils.getColor(this, R.color.gray_9999);
        roundMenu.icon = ImageUtils.drawable2Bitmap(this, R.drawable.ic_baseline_keyboard_arrow_right_24);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestDecode(LEFT);
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = ColorUtils.getColor(this, R.color.gray_9999);
        roundMenu.strokeColor = ColorUtils.getColor(this, R.color.gray_9999);
        roundMenu.icon = ImageUtils.drawable2Bitmap(this, R.drawable.ic_baseline_keyboard_arrow_right_24);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestDecode(UP);
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenu = new RoundMenuView.RoundMenu();
        roundMenu.selectSolidColor = ColorUtils.getColor(this, R.color.gray_9999);
        roundMenu.strokeColor = ColorUtils.getColor(this, R.color.gray_9999);
        roundMenu.icon = ImageUtils.drawable2Bitmap(this, R.drawable.ic_baseline_keyboard_arrow_right_24);
        roundMenu.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestDecode(RIGHT);
            }
        };
        roundMenuView.addRoundMenu(roundMenu);

        roundMenuView.setCoreMenu(
            ColorUtils.getColor(this, R.color.gray_f2f2),
            ColorUtils.getColor(this, R.color.gray_9999),
            ColorUtils.getColor(this, R.color.gray_9999),
            1,
            0.43,
            ImageUtils.drawable2Bitmap(this, R.drawable.ic_baseline_ok_24),
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestDecode(OK);
                }
            });
        numberNineGrid.setOnNumKeyBoardLister(numKeyBoardLister);
    }

    private void setBackActive() {
        Toolbar toolbar = findViewById(R.id.tv_operation_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });
    }

    private NumberNineGrid.NumKeyBoardLister numKeyBoardLister = new NumberNineGrid.NumKeyBoardLister() {
        @Override
        public void onNumLister(int num) {
            requestDecode(num + NumberOffset);
        }

        @Override
        public void onDelLister() {
            int BACK = 9;
            requestDecode(BACK);
        }

        @Override
        public void onDownLister() {
            int MENU = 11;
            requestDecode(MENU);
        }
    };

    @Override
    public void onClick(View v) {
        int VOLUME_PLUS = 7;
        int VOLUME_DECS = 8;
        int POWER = 0;
        int MUTE = 1;
        switch (v.getId()){
            case R.id.tv_power:
                requestDecode(POWER);
                break;
            case R.id.tv_mute:
                requestDecode(MUTE);
                break;
            case R.id.tv_volume_decs:
                requestDecode(VOLUME_DECS);
                break;
            case R.id.tv_volume_incs:
                requestDecode(VOLUME_PLUS);
                break;
            case R.id.channel_incs:
                requestDecode(UP);
                break;
            case R.id.channel_decs:
                requestDecode(DOWN);
                break;
            case R.id.pre_btn_tv:
                if(currentIndex <= 1){
                    ToastUtils.showToast(v.getContext(), "已是第一个");
                    return;
                }
                currentIndex -= 1;
                indicator.setText(currentIndex + "/" + remoteIndexList.size());
                break;
            case R.id.next_btn_tv:
                int index_2 = currentIndex + 1;
                if(index_2 > remoteIndexList.size()) {
                    ToastUtils.showToast(v.getContext(), "已是最后一个");
                    return;
                }
                currentIndex += 1;
                indicator.setText(currentIndex + "/" + remoteIndexList.size());
                break;
        }
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
                for(int i=0;i<list.size();i++) {
                    operatorKeyArray.add(new HashMap<>());
                }
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