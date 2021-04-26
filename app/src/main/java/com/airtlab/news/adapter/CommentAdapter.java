package com.airtlab.news.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airtlab.news.R;
import com.airtlab.news.entity.CommentEntity;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<CommentEntity> dataList = new ArrayList<>();

    public CommentAdapter(Context context) {
        this.mContext = context;
    }

    public CommentAdapter(Context context, List<CommentEntity> list) {
        this.mContext = context;
        this.dataList = list;
    }

    /**
     * @desc 设置列表数据，不通过构造器，避免反复创建 Adapter
     * @param list
     */
    public void setDataList(List<CommentEntity> list) {
        this.dataList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);
        CommentAdapter.ViewHolder viewHolder = new CommentAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.e("onBindViewHolder", String.valueOf(position));
        CommentAdapter.ViewHolder vh = (CommentAdapter.ViewHolder) holder;
        CommentEntity entity = dataList.get(position);
        vh.c_nickname.setText(entity.userInfo.nickname);
        vh.c_date.setText(entity.createTime);
        vh.c_content.setText(entity.content);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .cornerRadiusDp(4)
                .oval(false)
                .build();

        Picasso.with(mContext)
                .load(entity.userInfo.avatar)
                .fit()
                .transform(transformation)
                .into(vh.c_avatar);
    }

   public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView c_nickname;
        private TextView c_date;
        private TextView c_content;
        private ImageView c_avatar;


       public ViewHolder(@NonNull View itemView) {
           super(itemView);
           c_nickname = itemView.findViewById(R.id.c_nickname);
           c_date = itemView.findViewById(R.id.c_date);
           c_content = itemView.findViewById(R.id.c_content);
           c_avatar = itemView.findViewById(R.id.c_avatar);
       }
   }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
