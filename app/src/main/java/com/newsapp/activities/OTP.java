package com.newsapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.newsapp.R;
import com.newsapp.api.ApiListeners;
import com.newsapp.constant.Constant;
import com.newsapp.model.SaveUser;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OTP extends AppCompatActivity {

    public ApiListeners apiListeners;
    Button btn_submitl;
    ImageView img_logo;
    EditText edt_otp;
    ProgressDialog pb;
    String mobile, otp, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        getSupportActionBar().hide();
        btn_submitl = findViewById(R.id.btn_submit);
        edt_otp = findViewById(R.id.edt_otp);
        img_logo = findViewById(R.id.img_logo);
        Init();
        mobile = getIntent().getStringExtra("mobile");
        name = getIntent().getStringExtra("name");
        otp = getIntent().getIntExtra("otp", 0) + "";
        Picasso.with(getApplicationContext())
                .load(Constant.LOGO)
                .error(R.drawable.error_load)
                .placeholder(R.drawable.loading)
                .into(img_logo);
        btn_submitl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = true;
                String msg = "";
                if (!edt_otp.getText().toString().equals(otp)) {
                    isValid = true;
                    msg = "OTP Invalid.!";
                    Toast.makeText(OTP.this, msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isValid) {
                    VarifyOtp();
                }
            }
        });
    }

    private void Init() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiListeners = retrofit.create(ApiListeners.class);
        pb = new ProgressDialog(OTP.this);
        pb.setMessage("Please Wait...!");
        pb.setCancelable(false);
    }

    public void VarifyOtp() {
        pb.show();
        final Call<SaveUser> getUserInfoVoCall = apiListeners.VarifyOTP(edt_otp.getText().toString(), mobile);
        getUserInfoVoCall.enqueue(new Callback<SaveUser>() {
            @Override
            public void onResponse(Call<SaveUser> call, Response<SaveUser> response) {
                pb.dismiss();
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        Constant.save_sp(OTP.this, name, mobile);
                        finish();
                        startActivity(new Intent(OTP.this, Home.class));
                        Toast.makeText(OTP.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OTP.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SaveUser> call, Throwable t) {
                pb.dismiss();
                Toast.makeText(OTP.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}