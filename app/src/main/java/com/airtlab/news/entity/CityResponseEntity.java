package com.airtlab.news.entity;

import java.io.Serializable;

class SFCityData implements Serializable {
    public CityEntity[] list;
}

public class CityResponseEntity extends ApiProtocol<SFCityData> implements Serializable {

}


