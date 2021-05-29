package com.excuseme.newsapp.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.text.HtmlCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.excuseme.newsapp.AppUtilsKt;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.excuseme.newsapp.R;
import com.excuseme.newsapp.adpter.NewsTagAdapter;
import com.excuseme.newsapp.adpter.OtherStoryAdapter;
import com.excuseme.newsapp.adpter.SimpleSliderAdapter;
import com.excuseme.newsapp.api.ApiListeners;
import com.excuseme.newsapp.constant.Constant;
import com.excuseme.newsapp.dto.NewsDetailResponse;
import com.excuseme.newsapp.dto.PopupBannerResponse;
import com.excuseme.newsapp.model.News;
import com.excuseme.newsapp.model.NewsGallery;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsDetail extends AppCompatActivity {
    public ApiListeners apiListeners;
    NewsTagAdapter newsTagAdapter;
    RecyclerView rvOtherStories;
    List<News.Data> list;
    SliderView img_slider;
    TextView txt_tag, txt_title, txt_name, txt_date, tvWebsiteLink;
    TextView txt_desc;
    LinearLayoutCompat llNewsDetailContent, llWebsite;
    NestedScrollView nsvRoot;
    RecyclerView rvNewsTags;
    ProgressBar pbNewsDetail;
    ImageView img_profile, img_share, img_back, ivBookmark;
    ArrayList<String> ar_sliders;
    private int newsId = 0;
    private String newsCatId = "";
    private Handler handler = null;
    private Runnable runnableCode = null;
    private OtherStoryAdapter otherStoryAdapter;
    private SimpleSliderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);

        Objects.requireNonNull(getSupportActionBar()).hide();

        Constant.setBottomMenuSelected(NewsDetail.this, 0);
        if (Constant.get_sp(getApplicationContext(), "mobile").isEmpty()) {
            finish();
        }

        init();

        ar_sliders = new ArrayList<>();
        rvNewsTags = findViewById(R.id.rvNewsTags);
        rvOtherStories = findViewById(R.id.rec_otherstory);
        img_slider = findViewById(R.id.img_slider);
        txt_tag = findViewById(R.id.txt_tag);
        txt_title = findViewById(R.id.txt_title);
        txt_name = findViewById(R.id.txt_name);
        txt_desc = findViewById(R.id.txt_desc);
        img_share = findViewById(R.id.img_share);
        img_profile = findViewById(R.id.img_profile);
        img_back = findViewById(R.id.img_back);
        ivBookmark = findViewById(R.id.imgbookmark);
        txt_date = findViewById(R.id.txt_date);
        tvWebsiteLink = findViewById(R.id.tvWebsiteLink);

        llNewsDetailContent = findViewById(R.id.llNewsDetailContent);
        llWebsite = findViewById(R.id.llWebsite);
        nsvRoot = findViewById(R.id.nsvRoot);
        pbNewsDetail = findViewById(R.id.pbNewsDetail);

        newsId = getIntent().getIntExtra(Constant.NEWS_ID, 0);

        if (0 != newsId) {
            llNewsDetailContent.setVisibility(View.INVISIBLE);
            pbNewsDetail.setVisibility(View.VISIBLE);
            getNewsDetails();
        }
        getPopupBanner();
    }

    private void init() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .readTimeout(100, TimeUnit.SECONDS).build();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiListeners = retrofit.create(ApiListeners.class);
    }

    public void getOtherStories() {
        final Call<News> getUserInfoVoCall = apiListeners.OtherStories("", newsCatId, "", "", "", Constant.get_sp(NewsDetail.this, "mobile"));

        getUserInfoVoCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        list = response.body().getData();
                        // Other Stories
                        otherStoryAdapter = new OtherStoryAdapter(list, NewsDetail.this);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        rvOtherStories.setLayoutManager(mLayoutManager);
                        rvOtherStories.setItemAnimator(new DefaultItemAnimator());
                        rvOtherStories.setAdapter(otherStoryAdapter);
                    } else {
                        Toast.makeText(NewsDetail.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
                nsvRoot.scrollTo(0, 0);
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Toast.makeText(NewsDetail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getSliders() {
        final Call<NewsGallery> getUserInfoVoCall = apiListeners.NewsGallery(String.valueOf(newsId));

        getUserInfoVoCall.enqueue(new Callback<NewsGallery>() {
            @Override
            public void onResponse(Call<NewsGallery> call, Response<NewsGallery> response) {
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            ar_sliders.add(response.body().getData().get(i).getUp_pro_img());
                        }
                        adapter = new SimpleSliderAdapter(NewsDetail.this, ar_sliders);
                        img_slider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
                        img_slider.setSliderAdapter(adapter);
                        img_slider.setScrollTimeInSec(5);
                        img_slider.setAutoCycle(true);
                        if (ar_sliders.size() == 1) {
                            img_slider.setScrollTimeInSec(50000);
                            img_slider.setEnabled(false);
                        }
                        img_slider.startAutoCycle();
                        if (ar_sliders.size() == 0) {
                            ((View) img_slider.getParent()).setVisibility(View.GONE);
                        }
//                        HashMap<String, String> sliderImages = new HashMap<>();
//                        sliderImages.put("",data.getUp_pro_img());
//                        for(int i=0;i<response.body().getData().size();i++) {
//                            sliderImages.put("",response.body().getData().get(i).getUp_pro_img());
//                        }
//
//                        // Slider
//                        img_slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
//                        img_slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//                        img_slider.setCustomAnimation(new DescriptionAnimation());
//                        img_slider.setDuration(3000);
//                        img_slider.addOnPageChangeListener(DetailScreen.this);
//                        for (String name : sliderImages.keySet()) {
//
//                            TextSliderView textSliderView = new TextSliderView(DetailScreen.this);
//                            textSliderView
//                                    .description(name)
//                                    .image(sliderImages.get(name))
//                                    .setScaleType(BaseSliderView.ScaleType.Fit)
//                                    .setOnSliderClickListener(DetailScreen.this);
//                            textSliderView.bundle(new Bundle());
//                            textSliderView.getBundle()
//                                    .putString("extra", name);
//                            img_slider.addSlider(textSliderView);
//
//
//                        }
//                        if(sliderImages.size()==0){
//                            img_slider.setVisibility(View.GONE);
//                        }
                    } else {
                        Toast.makeText(NewsDetail.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
                nsvRoot.scrollTo(0, 0);
            }

            @Override
            public void onFailure(Call<NewsGallery> call, Throwable t) {
                Toast.makeText(NewsDetail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getNewsDetails() {
        final Call<NewsDetailResponse> getUserInfoVoCall = apiListeners.newsDetail(String.valueOf(newsId),
                Constant.get_sp(NewsDetail.this, "mobile"));
        getUserInfoVoCall.enqueue(new Callback<NewsDetailResponse>() {
            @Override
            public void onResponse(Call<NewsDetailResponse> call, Response<NewsDetailResponse> response) {
                nsvRoot.scrollTo(0, 0);
                pbNewsDetail.setVisibility(View.GONE);
                llNewsDetailContent.setVisibility(View.VISIBLE);
                nsvRoot.scrollTo(0, 0);
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        setupViews(response.body().getData().get(0));
                    } else {
                        Toast.makeText(NewsDetail.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
                nsvRoot.scrollTo(0, 0);
            }

            @Override
            public void onFailure(Call<NewsDetailResponse> call, Throwable t) {
                pbNewsDetail.setVisibility(View.GONE);
                llNewsDetailContent.setVisibility(View.VISIBLE);
                Toast.makeText(NewsDetail.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupViews(NewsDetailResponse.Data newsData) {
        txt_tag.setText(newsData.getKeywords().toUpperCase());
        txt_title.setText(newsData.getName().trim());
        txt_name.setText(newsData.getUpload_by());
        txt_date.setText(newsData.getPdate());
//        txt_desc.setText(HtmlCompat.fromHtml(newsData.getDescription().trim(), 0));
//        txt_desc.setMovementMethod(LinkMovementMethod.getInstance());
        txt_desc.setText(HtmlCompat.fromHtml(newsData.getDescription(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        //txt_desc.setMovementMethod(LinkMovementMethod.getInstance());//todo work here

        if (newsData.getIsbookmark().equalsIgnoreCase("0")) {
            ivBookmark.setImageResource(R.drawable.ic_baseline_bookmark_gray_border_24);
        } else {
            ivBookmark.setImageResource(R.drawable.ic_baseline_bookmark_24);
        }
        Picasso.with(NewsDetail.this)
                .load(Constant.AUTHER + newsData.getAuthor_img())
                .error(R.drawable.user_icon)
                .placeholder(R.drawable.user_icon)
                .into(img_profile);

        if (!newsData.getWeb_link().isEmpty()) {
            llWebsite.setVisibility(View.VISIBLE);
            tvWebsiteLink.setText(newsData.getWeb_link());
        }

        txt_tag.setOnClickListener(view -> Search.gotoSearchPage(NewsDetail.this, "", newsData.getCid(),
                "", "", "", newsData.getKeywords()));
        img_share.setOnClickListener(view -> {
            String shareBody = "*" + HtmlCompat.fromHtml(newsData.getName(), HtmlCompat.FROM_HTML_MODE_COMPACT)
                    + "*\n\n" + HtmlCompat.fromHtml(newsData.getDescription().substring(0,
                    Math.min(newsData.getDescription().length(), Constant.ShareDescWords)) + "...\n\n"
                    + Constant.get_sp(this, Constant.Postsharemsg), HtmlCompat.FROM_HTML_MODE_COMPACT);
            Constant.shareImage(this, shareBody, Constant.POST + newsData.getUp_pro_img(), null);
        });
        img_back.setOnClickListener(view -> finish());
        ivBookmark.setOnClickListener(view -> {
            Constant.saveBookmark(NewsDetail.this, newsData.getId(), Constant.get_sp(getApplicationContext(), "mobile"), "");
            if (newsData.getIsbookmark().equalsIgnoreCase("0")) {
                ivBookmark.setImageResource(R.drawable.ic_baseline_bookmark_24);
                newsData.setIsbookmark("1");
            } else {
                ivBookmark.setImageResource(R.drawable.ic_baseline_bookmark_gray_border_24);
                newsData.setIsbookmark("0");
            }
        });

        newsCatId = newsData.getCid();
        ar_sliders.add(newsData.getUp_pro_img());
        getOtherStories();
        getSliders();
        if (!newsData.getTags().isEmpty()) {
            setupNewsTags(newsData.getTags());
        } else {
            rvNewsTags.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constant.get_sp(getApplicationContext(), "mobile").isEmpty()) {
            finish();
        }
    }

    private void getPopupBanner() {
        final Call<PopupBannerResponse> getUserInfoVoCall = apiListeners.popupBanner(Constant.AppFullScreenBannerAd.news_detail_screen);
        getUserInfoVoCall.enqueue(new Callback<PopupBannerResponse>() {
            @Override
            public void onResponse(Call<PopupBannerResponse> call, Response<PopupBannerResponse> response) {
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        if (!response.body().getPopup_banner().isEmpty()) {
                            setupRepeatableBannerAd(response.body().getDelay_time(), response.body().getInitial_time(), response.body().getPopup_banner());
                        }
                    } else {
                        System.out.println(response.body().getMsg());
                    }
                }
                nsvRoot.scrollTo(0, 0);
            }

            @Override
            public void onFailure(Call<PopupBannerResponse> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void setupRepeatableBannerAd(String delayTime, String initialDelayTime, List<PopupBannerResponse.PopupBanner> popup_banner) {
        handler = new Handler();
        runnableCode = new Runnable() {
            @Override
            public void run() {
                if (!isDestroyed() && (!(NewsDetail.this).isFinishing())) {
                    if ((NewsDetail.this).getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                        AppUtilsKt.showProgressDialog(NewsDetail.this, popup_banner);
                    }
                    handler.postDelayed(this, Long.parseLong(delayTime) * 1000);
                }
            }
        };

        if (!isDestroyed() && (!(NewsDetail.this).isFinishing())) {
            handler.postDelayed(runnableCode, Long.parseLong(initialDelayTime) * 1000);
        }
    }

    void setupNewsTags(String tags) {
        rvNewsTags.setVisibility(View.VISIBLE);
        newsTagAdapter = new NewsTagAdapter(AppUtilsKt.commaSeparatedStringtoArrayList(tags), this);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW_REVERSE);
        layoutManager.setJustifyContent(JustifyContent.FLEX_END);
        rvNewsTags.setLayoutManager(layoutManager);
        rvNewsTags.setItemAnimator(new DefaultItemAnimator());
        rvNewsTags.setAdapter(newsTagAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != handler) {
            handler.removeCallbacks(runnableCode);
        }
    }
}