package com.airtlab.news.bean;

import com.contrarywind.interfaces.IPickerViewData;

public class ProvinceBean implements IPickerViewData {
    private String areaId;
    private String areaName;
    private String description;

    public ProvinceBean(String areaId, String areaName, String description){
        this.areaId = areaId;
        this.areaName = areaName;
        this.description = description;
    }

    public String getId() {
        return areaId;
    }

    public void setId(String id) {
        this.areaId = id;
    }

    public String getName() {
        return areaName;
    }

    public void setName(String areaName) {
        this.areaName = areaName;
    }

    //这个用来显示在PickerView上面的字符串,PickerView会通过getPickerViewText方法获取字符串显示出来。
    @Override
    public String getPickerViewText() {
        return areaName;
    }
}
