package com.airtlab.news.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProjectCategoryListResponse implements Serializable {

    public String errCode;
    public String errMsg;
    public String retCode;
    public Data data;

    public static class Data {
        public ArrayList<ProjectCategory> list;
    }
}
