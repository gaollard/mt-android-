package com.airtlab.news.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.airtlab.news.R;
import com.airtlab.news.entity.VideoEntity;
import com.airtlab.news.listener.OnItemChildClickListener;
import com.airtlab.news.listener.OnItemClickListener;
import com.airtlab.news.view.CircleTransform;
import com.dueeeke.videocontroller.component.PrepareView;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<VideoEntity> mVideoList = new ArrayList<>(); // 列表数据
    private OnItemChildClickListener mOnItemChildClickListener; // 点击播放按钮
    private OnItemClickListener mOnItemClickListener; // 点击 item

    protected VideoViewManager getVideoViewManager() {
        return VideoViewManager.instance();
    }

    public VideoAdapter(Context context) {
        this.mContext = context;
    }

    // Context 是啥?
    public VideoAdapter(Context context, List<VideoEntity> videoList) {
        mContext = context;
        mVideoList = videoList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * @desc 设置列表数据，不通过构造器，避免反复创建 Adapter
     * @param videoList
     */
    public void setDataList(List<VideoEntity> videoList) {
        this.mVideoList = videoList;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        VideoEntity videoEntity = mVideoList.get(position);

        vh.vTitle.setText(videoEntity.getVtitle());
        vh.vAuthor.setText(videoEntity.getAuthor());
        if (videoEntity.getVideoSocialEntity() != null) {
            int likeNum = videoEntity.getVideoSocialEntity().getLikenum();
            int commentNum = videoEntity.getVideoSocialEntity().getCommentnum();
            int collectNum = videoEntity.getVideoSocialEntity().getCollectnum();
            boolean flagLike = videoEntity.getVideoSocialEntity().isFlagLike();
            boolean flagCollect = videoEntity.getVideoSocialEntity().isFlagCollect();
            vh.vComment.setText(String.valueOf(commentNum));
            vh.vLike.setText(String.valueOf(likeNum));
            vh.vComment.setText(String.valueOf(collectNum));
            if (flagLike) {
                // 已点赞
                vh.vLike.setTextColor(Color.parseColor("#E21918"));
                vh.imgLike.setImageResource(R.mipmap.dianzan_select);
            }
            if (flagCollect) {
                // 已收藏
                vh.vCollect.setTextColor(Color.parseColor("#E21918"));
                vh.imgCollect.setImageResource(R.mipmap.collect_select);
            }
            vh.flagCollect = flagCollect;
            vh.flagLike = flagLike;
        }
        // 头像
        Picasso.with(mContext)
                .load(videoEntity.getHeadurl())
                .transform(new CircleTransform())
                .into(vh.imgHeader);
        // 封面
        Picasso.with(mContext)
                .load(videoEntity.getCoverurl())
                .into(vh.mThumb);

        // 滚动位置
        vh.mPosition = position;
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
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
        public ImageView mThumb; // 封面图
        public PrepareView mPrepareView; // 预览
        public FrameLayout mPlayerContainer; // 容器
        public int mPosition; // 列表的滚动位置

        private TextView vTitle; // 标题
        private TextView vAuthor; // 作者
        private TextView vLike; // 点赞
        private TextView vComment; // 评论
        private TextView vCollect; // 收藏
        private ImageView imgHeader; // 头像
        private ImageView imgCollect; // 收藏图标
        private ImageView imgLike; // 点赞图标
        private ImageView imgComment; // 评论图标
        private boolean flagCollect; // 是否收藏
        private boolean flagLike; // 是否点赞

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgHeader = itemView.findViewById(R.id.img_header);
            vTitle = itemView.findViewById(R.id.title);
            vAuthor = itemView.findViewById(R.id.author);
            vComment = itemView.findViewById(R.id.comment);
            imgComment = itemView.findViewById(R.id.img_comment);
            vCollect = itemView.findViewById(R.id.collect);
            imgCollect = itemView.findViewById(R.id.img_collect);
            vLike = itemView.findViewById(R.id.like);
            imgLike = itemView.findViewById(R.id.img_like);

            mPlayerContainer = itemView.findViewById(R.id.player_container);
            mPrepareView = itemView.findViewById(R.id.prepare_view);
            mThumb = mPrepareView.findViewById(R.id.thumb);

            // 点击事件
            mPlayerContainer.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.e("PLAY", v.toString());
            switch (v.getId()) {
                case R.id.player_container:
                    if (mOnItemChildClickListener != null) {
                        mOnItemChildClickListener.onItemChildClick(mPosition);
                    }
                    break;
                default:
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mPosition);
                    }
            }
        }
    }
}
