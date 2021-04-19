package com.airtlab.news.entity;

import java.io.Serializable;

public class ApiProtocol<T>  implements Serializable {
    public String errCode;
    public String errMsg;
    public String retCode;
    public T data;
}

