package com.airtlab.news.entity;

import java.io.Serializable;

public class CityEntity implements Serializable {
    private String areaId;
    private String areaName;
    private String parentId;
    private CityEntity[] children;
}
