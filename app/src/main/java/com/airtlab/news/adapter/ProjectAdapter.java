package com.airtlab.news.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airtlab.news.R;
import com.airtlab.news.entity.ProjectEntity;
import com.airtlab.news.entity.VideoEntity;
import com.airtlab.news.listener.OnItemChildClickListener;
import com.airtlab.news.listener.OnItemClickListener;
import com.airtlab.news.view.CircleTransform;
import com.dueeeke.videocontroller.component.PrepareView;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ProjectEntity> mDataList = new ArrayList<>(); // 列表数据

    private OnItemChildClickListener mOnItemChildClickListener; // 点击播放按钮
    private OnItemClickListener mOnItemClickListener; // 点击 item

    protected VideoViewManager getVideoViewManager() {
        return VideoViewManager.instance();
    }

    public ProjectAdapter(Context context) {
        this.mContext = context;
    }

    // Context 是啥?
    public ProjectAdapter(Context context, List<ProjectEntity> list) {
        mContext = context;
        mDataList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.project_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * @desc 设置列表数据，不通过构造器，避免反复创建 Adapter
     * @param videoList
     */
    public void setDataList(List<ProjectEntity> videoList) {
        this.mDataList = videoList;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ProjectEntity entity = mDataList.get(position);

        viewHolder.project_title.setText((position + 1) + ". " + entity.title);
        viewHolder.project_reward.setText(String.valueOf(entity.reward));
        viewHolder.project_skill.setText(entity.requires);
        viewHolder.mIndex = position;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * @desc 设置点击播放回调
     * @param onItemChildClickListener
     */
    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    /**
     * @desc 设置列表item点击回调
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView project_title;
        public TextView project_reward;
        public TextView project_skill;
        public int mIndex;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            project_title = itemView.findViewById(R.id.project_title);
            project_reward = itemView.findViewById(R.id.project_reward);
            project_skill = itemView.findViewById(R.id.project_skill);
            itemView.setTag(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.player_container:
                    if (mOnItemChildClickListener != null) {
//                        mOnItemChildClickListener.onItemChildClick(mPosition);
                    }
                    break;
                default:
                    if (mOnItemClickListener != null) {
//                        mOnItemClickListener.onItemClick(mPosition);
                    }
            }
        }
    }

    public class OnItemClickListener {

    }
}
