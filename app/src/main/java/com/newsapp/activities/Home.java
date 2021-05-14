package com.newsapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.newsapp.AppUtilsKt;
import com.newsapp.R;
import com.newsapp.adpter.SearchResultAdapter;
import com.newsapp.adpter.SliderAdapter;
import com.newsapp.api.ApiListeners;
import com.newsapp.constant.Constant;
import com.newsapp.dto.PopupBannerResponse;
import com.newsapp.model.City;
import com.newsapp.model.News;
import com.newsapp.model.Settings;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

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

public class Home extends AppCompatActivity implements ViewPagerEx.OnPageChangeListener, BaseSliderView.OnSliderClickListener {
    private final int STORAGE_PERMISSION_CODE = 1;
    public ApiListeners apiListeners;
    /*Start Load More*/
    public int page = 1;
    SliderView img_slider;
    RecyclerView rec_for_you;
    SliderAdapter adapter;
    ProgressDialog pb;
    Spinner sp_city;
    ArrayAdapter<String> ardb_city;
    ArrayList<String> ar_city_name, ar_city_id;
    boolean firsttime = true;
    String sel_city_id = "0";
    TextView txt_norecfound;
    LinearLayout lin_spinner;
    ImageView img_spinner_city, img_slider_next, img_slider_perv;
    private Handler handler = null;
    private Runnable runnableCode = null;
    private ArrayList<News.Data> list_regular, list1_tranding, list2_slider;
    private SearchResultAdapter mAdapter;
    private int total_page;
    private boolean isLoadMoreApiCall = true;
    private int mSelectedIndex = 0;
    private int previous_sel_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        getSupportActionBar().hide();
        Constant.setBottomMenuSelected(Home.this, 0);
        img_slider = findViewById(R.id.img_slider);
        rec_for_you = findViewById(R.id.rec_for_you);
        sp_city = findViewById(R.id.sp_city);
        txt_norecfound = findViewById(R.id.txt_norecfound);
        lin_spinner = findViewById(R.id.lin_spinner);
        img_spinner_city = findViewById(R.id.img_spinner_city);
        img_slider_next = findViewById(R.id.img_slider_next);
        img_slider_perv = findViewById(R.id.img_slider_perv);
        ar_city_name = new ArrayList<>();
        ar_city_id = new ArrayList<>();
        list_regular = new ArrayList<>();
        list1_tranding = new ArrayList<>();
        Init();

        getTranding();
        getSlider();
        getCities();
        getSettings();
        getPopupBanner();

        lin_spinner.setOnClickListener(view -> sp_city.performClick());
        img_spinner_city.setOnClickListener(view -> sp_city.performClick());

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
        pb = new ProgressDialog(Home.this);
        pb.setMessage("Please Wait...!");
        pb.setCancelable(false);

