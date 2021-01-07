package com.airtlab.news.entity;

public class ProjectEntity {
    public String title;
    public int reward;
    public String requires;

    public ProjectEntity(String title, int reward, String requires) {
        this.title = title;
        this.reward = reward;
        this.requires = requires;
    }
}
