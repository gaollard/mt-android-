package com.airtlab.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airtlab.news.R;
import com.airtlab.news.entity.NewsEntity;
import com.airtlab.news.view.CircleTransform;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter {
    private Context mContext; // Activity Context
    private List<NewsEntity> mNewsList = new ArrayList<>(); // 列表数据
    private OnItemClickListener mOnItemClickListener; // 点击 Item 回调

    /**
     * 设置点击 Item 回调
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
    
    /**
     * 构造器，无列表数据
     * @param context
     */
    public NewsAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * 构造器，有列表数据
     * @param context
     * @param list
     */
    public NewsAdapter(Context context, List<NewsEntity> list) {
        mContext = context;
        mNewsList = list;
    }

    /**
     * @desc 设置列表数据，不通过构造器，避免反复创建 Adapter
     * @param list
     */
    public void setDataList(List<NewsEntity> list) {
        this.mNewsList = list;
        // 这里不需要手动调用刷新吗？像 IOS UITableView 那样
    }

    /**
     * @description  获取 itemView 类型
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        int type = mNewsList.get(position).getType();
        return type;
    }

    /**
     * onCreateViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.news_item_one, parent, false);
            return new ViewHolderOne(view);
        } else if (viewType == 2) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.news_item_two, parent, false);
            return new ViewHolderTwo(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.news_item_three, parent, false);
            return new ViewHolderThree(view);
        }
    }

    // onBindViewHolder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        NewsEntity newsEntity = mNewsList.get(position);
        if (type == 1) {
            ViewHolderOne vh = (ViewHolderOne) holder;
            vh.title.setText(newsEntity.getNewsTitle());
            vh.author.setText(newsEntity.getAuthorName());
            vh.comment.setText(newsEntity.getCommentCount() + "评论 .");
            vh.time.setText(newsEntity.getReleaseDate());
            vh.newsEntity = newsEntity;
            Picasso.with(mContext)
                    .load(newsEntity.getHeaderUrl())
                    .transform(new CircleTransform())
                    .into(vh.header);

            Picasso.with(mContext)
                    .load(newsEntity.getThumbEntities().get(0).getThumbUrl())
                    .into(vh.thumb);
        } else if (type == 2) {
            ViewHolderTwo vh = (ViewHolderTwo) holder;
            vh.title.setText(newsEntity.getNewsTitle());
            vh.author.setText(newsEntity.getAuthorName());
            vh.comment.setText(newsEntity.getCommentCount() + "评论 .");
            vh.time.setText(newsEntity.getReleaseDate());
            vh.newsEntity = newsEntity;
            Picasso.with(mContext)
                    .load(newsEntity.getHeaderUrl())
                    .transform(new CircleTransform())
                    .into(vh.header);

            Picasso.with(mContext)
                    .load(newsEntity.getThumbEntities().get(0).getThumbUrl())
                    .into(vh.pic1);
            Picasso.with(mContext)
                    .load(newsEntity.getThumbEntities().get(1).getThumbUrl())
                    .into(vh.pic2);
            Picasso.with(mContext)
                    .load(newsEntity.getThumbEntities().get(2).getThumbUrl())
                    .into(vh.pic3);
        } else {
            ViewHolderThree vh = (ViewHolderThree) holder;
            vh.title.setText(newsEntity.getNewsTitle());
            vh.author.setText(newsEntity.getAuthorName());
            vh.comment.setText(newsEntity.getCommentCount() + "评论 .");
            vh.time.setText(newsEntity.getReleaseDate());
            vh.newsEntity = newsEntity;
            Picasso.with(mContext)
                    .load(newsEntity.getHeaderUrl())
                    .transform(new CircleTransform())
                    .into(vh.header);

            Picasso.with(mContext)
                    .load(newsEntity.getThumbEntities().get(0).getThumbUrl())
                    .into(vh.thumb);
        }
    }

    // 长度获取
    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    // Item Holder
    public class ViewHolderOne extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView author;
        private TextView comment;
        private TextView time;
        private ImageView header;
        private ImageView thumb;
        private NewsEntity newsEntity;

        public ViewHolderOne(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            comment = view.findViewById(R.id.comment);
            time = view.findViewById(R.id.time);
            header = view.findViewById(R.id.header);
            thumb = view.findViewById(R.id.thumb);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(newsEntity);
                }
            });
        }
    }

    // Item Holder
    public class ViewHolderTwo extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView author;
        private TextView comment;
        private TextView time;
        private ImageView header;
        private ImageView pic1, pic2, pic3;
        private NewsEntity newsEntity;

        public ViewHolderTwo(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            comment = view.findViewById(R.id.comment);
            time = view.findViewById(R.id.time);
            header = view.findViewById(R.id.header);
            pic1 = view.findViewById(R.id.pic1);
            pic2 = view.findViewById(R.id.pic2);
            pic3 = view.findViewById(R.id.pic3);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(newsEntity);
                }
            });
        }
    }

    // Item Holder
    public class ViewHolderThree extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView author;
        private TextView comment;
        private TextView time;
        private ImageView header;
        private ImageView thumb;
        private NewsEntity newsEntity;

        public ViewHolderThree(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            comment = view.findViewById(R.id.comment);
            time = view.findViewById(R.id.time);
            header = view.findViewById(R.id.header);
            thumb = view.findViewById(R.id.thumb);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(newsEntity);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Serializable obj);
    }
}
