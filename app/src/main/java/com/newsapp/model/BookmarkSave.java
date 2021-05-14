package com.newsapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookmarkSave {

    @SerializedName("data")
    private List<String> data;
    @SerializedName("success")
    private boolean success;
    @SerializedName("msg")
    private String msg;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
