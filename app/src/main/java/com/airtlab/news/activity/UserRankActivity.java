package com.airtlab.news.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;

import com.airtlab.news.R;
import com.airtlab.news.jsbridge.BridgeHandler;
import com.airtlab.news.jsbridge.BridgeWebView;
import com.airtlab.news.jsbridge.CallBackFunction;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("SetJavaScriptEnabled")
public class UserRankActivity extends BaseActivity {
//    private BridgeWebView bridgeWebView;
    private String url;

    @Override
    protected int initLayout() {
        return R.layout.activity_user_rank;
    }

    @Override
    protected void initView() {
        // bridgeWebView = findViewById(R.id.bridgeWebView);
    }

    @Override
    protected void initData() {

    }
}
