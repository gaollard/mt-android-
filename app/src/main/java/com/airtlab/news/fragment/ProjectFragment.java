package com.airtlab.news.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airtlab.news.R;
import com.airtlab.news.activity.WebActivity;
import com.airtlab.news.adapter.NewsAdapter;
import com.airtlab.news.adapter.ProjectAdapter;
import com.airtlab.news.api.Api;
import com.airtlab.news.api.Api2;
import com.airtlab.news.api.ApiCallback;
import com.airtlab.news.api.ApiConfig;
import com.airtlab.news.entity.NewsEntity;
import com.airtlab.news.entity.NewsListResponse;
import com.airtlab.news.entity.ProjectEntity;
import com.airtlab.news.entity.ProjectListResponse;
import com.airtlab.news.view.SpacesItemDecoration;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProjectFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private ProjectAdapter projectAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<ProjectEntity> newsList = new ArrayList();
    private int pageNum = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    projectAdapter.setDataList(newsList);
                    projectAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    public static ProjectFragment newInstance() {
        ProjectFragment fragment = new ProjectFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_project;
    }

    @Override
    protected void initView() {
        recyclerView = mRootView.findViewById(R.id.recyclerView);
//        SpacesItemDecoration decoration = new SpacesItemDecoration(40);
//        recyclerView.addItemDecoration(decoration);
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
    }

    @Override
    protected void initData() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        projectAdapter = new ProjectAdapter(getActivity());
        recyclerView.setAdapter(projectAdapter);

//        projectAdapter.setOnItemClickListener(new ProjectAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Serializable obj) {
//                NewsEntity newsEntity = (NewsEntity) obj;
////                String url = "http://192.168.31.32:8089/newsDetail?title=" + newsEntity.getAuthorName();
//                String url = "http://renwu.airtlab.com/project/56";
//                Bundle bundle = new Bundle();
//                bundle.putString("url", url);
//                navigateToWithBundle(WebActivity.class, bundle);
//            }
//        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getNewsList(true);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                pageNum++;
                getNewsList(false);
            }
        });
        getNewsList(true);
    }

    private void getNewsList(final boolean isRefresh) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("pageSize", ApiConfig.PAGE_SIZE);
        params.put("pageIndex", pageNum);

        Api2.config(ApiConfig.project_list, params).getRequest(getActivity(), new ApiCallback() {
            @Override
            public void onSuccess(final String res) {
                if (isRefresh) {
                    refreshLayout.finishRefresh(true);
                } else {
                    refreshLayout.finishLoadMore(true);
                }
                ProjectListResponse response = new Gson().fromJson(res, ProjectListResponse.class);
                if (response != null && (response.errCode.equals("0"))) {
                    List<ProjectEntity> list = response.data.list;
                    if (list != null && list.size() > 0) {
                        if (isRefresh) {
                            newsList = list;
                        } else {
                            newsList.addAll(list);
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