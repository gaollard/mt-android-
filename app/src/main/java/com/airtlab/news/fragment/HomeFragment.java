package com.airtlab.news.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airtlab.news.R;
import com.airtlab.news.activity.UserRankActivity;
import com.airtlab.news.adapter.HomeAdapter;
import com.airtlab.news.api.Api;
import com.airtlab.news.api.Api2;
import com.airtlab.news.api.ApiCallback;
import com.airtlab.news.api.ApiConfig;
import com.airtlab.news.api.ApiConfig2;
import com.airtlab.news.entity.CategoryEntity;
import com.airtlab.news.entity.ProjectCategory;
import com.airtlab.news.entity.ProjectCategoryListResponse;
import com.airtlab.news.entity.VideoCategoryResponse;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = v.findViewById(R.id.fixedViewPager);
        slidingTabLayout = v.findViewById(R.id.slidingTabLayout);
        return v;
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.go_to_user_rank).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("hello", "world");
                Bundle bundle = new Bundle();
                navigateToWithBundle(UserRankActivity.class, bundle);
            }
        });
        getProjectCategoryList();
    }

    @Override
    protected int initLayout() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    private void getProjectCategoryList() {
        HashMap<String, Object> params = new HashMap<>();
        Api2.config(ApiConfig2.project_category, params).getRequest(getActivity(), new ApiCallback() {
            @Override
            public void onSuccess(String res) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProjectCategoryListResponse response = new Gson().fromJson(res, ProjectCategoryListResponse.class);
                        if (response != null && response.errCode.equals("0")) {
                            List<ProjectCategory> list = response.data.list;
                            if (list != null && list.size() > 0) {
                                mTitles = new String[list.size()];
                                for (int i = 0; i < list.size(); i++) {
                                    mTitles[i] = list.get(i).name;
                                    mFragments.add(ProjectFragment.newInstance(list.get(i).id));
                                }
                                viewPager.setOffscreenPageLimit(mFragments.size());
                                viewPager.setAdapter(new HomeAdapter(getFragmentManager(), mTitles, mFragments));
                                slidingTabLayout.setViewPager(viewPager);
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(ApiConfig2.project_category + " onFailure", e.getMessage());
            }
        });
    }
}