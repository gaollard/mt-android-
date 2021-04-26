package com.airtlab.news.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import java.util.Timer;
import java.util.TimerTask;

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
        btm_action_bar.setVisibility(View.VISIBLE);
        msg_edit.setVisibility(View.GONE);

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
                focusControl();
                // 获取焦点
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        Log.v("timer", String.valueOf(500));
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 500);
            }
        });

        // 点击发送
        msg_edit_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(this.getClass().getName(), "click send");
            }
        });

        // 监听确认
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

    public void focusControl() {
        msg_edit_control.setFocusable(true);
        msg_edit_control.setFocusableInTouchMode(true);
        msg_edit_control.requestFocus();
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(msg_edit_control, 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            View v = getCurrentFocus();
            boolean  hideInputResult = isShouldHideInput(v,ev);
            Log.v("hideInputResult","zzz-->>" + hideInputResult);
            if(hideInputResult){
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) DetailActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                if(v != null){
                    if(imm.isActive()){
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                msg_edit.setVisibility(View.GONE);
                btm_action_bar.setVisibility(View.VISIBLE);
                Log.v("hide", hideInputResult + "");
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            // 之前一直不成功的原因是,getX获取的是相对父视图的坐标,getRawX获取的才是相对屏幕原点的坐标！！！
            Log.v("leftTop[]","zz--left:"+left+"--top:"+top+"--bottom:"+bottom+"--right:"+right);
            Log.v("event","zz--getX():"+event.getRawX()+"--getY():"+event.getRawY());
            if (event.getRawX() > left && event.getRawX() < right
                    && event.getRawY() > top && event.getRawY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * @desc 获取项目详情后，更新数据
     */
    private void updateView() {
        FragmentManager fm = getSupportFragmentManager();
        ProjectDetailDetailFragment fragment = (ProjectDetailDetailFragment)fm.findFragmentById(R.id.detail_detail);
        fragment.setDetailData(this.detailData);
    }

    /**
     * @desc 项目详情
     */
    class ProjectDetailResponseEntity extends ApiProtocol implements Serializable {
        public ProjectEntity data;
    }

    /**
     * @desc 评论接口
     */
    class CommentRes extends ApiProtocol {
        public Data data;
        class Data {
            public List<CommentEntity> list;
        }
    }

    /**
     * @desc 获取评论列表
     */
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