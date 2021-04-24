package com.airtlab.news.fragment;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.airtlab.news.R;
import com.airtlab.news.entity.ProjectEntity;

public class ProjectDetailDetailFragment extends BaseFragment {
    private ProjectEntity detailData;

    @Override
    protected int initLayout() {
        return R.layout.activity_project_detail_detail;
    }

    public void setDetailData(ProjectEntity detailData) {
        this.detailData = detailData;
        this.updateView();
    }

    private void updateView() {
        TextView project_title = mRootView.findViewById(R.id.project_title);
        TextView project_reward = mRootView.findViewById(R.id.project_reward);
        TextView project_skill = mRootView.findViewById(R.id.project_skill);
        TextView project_create_time = mRootView.findViewById(R.id.project_create_time);
        TextView project_desc = mRootView.findViewById(R.id.project_desc);

        project_title.setText(detailData.title);
        project_reward.setText(String.valueOf(detailData.reward / 100));
        project_skill.setText(detailData.requires);
        project_desc.setText(detailData.description);
        project_create_time.setText(detailData.createTime);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() { }
}