        if (ContextCompat.checkSelfPermission(Home.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(Signup.this, "You have already granted this permission!",
//                    Toast.LENGTH_SHORT).show();
        } else {
            requestStoragePermission();
        }
    }

    public void getCities() {
//        pb.show();
        final Call<City> getUserInfoVoCall = apiListeners.Cities();
        getUserInfoVoCall.enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {
//                pb.dismiss();
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        ar_city_name.add("All Cities");
                        ar_city_id.add("0");
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            City.Data obj = response.body().getData().get(i);
                            ar_city_name.add(obj.getName());
                            ar_city_id.add(obj.getId());
                        }
                        ardb_city = new ArrayAdapter(Home.this, R.layout.adapter_spinner_city, ar_city_name) {
                            public View getView(int position, View convertView, ViewGroup parent) {
                                // Cast the spinner collapsed item (non-popup item) as a text view
                                TextView tv = (TextView) super.getView(position, convertView, parent);

                                // Set the text color of spinner item
                                tv.setTextColor(Color.WHITE);
                                tv.setTextSize(14);

                                // Return the view
                                return tv;
                            }

                            @Override
                            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                // Cast the drop down items (popup items) as text view
                                TextView tv = (TextView) super.getDropDownView(position, convertView, parent);

                                // Set the text color of drop down items
                                tv.setTextColor(Color.WHITE);
                                if (parent.getParent() != null) {
                                    ((View) parent.getParent()).setBackgroundResource(R.drawable.spinner);
                                    ((View) parent.getParent().getParent()).setBackgroundResource(R.drawable.spinner);
                                }
                                // If this item is selected item
                                if (position == mSelectedIndex) {
                                    // Set spinner selected popup item's text color
                                    tv.setTextColor(Color.WHITE);
                                }

                                // Return the modified view
                                return tv;
                            }
                        };
                        // Set an item selection listener for spinner widget
                        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                // Set the value for selected index variable
                                mSelectedIndex = i;
                                sel_city_id = ar_city_id.get(i);
                                if (!firsttime) {
                                    list_regular.clear();
                                    list1_tranding.clear();
                                    page = 1;
                                    firsttime = false;
                                }
                                if (previous_sel_id != mSelectedIndex) {
                                    ((View) view.getParent().getParent()).setBackgroundResource(R.drawable.spinner);
                                }
                                previous_sel_id = mSelectedIndex;
                                if (!firsttime) {
                                    getTranding();
                                }
                                firsttime = false;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                Log.e("", "");
                            }
                        });
                        sp_city.setAdapter(ardb_city);
                    } else {
                        Toast.makeText(Home.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
//                pb.dismiss();
                Toast.makeText(Home.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getSettings() {
//        pb.show();
        final Call<Settings> getUserInfoVoCall = apiListeners.Settings();
        getUserInfoVoCall.enqueue(new Callback<Settings>() {
            @Override
            public void onResponse(Call<Settings> call, Response<Settings> response) {
//                pb.dismiss();
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        Constant.save_sp_genral(getApplicationContext(), Constant.Android_version, response.body().getData().get(0).getAndroid_version());
                        Constant.save_sp_genral(getApplicationContext(), Constant.Appsharemsg, response.body().getData().get(0).getAppsharemsg());
                        Constant.save_sp_genral(getApplicationContext(), Constant.Isfourceupdate, response.body().getData().get(0).getIsfourceupdate());
                        Constant.save_sp_genral(getApplicationContext(), Constant.Postsharemsg, response.body().getData().get(0).getPostsharemsg());
                        Constant.save_sp_genral(getApplicationContext(), Constant.Register_form, response.body().getData().get(0).getRegister_form());
                        Constant.save_sp_genral(getApplicationContext(), Constant.Updatemsg, response.body().getData().get(0).getUpdatemsg());
                        Constant.save_sp_genral(getApplicationContext(), Constant.Update_link, response.body().getData().get(0).getUpdate_link());
                        String version = "";
                        try {
                            PackageInfo pInfo = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
                            version = pInfo.versionName;
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        if (!version.equalsIgnoreCase(Constant.get_sp(getApplicationContext(), Constant.Android_version))) {
                            boolean ForcedUpdate = false;
                            if (Constant.get_sp(getApplicationContext(), Constant.Isfourceupdate).equalsIgnoreCase("yes")) {
                                ForcedUpdate = true;
                            }
                            Constant.UpdateVersion(Home.this, ForcedUpdate);
                        }
                    } else {
                        Toast.makeText(Home.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Settings> call, Throwable t) {
//                pb.dismiss();
                Toast.makeText(Home.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getRegularNews() {
        pb.show();
        if (sel_city_id.equalsIgnoreCase("0")) {
            sel_city_id = "";
        }
        final Call<News> getUserInfoVoCall = apiListeners.getNews(sel_city_id, "", "", "", "", "", Constant.get_sp(Home.this, "mobile"), page);

        getUserInfoVoCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                pb.dismiss();
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
//                        list_regular.clear();
                        total_page = response.body().getTotal_page();
                        page = Integer.parseInt(response.body().getCurrent_page());
                        page++;
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            if (i == 2 && page == 2) {
                                News.Data obj = new News.Data();
                                obj.setView_type(1);
                                list_regular.add(obj);
                            }
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

                            list_regular.add(obj);

                            // Result
                            if (page == 2) {
                                mAdapter = new SearchResultAdapter(list_regular, list1_tranding, Home.this);
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
                                            getRegularNews();
                                            //Call Load More
                                            isLoadMoreApiCall = false;
                                        }
                                    }
                                }
                            });
                        }
                        if (list_regular.size() == 0) {
                            txt_norecfound.setText("No Record Found.!");
                            txt_norecfound.setVisibility(View.VISIBLE);
                            rec_for_you.setVisibility(View.GONE);
                        } else {
                            txt_norecfound.setText("Loading...");
                            txt_norecfound.setVisibility(View.GONE);
                            rec_for_you.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(Home.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                pb.dismiss();
                Toast.makeText(Home.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getTranding() {
//        pb.show();
        if (sel_city_id.equalsIgnoreCase("0")) {
            sel_city_id = "";
        }
        final Call<News> getUserInfoVoCall = apiListeners.getNews(sel_city_id, "", "", "yes", "", "", Constant.get_sp(Home.this, "mobile"), page);

        getUserInfoVoCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
//                pb.dismiss();
                if (response.body() != null) {
                    if (response.body().getSuccess()) {

                        list1_tranding = new ArrayList<>();
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            News.Data data = response.body().getData().get(i);
                            data.setnumRecords((i + 1) + "/" + response.body().getData().size());
                            list1_tranding.add(data);
                        }
                    } else {
                        Toast.makeText(Home.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }

                getRegularNews();
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
//                pb.dismiss();
                getRegularNews();
                Toast.makeText(Home.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getSlider() {
//        pb.show();
        final Call<News> getUserInfoVoCall = apiListeners.getNews("", "", "", "", "", "yes", Constant.get_sp(Home.this, "mobile"), page);

        getUserInfoVoCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
//                pb.dismiss();
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        list2_slider = (ArrayList<News.Data>) response.body().getData();
                        // passing this array list inside our adapter class.
                        adapter = new SliderAdapter(Home.this, list2_slider);
                        img_slider.setSliderAdapter(adapter);
                        img_slider.setInfiniteAdapterEnabled(true);
                        img_slider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                        img_slider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                        img_slider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                        img_slider.setIndicatorSelectedColor(Color.WHITE);
                        img_slider.setIndicatorUnselectedColor(Color.GRAY);
                        img_slider.setScrollTimeInSec(4); //set scroll delay in seconds :
                        img_slider.startAutoCycle();
                        if (response.body().getData().size() == 0) {
                            ((View) img_slider.getParent()).setVisibility(View.GONE);
                        }

//                        img_slider_next.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                int pos=0;
//                                if(img_slider.getCurrentPagePosition()<list2_slider.size()){
//                                    pos=img_slider.getCurrentPagePosition()+1;
//                                    img_slider.setCurrentPagePosition(pos);
//                                }else{
//
//                                }
//                            }
//                        });
//                        img_slider_perv.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                int pos=0;
//                                if(img_slider.getCurrentPagePosition()>0){
//                                    pos=img_slider.getCurrentPagePosition()-1;
//                                    img_slider_next.setVisibility(View.VISIBLE);
//                                }else{
//                                    img_slider_perv.setVisibility(View.GONE);
//                                    img_slider_next.setVisibility(View.VISIBLE);
//                                }
//                                img_slider.setCurrentPagePosition(pos);
//                            }
//                        });
                    } else {
                        Toast.makeText(Home.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
//                pb.dismiss();
                Toast.makeText(Home.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
        boolean ForcedUpdate = false;
        if (Constant.get_sp(getApplicationContext(), Constant.Isfourceupdate).equalsIgnoreCase("yes")) {
            ForcedUpdate = true;
        }
        if (ForcedUpdate) {
            String version = "";
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
                version = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (!version.equalsIgnoreCase(Constant.get_sp(getApplicationContext(), Constant.Android_version))) {

                Constant.UpdateVersion(Home.this, ForcedUpdate);
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Constant.showExitDialog(Home.this);
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Home.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getPopupBanner() {
        final Call<PopupBannerResponse> getUserInfoVoCall = apiListeners.popupBanner(Constant.AppFullScreenBannerAd.home);
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
                if (!isDestroyed() && (!(Home.this).isFinishing())) {
                    if ((Home.this).getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                        AppUtilsKt.showProgressDialog(Home.this, popup_banner);
                    }
                    handler.postDelayed(this, Constant.AppFullScreenBannerAd.adBetweenTime);
                }
            }
        };

        if (!isDestroyed() && (!(Home.this).isFinishing())) {
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