package com.newsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.newsapp.R;
import com.newsapp.adpter.BookmarkAdapter;
import com.newsapp.adpter.SearchResultAdapter;
import com.newsapp.api.ApiListeners;
import com.newsapp.constant.Constant;
import com.newsapp.model.Bookmarks;
import com.newsapp.model.Feedback;
import com.newsapp.model.News;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Bookmark extends AppCompatActivity {

    public ApiListeners apiListeners;
    ProgressDialog pb;
    RecyclerView rec_bookmark;
    private ArrayList<News.Data> list;
    private BookmarkAdapter mAdapter;
    TextView txt_norecfound;
    ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        getSupportActionBar().hide();
        Constant.setBottomMenuSelected(Bookmark.this,0);
        txt_norecfound=findViewById(R.id.txt_norecfound);
        img_back=findViewById(R.id.img_back);
        if(Constant.get_sp(getApplicationContext(),"mobile").isEmpty()){
            finish();
        }
        rec_bookmark=findViewById(R.id.rec_bookmark);
        Init();
        getBookmark();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void Init() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiListeners = retrofit.create(ApiListeners.class);
        pb=new ProgressDialog(Bookmark.this);
        pb.setMessage("Please Wait...!");
        pb.setCancelable(false);
    }
    public void getBookmark(){
        pb.show();
        final Call<News> getUserInfoVoCall = apiListeners.Bookmark(Constant.get_sp(Bookmark.this,"mobile"));

        getUserInfoVoCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                pb.dismiss();
                if(response.body() != null){
                    if(response.body().getSuccess()){
                        list= (ArrayList<News.Data>) response.body().getData();
                        // Result
                        mAdapter = new BookmarkAdapter(list,Bookmark.this);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        rec_bookmark.setLayoutManager(mLayoutManager);
                        rec_bookmark.setItemAnimator(new DefaultItemAnimator());
                        rec_bookmark.setAdapter(mAdapter);

                        if(list.size()==0){
                            txt_norecfound.setText("No Bookmark Saved.!");
                            txt_norecfound.setVisibility(View.VISIBLE);
                        }else{
                            txt_norecfound.setText("Loading...");
                            txt_norecfound.setVisibility(View.GONE);
                        }
                    }else{
                        Toast.makeText(Bookmark.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                pb.dismiss();
                Toast.makeText(Bookmark.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(Constant.get_sp(getApplicationContext(),"mobile").isEmpty()){
            finish();
        }
    }
}