package com.newsapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Bookmarks {

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

    public static class Data implements Serializable {
        @SerializedName("status")
        private String status;
        @SerializedName("upload_by")
        private String upload_by;
        @SerializedName("pdate")
        private String pdate;
        @SerializedName("slider")
        private String slider;
        @SerializedName("trending_news")
        private String trending_news;
        @SerializedName("keywords")
        private String keywords;
        @SerializedName("author")
        private String author;
        @SerializedName("up_pro_img")
        private String up_pro_img;
        @SerializedName("description")
        private String description;
        @SerializedName("name")
        private String name;
        @SerializedName("cid")
        private String cid;
        @SerializedName("city")
        private String city;
        @SerializedName("user_mobile")
        private String user_mobile;
        @SerializedName("nid")
        private String nid;
        @SerializedName("id")
        private String id;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUpload_by() {
            return upload_by;
        }

        public void setUpload_by(String upload_by) {
            this.upload_by = upload_by;
        }

        public String getPdate() {
            return pdate;
        }

        public void setPdate(String pdate) {
            this.pdate = pdate;
        }

        public String getSlider() {
            return slider;
        }

        public void setSlider(String slider) {
            this.slider = slider;
        }

        public String getTrending_news() {
            return trending_news;
        }

        public void setTrending_news(String trending_news) {
            this.trending_news = trending_news;
        }

        public String getKeywords() {
            return keywords;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getUp_pro_img() {
            return up_pro_img;
        }

        public void setUp_pro_img(String up_pro_img) {
            this.up_pro_img = up_pro_img;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getUser_mobile() {
            return user_mobile;
        }

        public void setUser_mobile(String user_mobile) {
            this.user_mobile = user_mobile;
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
