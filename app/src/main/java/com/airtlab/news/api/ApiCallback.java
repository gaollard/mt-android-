package com.airtlab.news.api;

public interface ApiCallback {
    void onSuccess(String res);

    void onFailure(Exception e);
}
