package com.airtlab.news.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;

import com.airtlab.news.R;
import com.airtlab.news.jsbridge.BridgeHandler;
import com.airtlab.news.jsbridge.BridgeWebView;
import com.airtlab.news.jsbridge.CallBackFunction;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("SetJavaScriptEnabled")
public class SearchActivity extends BaseActivity {
    private BridgeWebView bridgeWebView;
    private String url;

    @Override
    protected int initLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
//        bridgeWebView = findViewById(R.id.bridgeWebView);
        //
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
        }
//        registerJavaHandler();
//        initWebView();
    }

    private void initWebView() {
        WebSettings settings = bridgeWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        bridgeWebView.loadUrl(url);
    }

    private void registerJavaHandler() {
        bridgeWebView.registerHandler("goBack", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                finish();
            }
        });
        bridgeWebView.registerHandler("httpRequest", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                // 创建数据
                HashMap<String, Object> json = new HashMap<>();
                json.put("data", new ArrayList<>());
                JSONObject jsonObject = new JSONObject(json);
                String jsonStr = jsonObject.toString();

                // 回调
                function.onCallBack(jsonStr);
            }
        });
    }
}
