package com.airtlab.news.adapter;

import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter RecyclerView.Adapter  {
    private Context mContext;
    private List<CommentEntity> dataList;

    public CommentAdapter(Context context) {
        this.mContext = context;
    }

    public CommentAdapter(Context context, List<CommentEntity> list) {
        this.mContext = context;
        this.dataList = list;
    }
}
