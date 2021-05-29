package com.excuseme.newsapp.model;

import java.util.List;

public class OtherStories {

    @com.google.gson.annotations.SerializedName("data")
    private List<Data> data;
    @com.google.gson.annotations.SerializedName("msg")
    private String msg;
    @com.google.gson.annotations.SerializedName("success")
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
        @com.google.gson.annotations.SerializedName("status")
        private String status;
        @com.google.gson.annotations.SerializedName("user_mobile")
        private String user_mobile;
        @com.google.gson.annotations.SerializedName("upload_by")
        private String upload_by;
        @com.google.gson.annotations.SerializedName("pdate")
        private String pdate;
        @com.google.gson.annotations.SerializedName("trending_news")
        private String trending_news;
        @com.google.gson.annotations.SerializedName("keywords")
        private String keywords;
        @com.google.gson.annotations.SerializedName("author")
        private String author;
        @com.google.gson.annotations.SerializedName("up_pro_img")
        private String up_pro_img;
        @com.google.gson.annotations.SerializedName("description")
        private String description;
        @com.google.gson.annotations.SerializedName("name")
        private String name;
        @com.google.gson.annotations.SerializedName("cid")
        private String cid;
        @com.google.gson.annotations.SerializedName("city")
        private String city;
        @com.google.gson.annotations.SerializedName("id")
        private String id;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUser_mobile() {
            return user_mobile;
        }

        public void setUser_mobile(String user_mobile) {
            this.user_mobile = user_mobile;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
