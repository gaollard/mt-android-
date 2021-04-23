package com.airtlab.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airtlab.news.R;
import com.airtlab.news.entity.ProjectEntity;
import com.dueeeke.videoplayer.player.VideoViewManager;

import java.util.ArrayList;
import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ProjectEntity> mDataList = new ArrayList<>(); // 列表数据
    private OnItemClickListener mOnItemClickListener; // 点击 item 的回调事件

    protected VideoViewManager getVideoViewManager() {
        return VideoViewManager.instance();
    }

    public ProjectAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * @desc 设置列表数据
     * @param context
     * @param list
     */
    public ProjectAdapter(Context context, List<ProjectEntity> list) {
        mContext = context;
        mDataList = list;
    }

    /**
     * @desc 创建 view holder
     * @param parent
     * @param viewType
     * @return
     */
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

    /**
     * holder item 绑定 data
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ProjectEntity entity = mDataList.get(position);
        viewHolder.project_title.setText((position + 1) + ". " + entity.title);
        viewHolder.project_reward.setText(String.valueOf(entity.reward));
        viewHolder.project_skill.setText(entity.requires);
        viewHolder.mIndex = position;
    }

    /**
     * @desc 获取条数
     * @return
     */
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * @desc 设置列表item点击回调
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * @desc 定义 ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mDataList.get(mIndex));
                    }
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(ProjectEntity entity);
    }
}
