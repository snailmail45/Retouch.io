package com.evan.scanenhancer.data.model;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

public class Data{
    @ColumnInfo(name = "tx_id")
    @SerializedName("id")
    private String id;

    @ColumnInfo(name = "url")
    @SerializedName("url")
    private String url;

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}