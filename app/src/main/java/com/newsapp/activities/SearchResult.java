package com.newsapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.newsapp.AppUtilsKt;
import com.newsapp.R;
import com.newsapp.adpter.SearchResultAdapter;
import com.newsapp.api.ApiListeners;
import com.newsapp.constant.Constant;
import com.newsapp.dto.PopupBannerResponse;
import com.newsapp.model.News;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchResult extends AppCompatActivity implements ViewPagerEx.OnPageChangeListener, BaseSliderView.OnSliderClickListener {

    public ApiListeners apiListeners;
    /*Start Load More*/
    public int page = 1;
    RecyclerView rec_for_you;
    ProgressDialog pb;
    String city, category, keyword, tranding, search, label;
    TextView search_label;
    EditText edt_search;
    TextView txt_norecfound;
    ImageView img_back;
    private ArrayList<News.Data> list;
    private SearchResultAdapter mAdapter;
    private int total_page;
    private boolean isLoadMoreApiCall = true;
    private Handler handler = null;
    private Runnable runnableCode = null;


    /*End Load More*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result2);
        getSupportActionBar().hide();
        Constant.setBottomMenuSelected(SearchResult.this, 1);
        txt_norecfound = findViewById(R.id.txt_norecfound);
        img_back = findViewById(R.id.img_back);
        list = new ArrayList<>();
        if (Constant.get_sp(getApplicationContext(), "mobile").isEmpty()) {
            finish();
        }
        rec_for_you = findViewById(R.id.rec_for_you);
        search_label = findViewById(R.id.search_label);
        edt_search = findViewById(R.id.edt_search);
//        edt_search.onActionViewExpanded();
        Init();

        city = getIntent().getStringExtra("city");
        category = getIntent().getStringExtra("category");
        keyword = getIntent().getStringExtra("keyword");
        tranding = getIntent().getStringExtra("tranding");
        search = getIntent().getStringExtra("search");
        label = getIntent().getStringExtra("label");
        search_label.setText(label);

        img_back.setOnClickListener(view -> finish());

        getSearchData(city, category, keyword, tranding, search);

//        edt_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                Search.gotoSearchPage(SearchResult.this,"","","","",s,s);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                return false;
//            }
//        });
        edt_search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Search.gotoSearchPage(SearchResult.this, "", "", "", "", edt_search.getText().toString(), edt_search.getText().toString());
                return true;
            }
            return false;
        });

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
        pb = new ProgressDialog(SearchResult.this);
        pb.setMessage("Please Wait...!");
        pb.setCancelable(false);
    }

    public void getSearchData(final String city, final String category, final String keyword, final String tranding, final String search) {
        if (page == 1)
            pb.show();
        final Call<News> getUserInfoVoCall = apiListeners.getNews(city, category, keyword, tranding, search, "", Constant.get_sp(SearchResult.this, "mobile"), page);

        getUserInfoVoCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (pb.isShowing())
                    pb.dismiss();
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        total_page = response.body().getTotal_page();
                        page = Integer.parseInt(response.body().getCurrent_page());
                        page++;
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            News.Data obj = new News.Data();
                            obj.setIsbookmark(response.body().getData().get(i).getIsbookmark());
                            obj.setAuthor(response.body().getData().get(i).getAuthor());
                            obj.setCid(response.body().getData().get(i).getCid());
                            obj.setDescription(response.body().getData().get(i).getDescription());
                            obj.setCity(response.body().getData().get(i).getCity());
                            obj.setId(response.body().getData().get(i).getId());
                            obj.setKeywords(response.body().getData().get(i).getKeywords());
                            obj.setName(response.body().getData().get(i).getName());
                            obj.setPdate(response.body().getData().get(i).getPdate());
                            obj.setSlider(response.body().getData().get(i).getSlider());
                            obj.setStatus(response.body().getData().get(i).getStatus());
                            obj.setTrending_news(response.body().getData().get(i).getTrending_news());
                            obj.setUp_pro_img(response.body().getData().get(i).getUp_pro_img());
                            obj.setUpload_by(response.body().getData().get(i).getUpload_by());
                            obj.setUser_mobile(response.body().getData().get(i).getUser_mobile());
                            obj.setAuthor_img(response.body().getData().get(i).getAuthor_img());
                            list.add(obj);
                        }
                        if (list.size() == 0) {
                            txt_norecfound.setText("The Requested Story Update Soon. If you have any knowledge regarding the same, then feel free to upload it to spread in society !!!");
                            txt_norecfound.setVisibility(View.VISIBLE);
                        } else {
                            txt_norecfound.setText("Loading...");
                            txt_norecfound.setVisibility(View.GONE);
                        }
                        // Result
                        if (page == 2) {
                            mAdapter = new SearchResultAdapter(list, null, SearchResult.this);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            rec_for_you.setLayoutManager(mLayoutManager);
                            rec_for_you.setItemAnimator(new DefaultItemAnimator());
                            rec_for_you.setAdapter(mAdapter);
                        } else {
                            if (mAdapter != null) {
                                isLoadMoreApiCall = true;
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                        rec_for_you.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if (total_page >= page) {
                                    if (isLoadMoreApiCall) {
                                        getSearchData(city, category, keyword, tranding, search);
                                        //Call Load More
                                        isLoadMoreApiCall = false;
                                    }
                                }
                            }
                        });

                    } else {
                        Toast.makeText(SearchResult.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                if (pb.isShowing())
                    pb.dismiss();
                Toast.makeText(SearchResult.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
        final Call<PopupBannerResponse> getUserInfoVoCall = apiListeners.popupBanner(Constant.AppFullScreenBannerAd.search_result_screen);
        getUserInfoVoCall.enqueue(new Callback<PopupBannerResponse>() {
            @Override
            public void onResponse(Call<PopupBannerResponse> call, Response<PopupBannerResponse> response) {
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        if (!response.body().getPopup_banner().isEmpty()) {
                            setupRepeatableBannerAd(response.body().getPopup_banner());
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

    private void setupRepeatableBannerAd(List<PopupBannerResponse.PopupBanner> popup_banner) {
        handler = new Handler();
        runnableCode = new Runnable() {
            @Override
            public void run() {
                if (!isDestroyed() && (!(SearchResult.this).isFinishing())) {
                    if ((SearchResult.this).getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                        AppUtilsKt.showProgressDialog(SearchResult.this, popup_banner);
                    }
                    handler.postDelayed(this, Constant.AppFullScreenBannerAd.adBetweenTime);
                }
            }
        };

        if (!isDestroyed() && (!(SearchResult.this).isFinishing())) {
            handler.postDelayed(runnableCode, Constant.AppFullScreenBannerAd.adDelayTime);
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