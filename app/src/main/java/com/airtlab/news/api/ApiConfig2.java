package com.airtlab.news.api;

public class ApiConfig2 {
    public static final int PAGE_SIZE = 5;
    public static final String BASE_URl = "http://renwu.airtlab.com/api";

    // 项目
    public static final String project_category = "/demand-category";
    public static final String project_list = "/demand";
    public static String getDetailUrl (int id) {
        return "/demand/" + String.valueOf(id);
    }
    public static final String comment_url = "/comment";

    // 用户
    public static final String user_login = "/user/login";
    public static final String user_register = "/user/register";
    public static final String user_userInfo = "/user/userInfo";

    // 公共
    public static final String sfCity = "/sfCity";
}
