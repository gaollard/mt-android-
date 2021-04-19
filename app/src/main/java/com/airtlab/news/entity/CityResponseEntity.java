package com.airtlab.news.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class CityResponseEntity extends ApiProtocol implements Serializable {
    public SFCityData data;
    public class SFCityData implements Serializable {
        public ArrayList<CityEntity> list;
    }
}


