package com.airtlab.news.entity;

import java.io.Serializable;
import java.util.List;

public class RankUserListResponse implements Serializable {
    public String errCode;
    public String errMsg;
    public String retCode;
    public Data data;

    public String getCode() {
        return errCode;
    }

    public static class Data {
        public List<RankUserEntity> list;
    }
}
