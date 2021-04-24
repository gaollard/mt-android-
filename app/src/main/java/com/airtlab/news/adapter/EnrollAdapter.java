package com.airtlab.news.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airtlab.news.entity.EnrollEntity;

import java.util.List;

/**
 * @desc 报名列表适配器
 */
public class EnrollAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<EnrollEntity> dataList;
    /**
     * 构造器，无列表数据
     * @param context
     */
    public EnrollAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * 构造器，有列表数据
     * @param context
     * @param list
     */
    public EnrollAdapter(Context context, List<EnrollEntity> list) {
        this.mContext = context;
        this.dataList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
