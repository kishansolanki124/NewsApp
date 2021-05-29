package com.excuseme.newsapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Settings {

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
        @SerializedName("register_form")
        private String register_form;
        @SerializedName("postsharemsg")
        private String postsharemsg;
        @SerializedName("appsharemsg")
        private String appsharemsg;
        @SerializedName("update_link")
        private String update_link;
        @SerializedName("updatemsg")
        private String updatemsg;
        @SerializedName("isfourceupdate")
        private String isfourceupdate;
        @SerializedName("ios_version")
        private String ios_version;
        @SerializedName("android_version")
        private String android_version;
        @SerializedName("id")
        private String id;

        public String getRegister_form() {
            return register_form;
        }

        public void setRegister_form(String register_form) {
            this.register_form = register_form;
        }

        public String getPostsharemsg() {
            return postsharemsg;
        }

        public void setPostsharemsg(String postsharemsg) {
            this.postsharemsg = postsharemsg;
        }

        public String getAppsharemsg() {
            return appsharemsg;
        }

        public void setAppsharemsg(String appsharemsg) {
            this.appsharemsg = appsharemsg;
        }

        public String getUpdatemsg() {
            return updatemsg;
        }

        public void setUpdatemsg(String updatemsg) {
            this.updatemsg = updatemsg;
        }

        public String getIsfourceupdate() {
            return isfourceupdate;
        }

        public void setIsfourceupdate(String isfourceupdate) {
            this.isfourceupdate = isfourceupdate;
        }

        public String getIos_version() {
            return ios_version;
        }

        public void setIos_version(String ios_version) {
            this.ios_version = ios_version;
        }

        public String getAndroid_version() {
            return android_version;
        }

        public void setAndroid_version(String android_version) {
            this.android_version = android_version;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUpdate_link() {
            return update_link;
        }

        public void setUpdate_link(String update_link) {
            this.update_link = update_link;
        }
    }
}
