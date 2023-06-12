package com.example.javaremotecontroller.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaremotecontroller.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder> {

    private List<String> titles = new ArrayList();

    public ViewPagerAdapter() {
        titles.add("hello1");
        titles.add("hello2");
        titles.add("hello3");
        titles.add("hello4");
        titles.add("hello5");
    }

    @NonNull
    @NotNull
    @Override
    public ViewPagerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewPagerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pager, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerViewHolder holder, int position) {
        // 进行数据绑定，不同页面展示不同数据
        holder.textView.setText(titles.get(position));
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class ViewPagerViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        RelativeLayout mContainer;
        public ViewPagerViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            mContainer = itemView.findViewById(R.id.container);
            textView = itemView.findViewById(R.id.text_view_in_pager);
        }
    }
}
