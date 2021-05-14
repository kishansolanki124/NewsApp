package com.newsapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsGallery {

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
        @SerializedName("up_pro_img")
        private String up_pro_img;
        @SerializedName("nid")
        private String nid;
        @SerializedName("id")
        private String id;

        public String getUp_pro_img() {
            return up_pro_img;
        }

        public void setUp_pro_img(String up_pro_img) {
            this.up_pro_img = up_pro_img;
        }

        public String getNid() {
            return nid;
        }

        public void setNid(String nid) {
            this.nid = nid;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
