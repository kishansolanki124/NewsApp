package com.excuseme.newsapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.excuseme.newsapp.AppUtilsKt;
import com.excuseme.newsapp.constant.Constant;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.excuseme.newsapp.R;
import com.excuseme.newsapp.adpter.CategoriesAdapter;
import com.excuseme.newsapp.adpter.KeywordAdapter;
import com.excuseme.newsapp.api.ApiListeners;
import com.excuseme.newsapp.dto.PopupBannerResponse;
import com.excuseme.newsapp.model.Categories;
import com.excuseme.newsapp.model.Keyword;

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

public class Search extends AppCompatActivity {

    public ApiListeners apiListeners;
    RecyclerView rec_categories, rec_popularkeyword;
    List<Categories.Data> list;
    List<Keyword.Data> list1;
    CategoriesAdapter mAdapter;
    KeywordAdapter mAdapter1;
    EditText edt_search;
    ProgressDialog pb;
    private Handler handler = null;
    private Runnable runnableCode = null;

    public static void gotoSearchPage(Context context, String city, String category, String keyword, String tranding, String search, String label) {
        Intent intent = new Intent(context, SearchResult.class);
        intent.putExtra("city", city);
        intent.putExtra("category", category);
        intent.putExtra("keyword", keyword);
        intent.putExtra("tranding", tranding);
        intent.putExtra("search", search);
        intent.putExtra("label", label);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();
        if (Constant.get_sp(getApplicationContext(), "mobile").isEmpty()) {
            finish();
        }
        Constant.setBottomMenuSelected(Search.this, 1);
        rec_categories = (RecyclerView) findViewById(R.id.rec_categories);
        rec_popularkeyword = (RecyclerView) findViewById(R.id.rec_popularkeyword);
        edt_search = (EditText) findViewById(R.id.edt_search);
//        edt_search.onActionViewExpanded();
        Constant.hideKeyboard(Search.this);
        Init();

        // Search View
//        edt_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                gotoSearchPage(Search.this,"","","","",s,s);
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
                gotoSearchPage(Search.this, "", "", "", "", edt_search.getText().toString(), edt_search.getText().toString());
                return true;
            }
            return false;
        });
        getCategories();
        getKeyWords();
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
        pb = new ProgressDialog(Search.this);
        pb.setMessage("Please Wait...!");
        pb.setCancelable(false);
    }

    public void getCategories() {
        pb.show();
        final Call<Categories> getUserInfoVoCall = apiListeners.Categories();

        getUserInfoVoCall.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                pb.dismiss();
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        list = (ArrayList<Categories.Data>) response.body().getData();
                        // Categories
                        mAdapter = new CategoriesAdapter(list, Search.this);


                        rec_categories.setHasFixedSize(true);
//                        rec_categories.addItemDecoration(new DividerItemDecoration(Search.this,
//                                DividerItemDecoration.HORIZONTAL));
//                        rec_categories.addItemDecoration(new DividerItemDecoration(Search.this,
//                                DividerItemDecoration.VERTICAL));
                        rec_categories.setLayoutManager(new GridLayoutManager(Search.this, 3));

                        rec_categories.setAdapter(mAdapter);
                    } else {
                        Toast.makeText(Search.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                pb.dismiss();
                Toast.makeText(Search.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getKeyWords() {
        pb.show();
        final Call<Keyword> getUserInfoVoCall = apiListeners.Keyword();

        getUserInfoVoCall.enqueue(new Callback<Keyword>() {
            @Override
            public void onResponse(Call<Keyword> call, Response<Keyword> response) {
                pb.dismiss();
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        list1 = (ArrayList<Keyword.Data>) response.body().getData();
                        // Popular Keyword
                        mAdapter1 = new KeywordAdapter(list1, Search.this);
//                        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());
//                        rec_popularkeyword.setLayoutManager(new GridLayoutManager(Search.this, 3));
                        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(Search.this);
                        layoutManager.setFlexDirection(FlexDirection.ROW_REVERSE);
                        layoutManager.setJustifyContent(JustifyContent.FLEX_END);
                        rec_popularkeyword.setLayoutManager(layoutManager);
                        rec_popularkeyword.setItemAnimator(new DefaultItemAnimator());
                        rec_popularkeyword.setAdapter(mAdapter1);
                    } else {
                        Toast.makeText(Search.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Keyword> call, Throwable t) {
                pb.dismiss();
                Toast.makeText(Search.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constant.get_sp(getApplicationContext(), "mobile").isEmpty()) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), Home.class));
    }

    private void getPopupBanner() {
        final Call<PopupBannerResponse> getUserInfoVoCall = apiListeners.popupBanner(Constant.AppFullScreenBannerAd.search_screen);
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
                if (!isDestroyed() && (!(Search.this).isFinishing())) {
                    if ((Search.this).getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                        AppUtilsKt.showProgressDialog(Search.this, popup_banner);
                    }
                    handler.postDelayed(this, Long.parseLong(delayTime) * 1000);
                }
            }
        };

        if (!isDestroyed() && (!(Search.this).isFinishing())) {
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