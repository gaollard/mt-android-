package com.airtlab.news.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.airtlab.news.R;
import com.airtlab.news.adapter.VideoAdapter;
import com.airtlab.news.api.Api;
import com.airtlab.news.api.ApiCallback;
import com.airtlab.news.api.ApiConfig;
import com.airtlab.news.entity.VideoEntity;
import com.airtlab.news.entity.VideoListResponse;
import com.airtlab.news.listener.OnItemChildClickListener;
import com.airtlab.news.listener.OnItemClickListener;
import com.airtlab.news.util.Tag;
import com.airtlab.news.util.Utils;
import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videocontroller.component.CompleteView;
import com.dueeeke.videocontroller.component.ErrorView;
import com.dueeeke.videocontroller.component.GestureView;
import com.dueeeke.videocontroller.component.TitleView;
import com.dueeeke.videocontroller.component.VodControlView;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.player.VideoViewManager;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class VideoFragment extends BaseFragment implements OnItemChildClickListener {
    private int categoryId; // 分类ID
    private int pageNum = 1; // 分页索引
    private VideoAdapter videoAdapter; // 适配器
    private RefreshLayout refreshLayout; // 刷新&加载
    private List<VideoEntity> mVideoList = new ArrayList<>(); // 列表
    RecyclerView recyclerView; // RecyclerView
    private LinearLayoutManager linearLayoutManager;

    // 播放器相关
    protected VideoView mVideoView; // 播放视图
    protected StandardVideoController mController; // 播发控制器
    protected ErrorView mErrorView; // 播放失败
    protected CompleteView mCompleteView; // 播放完成
    protected TitleView mTitleView; // 播放器标题

    protected int mCurPos = -1; // 当前播放的位置
    protected int mLastPos = mCurPos; // 上次播放的位置，用于页面切回来之后恢复播放

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    videoAdapter.setDataList(mVideoList);
                    videoAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    public static VideoFragment newInstance(int categoryId) {
        VideoFragment fragment = new VideoFragment();
        fragment.categoryId = categoryId;
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView() {
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
        recyclerView = mRootView.findViewById(R.id.recyclerView);
        initVideoView();
    }

    @Override
    protected void initData() {
        // 布局管理器(给View设置布局方式)
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        videoAdapter = new VideoAdapter(getActivity(), mVideoList);
        videoAdapter.setOnItemChildClickListener(this);

        recyclerView.setAdapter(videoAdapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getVideoList(true);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                pageNum++;
                getVideoList(false);
            }
        });
        getVideoList(true);
    }

    /**
     * @description 播放器初始化
     */
    protected void initVideoView() {
        mVideoView = new VideoView(getActivity());
        mController = new StandardVideoController(getActivity());
        mErrorView = new ErrorView(getActivity());
        mController.addControlComponent(mErrorView);
        mCompleteView = new CompleteView(getActivity());
        mController.addControlComponent(mCompleteView);
        mTitleView = new TitleView(getActivity());
        mController.addControlComponent(mTitleView);
        mController.addControlComponent(new VodControlView(getActivity()));
        mController.addControlComponent(new GestureView(getActivity()));
        mController.setEnableOrientation(true);
        mVideoView.setVideoController(mController);
    }

    @Override
    public void onPause() {
        super.onPause();
        mVideoView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mVideoView.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVideoView.release();
    }

    /**
     * @description PrepareView 被点击
     */
    @Override
    public void onItemChildClick(int position) {
        startPlay(position);
    }

    /**
     * @description 开始播放
     * @param position
     */
    protected void startPlay(int position) {
        VideoEntity videoEntity = mVideoList.get(position);
        mVideoView.setUrl(videoEntity.getPlayurl());
        mTitleView.setTitle(videoEntity.getVtitle());

        // 找到对应的 itemView 以及 viewHolder
        View itemView = linearLayoutManager.findViewByPosition(position);
        if (itemView == null) return;
        VideoAdapter.ViewHolder viewHolder = (VideoAdapter.ViewHolder) itemView.getTag();

        // 把列表中预置的 PrepareView 添加到控制器中，注意isPrivate此处只能为true。
        mController.addControlComponent(viewHolder.mPrepareView, true);
        Utils.removeViewFormParent(mVideoView);
        viewHolder.mPlayerContainer.addView(mVideoView, 0);
        // 播放之前将 VideoView 添加到 VideoViewManager 以便在别的页面也能操作它
        getVideoViewManager().add(mVideoView, Tag.LIST);
        mVideoView.start();
    }

    private void releaseVideoView() {
        mVideoView.release();
//        if (mVideoView.isFullScreen()) {
//            mVideoView.stopFullScreen();
//        }
//        if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
//        mCurPos = -1;
    }

    protected VideoViewManager getVideoViewManager() {
        return VideoViewManager.instance();
    }

    private void getVideoList(final boolean isRefresh) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("page", pageNum);
        params.put("limit", ApiConfig.PAGE_SIZE);
        params.put("categoryId", categoryId);
        Log.e("Params", params.toString());
        Api.config(ApiConfig.VIDEO_LIST_BY_CATEGORY, params).getRequest(getActivity(), new ApiCallback() {
            @Override
            public void onSuccess(String res) {
                if (isRefresh) {
                    refreshLayout.finishRefresh(true);
                } else {
                    refreshLayout.finishLoadMore(true);
                }
                VideoListResponse response = new Gson().fromJson(res, VideoListResponse.class);
                if (response != null && response.getCode() == 0) {
                    List<VideoEntity> list = response.getPage().getList();
                    if (list != null && list.size() > 0) {
                        if (isRefresh) {
                            mVideoList = list;
                        } else {
                            mVideoList.addAll(list);
                        }
                        mHandler.sendEmptyMessage(0);
                    } else {
                        if (isRefresh) {
                            showToastSync("暂时无数据");
                        } else {
                            showToastSync("没有更多数据");
                        }
                    }
                }
            }

            @Override
            public void onFailure(final Exception e) {
                if (isRefresh) {
                    refreshLayout.finishRefresh(true);
                } else {
                    refreshLayout.finishLoadMore(true);
                }
            }
        });
    }
}