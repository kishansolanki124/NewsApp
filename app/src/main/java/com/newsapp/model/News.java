package com.newsapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class News {

    @SerializedName("data")
    private List<Data> data;
    @SerializedName("Current_page")
    private String Current_page;
    @SerializedName("Total_page")
    private int Total_page;
    @SerializedName("msg")
    private String msg;
    @SerializedName("success")
    private boolean success;
    @SerializedName("news_banner")
    private List<NewsBanner> news_banner = new ArrayList();

    public boolean isSuccess() {
        return success;
    }

    public List<NewsBanner> getNews_banner() {
        return news_banner;
    }

    public void setNews_banner(List<NewsBanner> news_banner) {
        this.news_banner = news_banner;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getCurrent_page() {
        return Current_page;
    }

    public void setCurrent_page(String Current_page) {
        this.Current_page = Current_page;
    }

    public int getTotal_page() {
        return Total_page;
    }

    public void setTotal_page(int Total_page) {
        this.Total_page = Total_page;
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
        @SerializedName("author_img")
        private String author_img;
        @SerializedName("view_type")
        private int view_type;
        @SerializedName("user_mobile")
        private String user_mobile;
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
        @SerializedName("id")
        private String id;
        @SerializedName("isbookmark")
        private String isbookmark;
        @SerializedName("web_link")
        private String web_link;
        private String numRecords;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAuthor_img() {
            return author_img;
        }

        public String getNumRecords() {
            return numRecords;
        }

        public void setNumRecords(String numRecords) {
            this.numRecords = numRecords;
        }

        public void setAuthor_img(String author_img) {
            this.author_img = author_img;
        }

        public String getnumRecords() {
            return numRecords;
        }

        public void setnumRecords(String numRecords) {
            this.numRecords = numRecords;
        }

        public int getView_type() {
            return view_type;
        }

        public void setView_type(int view_type) {
            this.view_type = view_type;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIsbookmark() {
            return isbookmark;
        }

        public void setIsbookmark(String isbookmark) {
            this.isbookmark = isbookmark;
        }

        public String getWeb_link() {
            return web_link;
        }

        public void setWeb_link(String web_link) {
            this.web_link = web_link;
        }
    }
}