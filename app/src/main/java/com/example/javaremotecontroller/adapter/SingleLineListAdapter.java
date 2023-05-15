package com.example.javaremotecontroller.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaremotecontroller.AirConditionPanel;
import com.example.javaremotecontroller.BrandListActivity;
import com.example.javaremotecontroller.MainActivity;
import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.model.BrandModel;
import com.example.javaremotecontroller.ui.activity.FanActivity;
import com.example.javaremotecontroller.ui.activity.LampActivity;
import com.example.javaremotecontroller.ui.activity.SoundActivity;
import com.example.javaremotecontroller.ui.activity.SweepRobotActivity;
import com.example.javaremotecontroller.ui.activity.TvActivity;
import com.example.javaremotecontroller.util.util;

import net.irext.webapi.model.Brand;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SingleLineListAdapter extends RecyclerView.Adapter<SingleLineListAdapter.SingleLineHolder> {

    private Context context;
    private List<Brand> data;
    private String TAG = "LIST_ITEM_CLICK";

    public SingleLineListAdapter(Context context, List data) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public SingleLineListAdapter.SingleLineHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.brand_list_item,parent,false);
        return new SingleLineHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SingleLineHolder holder, int position) {
        holder.textView.setText(data.get(position).getName());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Brand brand = data.get(position);
                BrandModel brandModel = new BrandModel(brand);
                Intent operationPanel;
                // 跳转到控制面板界面
                switch (brand.getCategoryId()){
                    // 空调
                    case util.CATEGORY_ID_AIR_CONDITION:
                        operationPanel = new Intent(context, AirConditionPanel.class);
                        break;
                    case util.CATEGORY_ID_TV:
                        operationPanel = new Intent(context, TvActivity.class);
                        break;
                    case util.CATEGORY_ID_FAN:
                        operationPanel = new Intent(context, FanActivity.class);
                        break;
                    case util.CATEGORY_ID_SOUND:
                        operationPanel = new Intent(context, SoundActivity.class);
                        break;
                    case util.CATEGORY_ID_LAMP:
                        operationPanel = new Intent(context, LampActivity.class);
                        break;
                    case util.CATEGORY_ID_SWEEP:
                        operationPanel = new Intent(context, SweepRobotActivity.class);
                        break;
                    default:
                        operationPanel = new Intent(context, MainActivity.class);
                        break;
                }
                Bundle bundle = new Bundle();
                bundle.putParcelable(util.BRAND_LIST_TO_OPERATION_PANEL_KEY, brandModel);
                operationPanel.putExtras(bundle);
                context.startActivity(operationPanel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class SingleLineHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public SingleLineHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.brand_list_item_textview);
        }
    }
}
