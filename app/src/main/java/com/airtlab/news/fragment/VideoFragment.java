package com.airtlab.news.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.airtlab.news.R;
import com.airtlab.news.adapter.VideoAdapter;
import com.airtlab.news.api.Api;
import com.airtlab.news.api.ApiCallback;
import com.airtlab.news.api.ApiConfig;
import com.airtlab.news.entity.VideoEntity;
import com.airtlab.news.entity.VideoListResponse;
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
public class VideoFragment extends BaseFragment {
    private int categoryId; // 分类ID
    private int pageNum = 1; // 分页索引
    private VideoAdapter videoAdapter; // 适配器
    private RefreshLayout refreshLayout; // 刷新&加载
    private List<VideoEntity> mVideoList = new ArrayList<>(); // 列表

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
        // 刷新&加载
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
        // 布局管理器(给View设置布局方式)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // RecyclerView
        RecyclerView recyclerView = mRootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        videoAdapter = new VideoAdapter(getActivity(), mVideoList);
        recyclerView.setAdapter(videoAdapter);
    }

    @Override
    protected void initData() {
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