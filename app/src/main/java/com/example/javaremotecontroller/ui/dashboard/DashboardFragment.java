package com.example.javaremotecontroller.ui.dashboard;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaremotecontroller.BrandListActivity;
import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.adapter.BlueToothDeviceListAdapter;
import com.example.javaremotecontroller.communication.BlueToothHelper;
import com.example.javaremotecontroller.databinding.FragmentDashboardBinding;
import com.example.javaremotecontroller.model.DeviceCategoryModel;
import com.example.javaremotecontroller.util.util;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private Toast mToast;


    RecyclerView recyclerView;
    BlueToothDeviceListAdapter mAdapter;
    ArrayList<BluetoothDevice> mDeviceList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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

        setOnClick(cardView, model);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setOnClick(View v, DeviceCategoryModel dcm) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent brandListIntent = new Intent(getActivity(), BrandListActivity.class);

                Bundle bundle = new Bundle();
                bundle.putParcelable(util.DASHBOARD_TO_BRAND_LIST_KEY, dcm);
                brandListIntent.putExtras(bundle);

                startActivity(brandListIntent);
            }
        });
    }
}