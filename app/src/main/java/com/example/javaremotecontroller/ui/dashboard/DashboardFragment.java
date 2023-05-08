package com.example.javaremotecontroller.ui.dashboard;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaremotecontroller.BlueToothDeviceDetail;
import com.example.javaremotecontroller.BrandListActivity;
import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.WifiDeviceConnectState;
import com.example.javaremotecontroller.adapter.BlueToothDeviceListAdapter;
import com.example.javaremotecontroller.communication.BlueToothHelper;
import com.example.javaremotecontroller.databinding.FragmentDashboardBinding;
import com.example.javaremotecontroller.model.DeviceCategoryModel;
import com.example.javaremotecontroller.util.util;

import java.util.ArrayList;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private Toast mToast;
    private ProgressBar progressBar;

    RecyclerView recyclerView;
    BlueToothHelper blueToothHelper = new BlueToothHelper();
    BlueToothDeviceListAdapter mAdapter;
    ArrayList<BluetoothDevice> mDeviceList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        progressBar = root.findViewById(R.id.device_scan_progress);

        mAdapter = new BlueToothDeviceListAdapter(getActivity(), mDeviceList);
        recyclerView = root.findViewById(R.id.blue_tooth_scan_device_recycler_view);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        init();
        initGridLayout();

        return root;
    }

    private void initGridLayout() {
        View root = binding.getRoot();
        GridLayout gridLayout = (GridLayout) root.findViewById(R.id.device_category_grid);

        for(DeviceCategoryModel dm:util.deviceCategory) {
            CardView cd = cardViewFactory(dm);
            gridLayout.addView(cd);
        }
    }

    /**
     * 动态创建 CardView
     * @param model
     * @return
     */
    private CardView cardViewFactory(DeviceCategoryModel model) {
        // cardView
        CardView cardView = new CardView(getActivity());
        cardView.setRadius(8);
        cardView.setContentPadding(
                util.dpToPx(8,getActivity()),
                util.dpToPx(8,getActivity()),
                util.dpToPx(8,getActivity()),
                util.dpToPx(8,getActivity()));

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.setMargins(
                util.dpToPx(10,getActivity()),
                util.dpToPx(10,getActivity()),
                util.dpToPx(10,getActivity()),
                util.dpToPx(10,getActivity()));
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        cardView.setLayoutParams(params);

        // linearLayout
        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(lParams);

        // imageView
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(model.getImageSrc());
        ViewGroup.LayoutParams imageParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(imageParams);

        // textView
        TextView textView = new TextView(getActivity());
        textView.setText(model.getCategoryName());
        ViewGroup.LayoutParams textParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        textView.setLayoutParams(textParams);

        linearLayout.addView(imageView);
        linearLayout.addView(textView);
        cardView.addView(linearLayout);

        cardView.setOnClickListener(this);

        return cardView;

        //                <androidx.cardview.widget.CardView
//        app:cardCornerRadius="8dp"
//        app:contentPadding="8dp"
//        android:layout_columnWeight="1"
//        android:layout_margin="10dp"
//        android:layout_width="wrap_content"
//        android:layout_height="wrap_content"
//                >
//            <LinearLayout
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:orientation="vertical">
//                <ImageView
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:src="@drawable/ic_baseline_live_tv_24"/>
//                <TextView
//        android:layout_width="match_parent"
//        android:layout_height="wrap_content"
//        android:textAlignment="center"
//        android:text="电视"/>
//            </LinearLayout>
//        </androidx.cardview.widget.CardView>
    }

    private void init() {
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, intentFilter);
        IntentFilter intentFilter1 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mReceiver, intentFilter1);
    }

    //设置toast的标准格式
    private void showToast(String text){
        if(mToast == null){
            mToast = Toast.makeText(getActivity(), text,Toast.LENGTH_SHORT);
        }
        else {
            mToast.setText(text);
        }
        mToast.show();

    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                //setProgressBarIndeterminateVisibility(true);
                //初始化数据列表
                mDeviceList.clear();
                mAdapter.notifyDataSetChanged();
            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                progressBar.setVisibility(View.GONE);
                if(mDeviceList.size() == 0) {
                    Toast.makeText(getContext(),"no device found", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(),"Click on the device to start Chat", Toast.LENGTH_SHORT).show();
                }
            } else if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                // 已连接
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.ACTION_ACL_CONNECTED);
                Log.e("TAG", "connected: " + device.getName());
            } else if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //找到一个添加一个
                if(device.getBondState()!=BluetoothDevice.BOND_BONDED){
                    mDeviceList.add(device);
                }
                mAdapter.notifyDataSetChanged();

            } else if(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {  //此处作用待细查
                int scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, 0);
                if(scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }

            } else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(remoteDevice == null) {
                    showToast("无设备");
                    return;
                }
                int status = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, 0);
                if(status == BluetoothDevice.BOND_BONDED) {
                    showToast("已绑定" + remoteDevice.getName());
                } else if(status == BluetoothDevice.BOND_BONDING) {
                    showToast("正在绑定" + remoteDevice.getName());
                } else if(status == BluetoothDevice.BOND_NONE) {
                    showToast("未绑定" + remoteDevice.getName());
                }
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        Intent brandListIntent = new Intent(getActivity(), BrandListActivity.class);
        startActivity(brandListIntent);
    }
}