package com.airtlab.news.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.airtlab.news.R;
import com.airtlab.news.api.Api2;
import com.airtlab.news.api.ApiCallback;
import com.airtlab.news.api.ApiConfig2;
import com.airtlab.news.entity.ApiProtocol;
import com.airtlab.news.entity.ProjectEntity;
import com.airtlab.news.fragment.ProjectDetailDetailFragment;
import com.google.gson.Gson;
import java.io.Serializable;

public class DetailActivity extends BaseActivity {
    private ProjectEntity detailData;

    @Override
    protected int initLayout() {
        return R.layout.activity_project_detail;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    updateView();
                    break;
            }
        }
    };

    @Override
    protected void initView() {
        findViewById(R.id.page_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        TextView textView = findViewById(R.id.page_header_title);
        textView.setText(title);
        this.getData();
    }

    private void updateView() {
        FragmentManager fm = getSupportFragmentManager();
        ProjectDetailDetailFragment fragment = (ProjectDetailDetailFragment)fm.findFragmentById(R.id.detail_detail);
        fragment.setDetailData(this.detailData);
    }

    class ProjectDetailResponseEntity extends ApiProtocol implements Serializable {
        public ProjectEntity data;
    }

    /**
     * @desc 获取项目详情
     */
    private void getData() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Api2.config(ApiConfig2.getDetailUrl(Integer.parseInt(id)), null).getRequest(this, new ApiCallback() {
            @Override
            public void onSuccess(final String res) {
                ProjectDetailResponseEntity response = new Gson().fromJson(res, ProjectDetailResponseEntity.class);
                if (response != null && (response.errCode.equals("0"))) {
                    Log.e(this.getClass().getName(), response.data.title);
                    detailData = response.data;
                    mHandler.sendEmptyMessage(0);
                } else {
                    Log.e(this.getClass().getName(), res);
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.e(this.getClass().getName(), e.toString());
            }
        });
    }
}