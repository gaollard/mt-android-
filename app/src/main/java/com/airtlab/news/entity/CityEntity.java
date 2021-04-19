package com.airtlab.news.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class CityEntity implements Serializable {
    public String areaId;
    public String areaName;
    public String parentId;
    public ArrayList<CityEntity> children;
}
