package com.airtlab.news.entity;

import java.io.Serializable;
import java.util.List;

public class ProjectListResponse implements Serializable {

    public String errCode;
    public String errMsg;
    public String retCode;
    public Data data;

    public static class Data {
        public List<ProjectEntity> list;
    }
}
