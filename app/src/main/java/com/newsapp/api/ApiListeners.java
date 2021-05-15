package com.newsapp.api;

import com.newsapp.constant.Constant;
import com.newsapp.dto.IntroResponseModel;
import com.newsapp.dto.NewsDetailResponse;
import com.newsapp.dto.PopupBannerResponse;
import com.newsapp.model.BookmarkSave;
import com.newsapp.model.Bookmarks;
import com.newsapp.model.Categories;
import com.newsapp.model.City;
import com.newsapp.model.Feedback;
import com.newsapp.model.Keyword;
import com.newsapp.model.News;
import com.newsapp.model.NewsGallery;
import com.newsapp.model.OtherStories;
import com.newsapp.model.PostSave;
import com.newsapp.model.SaveUser;
import com.newsapp.model.Settings;
import com.newsapp.model.StaticPage;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiListeners {


    @POST(Constant.WS_REGISTER)
    @FormUrlEncoded
    Call<SaveUser> SaveUser(@Field("name") String name,
                            @Field("mobile") String mobile,
                            @Field("city") String city);

    @POST(Constant.WS_OTP)
    @FormUrlEncoded
    Call<SaveUser> VarifyOTP(@Field("otp") String otp,
                             @Field("mobile") String mobile);

    @POST(Constant.WS_SAVE_BOOKMARK)
    @FormUrlEncoded
    Call<BookmarkSave> Bookmark_Save(@Field("nid") String nid,
                                     @Field("user_mobile") String user_mobile,
                                     @Field("id") String id);


    @POST(Constant.WS_NEWS)
    @FormUrlEncoded
    Call<News> getNews(@Field("city") String city,
                       @Field("cid") String cid,
                       @Field("keywords") String keywords,
                       @Field("trending_news") String trending_news,
                       @Field("title_desc") String title_desc,
                       @Field("slider") String slider,
                       @Field("user_mobile") String user_mobile,
                       @Field("page") Integer page);

    @POST(Constant.WS_NEWS)
    @FormUrlEncoded
    Call<News> OtherStories(@Field("city") String city,
                       @Field("cid") String cid,
                       @Field("keywords") String keywords,
                       @Field("trending_news") String trending_news,
                       @Field("title_desc") String title_desc,
                       @Field("user_mobile") String user_mobile);

    @POST(Constant.WS_CATEGORIES)
    Call<Categories> Categories();

    @POST(Constant.WS_CITIES)
    Call<City> Cities();

    @POST(Constant.WS_Keyword)
    Call<Keyword> Keyword();

    @POST(Constant.WS_STATIC_PAGE)
    Call<StaticPage> StaticPage();

    @POST(Constant.WS_NEWS_GALLRY)
    @FormUrlEncoded
    Call<NewsGallery> NewsGallery(@Field("nid") String nid);

    @POST(Constant.WS_NEWS_DETAIL)
    @FormUrlEncoded
    Call<NewsDetailResponse> newsDetail(@Field("nid") String nid,
                                        @Field("user_mobile") String user_mobile);

    @POST(Constant.WS_POPUP_BANNER)
    @FormUrlEncoded
    Call<PopupBannerResponse> popupBanner(@Field("screen") String screen);

    @POST(Constant.WS_FEEDBACK)
    @FormUrlEncoded
    Call<Feedback> Feedback(@Field("name") String name,
                            @Field("mobile") String mobile,
                            @Field("message") String message,
                            @Field("email") String edt_email);

    @POST(Constant.WS_BOOKMARK)
    @FormUrlEncoded
    Call<News> Bookmark(@Field("user_mobile") String user_mobile);

    @Multipart
    @POST(Constant.WS_SAVE_POST)
    Call<PostSave> SavePost(@Part("city") RequestBody city,
                            @Part("cid") RequestBody cid,
                            @Part("name") RequestBody name,
                            @Part("description") RequestBody description,
                            @Part("author") RequestBody author,
                            @Part("keywords") RequestBody keywords,
                            @Part("upload_by") RequestBody upload_by,
                            @Part("user_mobile") RequestBody user_mobile,
                            @Part MultipartBody.Part... filename);

    @POST(Constant.WS_SETTINGS)
    Call<Settings> Settings();

    @GET(Constant.WS_HOME_BANNER)
    Call<IntroResponseModel> introBanner();

}
