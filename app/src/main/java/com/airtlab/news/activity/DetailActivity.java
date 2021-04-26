package com.airtlab.news.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airtlab.news.R;
import com.airtlab.news.adapter.CommentAdapter;
import com.airtlab.news.api.Api2;
import com.airtlab.news.api.ApiCallback;
import com.airtlab.news.api.ApiConfig2;
import com.airtlab.news.entity.ApiProtocol;
import com.airtlab.news.entity.CommentEntity;
import com.airtlab.news.entity.ProjectEntity;
import com.airtlab.news.fragment.ProjectDetailDetailFragment;
import com.google.gson.Gson;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class DetailActivity extends BaseActivity {
    private ProjectEntity detailData;
    private List<CommentEntity> commentList;
    private RecyclerView commentListView;
    private CommentAdapter commentAdapter;
    private LinearLayout msg_edit;              // 留言容器
    private LinearLayout btm_action_bar;        // 底部操作栏
    private LinearLayout btm_action_msg;        // 留言容器
    private EditText msg_edit_control;          // 输入框
    private TextView msg_edit_send;             // 发送按钮

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
                case 1:
                    commentAdapter.setDataList(commentList);
                    commentAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void initView() {
        commentListView = findViewById(R.id.comment_list);
        msg_edit = findViewById(R.id.msg_edit);
        btm_action_bar = findViewById(R.id.btm_action_bar);
        btm_action_msg = findViewById(R.id.btm_action_msg);
        msg_edit_control = findViewById(R.id.msg_edit_control);
        msg_edit_send = findViewById(R.id.msg_edit_send);
    }

    @Override
    protected void initData() {
        // 初始化 recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentListView.setLayoutManager(linearLayoutManager);

        commentAdapter = new CommentAdapter(this);
        commentListView.setAdapter(commentAdapter);
        this.getData();
        this.getComment();

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        TextView textView = findViewById(R.id.page_header_title);
        textView.setText(title);

        // 返回
        findViewById(R.id.page_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 点击留言
        btm_action_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btm_action_bar.setVisibility(View.GONE); // 隐藏底部操作栏
                msg_edit.setVisibility(View.VISIBLE);    // 显示留言输入框
            }
        });

        // 点击发送
        msg_edit_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(this.getClass().getName(), "click send");
            }
        });

        msg_edit_control.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // IME_ACTION_SEND 发送
                // IME_ACTION_DONE 完成
                // IME_ACTION_SEARCH 搜索
                Log.i("---","搜索操作执行" + String.valueOf(actionId));
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                }
                return false;
            }
        });
    }

    private void updateView() {
        FragmentManager fm = getSupportFragmentManager();
        ProjectDetailDetailFragment fragment = (ProjectDetailDetailFragment)fm.findFragmentById(R.id.detail_detail);
        fragment.setDetailData(this.detailData);
    }

    class ProjectDetailResponseEntity extends ApiProtocol implements Serializable {
        public ProjectEntity data;
    }

    class CommentRes extends ApiProtocol {
        public Data data;
        class Data {
            public List<CommentEntity> list;
        }
    }

    private void getComment() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        HashMap<String, Object> params = new HashMap<>();
        params.put("itemId", id);
        params.put("typeId", 2);
        // http://renwu.airtlab.com/api/comment?itemId=56&typeId=2&

        Api2.config(ApiConfig2.comment_url, params).getRequest(this, new ApiCallback() {
            @Override
            public void onSuccess(final String res) {
                CommentRes response = new Gson().fromJson(res, CommentRes.class);
                if (response != null && (response.errCode.equals("0"))) {
                    commentList = response.data.list;
                    Log.e("comment_list", res);
                    mHandler.sendEmptyMessage(1);
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