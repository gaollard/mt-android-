package com.airtlab.news.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airtlab.news.R;
import com.airtlab.news.api.Api;
import com.airtlab.news.api.Api2;
import com.airtlab.news.api.ApiCallback;
import com.airtlab.news.api.ApiConfig;
import com.airtlab.news.api.ApiConfig2;
import com.airtlab.news.entity.ApiProtocol;
import com.airtlab.news.entity.NewsEntity;
import com.airtlab.news.entity.NewsListResponse;
import com.airtlab.news.entity.UserEntity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MyFragment extends BaseFragment {
    private UserEntity userInfo;
    private static int GET_USER_INFO_SUCCESS = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    afterGetUserInfo();
                    break;
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.fragment_account;
    }

    @Override
    protected void initView() {

    }

    private void afterGetUserInfo() {

    }

    @Override
    protected void initData() {
        this.getUseInfo();
    }

    class UserInfoRes extends ApiProtocol {
        public UserEntity data;
    }

    /**
     * @desc 获取用户信息
     */
    private void getUseInfo() {
        HashMap<String, Object> params = new HashMap<>();
        Api2.config(ApiConfig2.user_userInfo, params).getRequest(getActivity(), new ApiCallback() {
            @Override
            public void onSuccess(final String res) {
                UserInfoRes userInfoRes = new Gson().fromJson(res, UserInfoRes.class);
                if (userInfoRes != null && userInfoRes.errCode.equals("0")) {
                    userInfo = userInfoRes.data;
                    mHandler.sendEmptyMessage(GET_USER_INFO_SUCCESS);
                }
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("user_userInfo failed", e.toString());
            }
        });
    }

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }
}