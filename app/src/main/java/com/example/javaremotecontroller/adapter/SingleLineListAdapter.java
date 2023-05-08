package com.example.javaremotecontroller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaremotecontroller.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SingleLineListAdapter extends RecyclerView.Adapter<SingleLineListAdapter.SingleLineHolder> {

    private Context context;
    private ArrayList<String> data;

    public SingleLineListAdapter(Context context, ArrayList data) {
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
        holder.textView.setText(data.get(position));
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
