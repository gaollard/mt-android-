package com.airtlab.news.entity;

public class ProjectEntity {
    public String id; // 需求ID
    public String title; // 标题
    public int reward;   // 赏金
    public String requires; // 要求
    public String appType; // 应用类型
    public String projectType; // 项目类型
    public String createTime; // 创建时间
    public String description; // 需求的项目描述

    public ProjectEntity(String title, int reward, String requires) {
        this.title = title;
        this.reward = reward;
        this.requires = requires;
    }
}
