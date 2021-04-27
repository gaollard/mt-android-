package com.airtlab.news.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.airtlab.news.R;
import com.airtlab.news.api.Api;
import com.airtlab.news.api.Api2;
import com.airtlab.news.api.ApiCallback;
import com.airtlab.news.api.ApiConfig;
import com.airtlab.news.api.ApiConfig2;
import com.airtlab.news.entity.ApiProtocol;
import com.airtlab.news.entity.LoginResponse;
import com.airtlab.news.entity.UserEntity;
import com.airtlab.news.util.StringUtils;
import com.google.gson.Gson;

import java.util.HashMap;

public class LoginActivity extends BaseActivity {
    private EditText etAccount;
    private EditText etPwd;
    private Button btnLogin;

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        etAccount = findViewById(R.id.et_account);
        etPwd = findViewById(R.id.et_pwd);
        btnLogin = findViewById(R.id.btn_login);
    }

    @Override
    protected void initData() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString().trim();
                String pwd = etPwd.getText().toString().trim();
                login(account, pwd);
            }
        });
    }

    class LoginRes extends ApiProtocol {
        class Data extends UserEntity {
            public String token;
        }
        public Data data;
    }

    private void login(String account, String pwd) {
        if (StringUtils.isEmpty(account)) {
            showToast("请输入账号");
            return;
        }
        if (StringUtils.isEmpty(pwd)) {
            showToast("请输入密码");
            return;
        }

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("email", "1056834607@qq.com");
        params.put("password", "199389");

        Api2.config(ApiConfig2.user_login, params).postRequest(this, new ApiCallback() {
            @Override
            public void onSuccess(String res) {
                LoginRes loginRes = new Gson().fromJson(res, LoginRes.class);
                if (loginRes != null && loginRes.errCode.equals("0")) {
                    String token = loginRes.data.token;
                    insertVal("token", token);
                    insertVal("userInfo", loginRes.data.toString());
                    // 不允许返回启动页面
                    navigateToWithFlag(HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    showToastSync("登录失败" + res);
                }
            }
            @Override
            public void onFailure(Exception e) {
                showToast("登录失败" + e.toString());
            }
        });
    }
}