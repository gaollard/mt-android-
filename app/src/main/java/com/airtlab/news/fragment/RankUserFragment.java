package com.airtlab.news.fragment;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airtlab.news.R;
import com.airtlab.news.adapter.RankUserAdapter;
import com.airtlab.news.api.Api2;
import com.airtlab.news.api.ApiCallback;
import com.airtlab.news.api.ApiConfig;
import com.airtlab.news.entity.RankUserEntity;
import com.airtlab.news.entity.RankUserListResponse;
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
public class RankUserFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private RankUserAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<RankUserEntity> dataList = new ArrayList();
    private int pageNum = 1;
    private int pageSize = 10;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mAdapter.setDataList(dataList);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    public static RankUserFragment newInstance() {
        RankUserFragment fragment = new RankUserFragment();
        return fragment;
    }

    public static RankUserFragment newInstance(int categoryId) {
        RankUserFragment fragment = new RankUserFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_rank_user;
    }

    @Override
    protected void initView() {
        recyclerView = mRootView.findViewById(R.id.recyclerView);
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
    }

    @Override
    protected void initData() {
        // 初始化 recyclerView
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        // 初始化 adapter
        mAdapter = new RankUserAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getDataList(true);
            }
        });
//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
//                pageNum++;
//                getDataList(false);
//            }
//        });
        getDataList(true);
    }

    /**
     * @desc 获取列表数据
     * @param isRefresh
     */
    private void getDataList(final boolean isRefresh) {
        // 请求参数构建
        HashMap<String, Object> params = new HashMap<>();
        params.put("pageSize", ApiConfig.PAGE_SIZE);
        params.put("pageIndex", pageNum);

        // 发送请求
        Api2.config(ApiConfig.user_rank_list, params).getRequest(getActivity(), new ApiCallback() {
            @Override
            public void onSuccess(final String res) {
                if (isRefresh) {
                    refreshLayout.finishRefresh(true);
                } else {
                    refreshLayout.finishLoadMore(true);
                }
                RankUserListResponse response = new Gson().fromJson(res, RankUserListResponse.class);
                if (response != null && (response.errCode.equals("0"))) {
                    List<RankUserEntity> list = response.data.list;
                    if (list != null && list.size() > 0) {
                        if (isRefresh) {
                            dataList = list;
                        } else {
                            dataList.addAll(list);
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
            public void onFailure(Exception e) {
                if (isRefresh) {
                    refreshLayout.finishRefresh(true);
                } else {
                    refreshLayout.finishLoadMore(true);
                }
            }
        });
    }
}