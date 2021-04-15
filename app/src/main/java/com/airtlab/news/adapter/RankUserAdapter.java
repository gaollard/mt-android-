package com.airtlab.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airtlab.news.R;
import com.airtlab.news.entity.RankUserEntity;
import com.airtlab.news.view.CircleTransform;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RankUserAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<RankUserEntity> mDataList = new ArrayList<>(); // 列表数据
    private OnItemClickListener mOnItemClickListener;

    /**
     * 设置回调
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    /**
     * 构造器
     * @param context
     */
    public RankUserAdapter(Context context) {
        this.mContext = context;
    }
    public RankUserAdapter(Context context, List<RankUserEntity> list) {
        mContext = context;
        mDataList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_rank_user_item, parent, false);
        return new ViewHolderOne(view);
    }

    /**
     * @desc 设置列表数据，不通过构造器，避免反复创建 Adapter
     * @param list
     */
    public void setDataList(List<RankUserEntity> list) {
        this.mDataList = list;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RankUserEntity entity = mDataList.get(position);
        ViewHolderOne vh = (ViewHolderOne) holder;

        vh.entity = entity;
        vh.ur_sort.setText(String.valueOf(position + 1));
        vh.ur_nickname.setText(entity.nickname == null ? "-" : entity.nickname);
        vh.ur_words.setText(entity.words == null ? "该用户啥也没说~" : entity.words);
        vh.ur_achieve.setText(entity.rcoinNum == null ? "0" : entity.rcoinNum);

        Picasso.with(mContext)
                .load(entity.avatar)
                .into(vh.ur_avatar);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHolderOne extends RecyclerView.ViewHolder {
        private TextView ur_sort;
        private TextView ur_nickname;
        private TextView ur_words;
        private ImageView ur_avatar;
        private TextView ur_achieve;
        private RankUserEntity entity;

        public ViewHolderOne(@NonNull View view) {
            super(view);
            ur_sort = view.findViewById(R.id.ur_sort);
            ur_nickname = view.findViewById(R.id.ur_nickname);
            ur_words = view.findViewById(R.id.ur_words);
            ur_avatar = view.findViewById(R.id.ur_avatar);
            ur_achieve = view.findViewById(R.id.ur_achieve);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickListener.onItemClick(entity);
//                }
//            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Serializable obj);
    }
}
