package com.newsapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.newsapp.AppUtilsKt;
import com.newsapp.R;
import com.newsapp.adpter.OtherStoryAdapter;
import com.newsapp.adpter.SimpleSliderAdapter;
import com.newsapp.api.ApiListeners;
import com.newsapp.constant.Constant;
import com.newsapp.dto.PopupBannerResponse;
import com.newsapp.model.News;
import com.newsapp.model.NewsGallery;
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

public class DetailScreen extends AppCompatActivity implements ViewPagerEx.OnPageChangeListener, BaseSliderView.OnSliderClickListener {

    public ApiListeners apiListeners;
    RecyclerView rec_otherstory;
    List<News.Data> list;
    SliderView img_slider;
    TextView txt_tag, txt_title, txt_name, txt_desc, txt_date;
    ImageView img_profile, img_share, img_back, imgbookmark;
    ProgressDialog pb;
    News.Data newsData;
    ArrayList<String> ar_sliders;
    private Handler handler = null;
    private Runnable runnableCode = null;
    private OtherStoryAdapter otherStoryAdapter;
    private SimpleSliderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Constant.setBottomMenuSelected(DetailScreen.this, 0);
        if (Constant.get_sp(getApplicationContext(), "mobile").isEmpty()) {
            finish();
        }
        ar_sliders = new ArrayList<>();
        rec_otherstory = findViewById(R.id.rec_otherstory);
        img_slider = findViewById(R.id.img_slider);
        Init();
        txt_tag = findViewById(R.id.txt_tag);
        txt_title = findViewById(R.id.txt_title);
        txt_name = findViewById(R.id.txt_name);
        txt_desc = findViewById(R.id.txt_desc);
        img_share = findViewById(R.id.img_share);
        img_profile = findViewById(R.id.img_profile);
        img_back = findViewById(R.id.img_back);
        imgbookmark = findViewById(R.id.imgbookmark);
        txt_date = findViewById(R.id.txt_date);

        newsData = (News.Data) getIntent().getSerializableExtra("newsDetails");

        txt_tag.setText(newsData.getKeywords().toUpperCase());
        txt_title.setText(newsData.getName().trim());
        txt_name.setText(newsData.getUpload_by());
        txt_date.setText(newsData.getPdate());
        //txt_desc.setText(HtmlCompat.fromHtml(newsData.getDescription().trim(), 0));
        //txt_desc.setMovementMethod(LinkMovementMethod.getInstance());
        txt_desc.setText(HtmlCompat.fromHtml(newsData.getDescription(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        //txt_desc.setMovementMethod(LinkMovementMethod.getInstance());//todo work here

        if (newsData.getIsbookmark().equalsIgnoreCase("0")) {
            imgbookmark.setImageResource(R.drawable.ic_baseline_bookmark_gray_border_24);
        } else {
            imgbookmark.setImageResource(R.drawable.ic_baseline_bookmark_24);
        }
        Picasso.with(DetailScreen.this)
                .load(Constant.AUTHER + newsData.getAuthor_img())
                .error(R.drawable.user_icon)
                .placeholder(R.drawable.user_icon)
                .into(img_profile);
//        Picasso.with(getApplicationContext())
//                .load(Constant.AUTHER+data.getAuthor_img())
//                .error(R.drawable.error_load)
//                .placeholder(R.drawable.loading)
//                .into(img_profile);
        txt_tag.setOnClickListener(view -> Search.gotoSearchPage(DetailScreen.this, "", newsData.getCid(),
                "", "", "", newsData.getKeywords()));
        img_share.setOnClickListener(view -> {
//            String shareBody = Constant.get_sp(getApplicationContext(), Constant.Postsharemsg) + "\n\n"
//                    + HtmlCompat.fromHtml(newsData.getDescription().substring(0, Math.min(newsData.getDescription().length(), Constant.ShareDescWords)),
//                    HtmlCompat.FROM_HTML_MODE_COMPACT);
//            Constant.shareImage(getApplicationContext(), shareBody, Constant.POST + newsData.getUp_pro_img(), null);

            String shareBody = "*" + HtmlCompat.fromHtml(newsData.getName(), HtmlCompat.FROM_HTML_MODE_COMPACT)
                    + "*\n\n" + HtmlCompat.fromHtml(newsData.getDescription().substring(0,
                    Math.min(newsData.getDescription().length(), Constant.ShareDescWords)) + "...\n\n"
                    + Constant.get_sp(this, Constant.Postsharemsg), HtmlCompat.FROM_HTML_MODE_COMPACT);
            Constant.shareImage(this, shareBody, Constant.POST + newsData.getUp_pro_img(), null);
        });
        img_back.setOnClickListener(view -> finish());
        imgbookmark.setOnClickListener(view -> {
            Constant.saveBookmark(DetailScreen.this, newsData.getId(), Constant.get_sp(getApplicationContext(), "mobile"), "");
            if (newsData.getIsbookmark().equalsIgnoreCase("0")) {
                imgbookmark.setImageResource(R.drawable.ic_baseline_bookmark_24);
                newsData.setIsbookmark("1");
            } else {
                imgbookmark.setImageResource(R.drawable.ic_baseline_bookmark_gray_border_24);
                newsData.setIsbookmark("0");
            }
        });

        getOtherStories();
        getSliders();
        getPopupBanner();
    }

    private void Init() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .readTimeout(100, TimeUnit.SECONDS).build();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiListeners = retrofit.create(ApiListeners.class);
        pb = new ProgressDialog(DetailScreen.this);
        pb.setMessage("Please Wait...!");
        pb.setCancelable(false);
    }

    public void getOtherStories() {
        pb.show();
        final Call<News> getUserInfoVoCall = apiListeners.OtherStories("", newsData.getCid(), "", "", "", Constant.get_sp(DetailScreen.this, "mobile"));

        getUserInfoVoCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                pb.dismiss();
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        list = response.body().getData();
                        // Other Stories
                        otherStoryAdapter = new OtherStoryAdapter(list, DetailScreen.this);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        rec_otherstory.setLayoutManager(mLayoutManager);
                        rec_otherstory.setItemAnimator(new DefaultItemAnimator());
                        rec_otherstory.setAdapter(otherStoryAdapter);
                    } else {
                        Toast.makeText(DetailScreen.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                pb.dismiss();
                Toast.makeText(DetailScreen.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getSliders() {
        pb.show();
        final Call<NewsGallery> getUserInfoVoCall = apiListeners.NewsGallery(newsData.getId());

        getUserInfoVoCall.enqueue(new Callback<NewsGallery>() {
            @Override
            public void onResponse(Call<NewsGallery> call, Response<NewsGallery> response) {
                pb.dismiss();
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        ar_sliders.add(newsData.getUp_pro_img());
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            ar_sliders.add(response.body().getData().get(i).getUp_pro_img());
                        }
                        adapter = new SimpleSliderAdapter(DetailScreen.this, ar_sliders);
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
                        Toast.makeText(DetailScreen.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<NewsGallery> call, Throwable t) {
                pb.dismiss();
                Toast.makeText(DetailScreen.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

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
                if (!isDestroyed() && (!(DetailScreen.this).isFinishing())) {
                    if ((DetailScreen.this).getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                        AppUtilsKt.showProgressDialog(DetailScreen.this, popup_banner);
                    }
                    handler.postDelayed(this, Long.parseLong(delayTime) * 1000);
                }
            }
        };

        if (!isDestroyed() && (!(DetailScreen.this).isFinishing())) {
            handler.postDelayed(runnableCode, Long.parseLong(initialDelayTime) * 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != handler) {
            handler.removeCallbacks(runnableCode);
        }
    }
}