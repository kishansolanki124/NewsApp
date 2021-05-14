package com.newsapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Keyword {

    @SerializedName("data")
    private List<Data> data;
    @SerializedName("msg")
    private String msg;
    @SerializedName("success")
    private boolean success;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class Data {
        @SerializedName("popular_keyword")
        private String popular_keyword;
        @SerializedName("name")
        private String name;
        @SerializedName("id")
        private String id;

        public String getPopular_keyword() {
            return popular_keyword;
        }

        public void setPopular_keyword(String popular_keyword) {
            this.popular_keyword = popular_keyword;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
