package com.excuseme.newsapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.excuseme.newsapp.api.ApiListeners;
import com.excuseme.newsapp.constant.Constant;
import com.excuseme.newsapp.model.SaveUser;
import com.excuseme.newsapp.R;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Signup extends AppCompatActivity {

    private final int STORAGE_PERMISSION_CODE = 1;
    public ApiListeners apiListeners;
    Button btn_submitl;
    EditText edt_fullname, edt_mobile, edt_city;
    ImageView img_logo;
    ProgressDialog pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        btn_submitl = findViewById(R.id.btn_submit);
        edt_fullname = findViewById(R.id.edt_fullname);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_city = findViewById(R.id.edt_city);
        img_logo = findViewById(R.id.img_logo);
        Init();
        Picasso.with(getApplicationContext())
                .load(Constant.get_sp(getApplicationContext(), Constant.APP_LOGO))
                .error(R.drawable.error_load)
                .placeholder(R.drawable.loading)
                .into(img_logo);
        if (!Constant.get_sp(Signup.this, "mobile").isEmpty()) {
            Intent intent = new Intent(Signup.this, Home.class);
            finish();
            startActivity(intent);
        }

        btn_submitl.setOnClickListener(view -> {
            boolean isValid = true;
            String msg = "";
            if (edt_fullname.getText().toString().isEmpty()) {
                isValid = true;
                msg = "Full name must  required.";
                return;
            }
            if (edt_mobile.getText().toString().isEmpty()) {
                isValid = true;
                msg = "Mobile number must  required.";
                return;
            }
            if (edt_city.getText().toString().isEmpty()) {
                isValid = true;
                msg = "City must  required.";
                return;
            }

            if (isValid) {
                saveUser();

            }
        });
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
        pb = new ProgressDialog(Signup.this);
        pb.setMessage("Please Wait...!");
        pb.setCancelable(false);

        if (ContextCompat.checkSelfPermission(Signup.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(Signup.this, "You have already granted this permission!",
//                    Toast.LENGTH_SHORT).show();
        } else {
            requestStoragePermission();
        }
    }

    public void saveUser() {
        pb.show();
        final Call<SaveUser> getUserInfoVoCall = apiListeners.SaveUser(edt_fullname.getText().toString(), edt_mobile.getText().toString(), edt_city.getText().toString());
        getUserInfoVoCall.enqueue(new Callback<SaveUser>() {
            @Override
            public void onResponse(Call<SaveUser> call, Response<SaveUser> response) {
                pb.dismiss();
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        finish();
                        Intent intent = new Intent(Signup.this, OTP.class);
                        intent.putExtra("mobile", response.body().getMobile());
                        intent.putExtra("otp", response.body().getOtp());
                        intent.putExtra("name", response.body().getName());
                        startActivity(intent);
                        //Toast.makeText(Signup.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(Signup.this, "OTP : " + response.body().getOtp(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Signup.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SaveUser> call, Throwable t) {
                pb.dismiss();
                Toast.makeText(Signup.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", (dialog, which) -> ActivityCompat.requestPermissions(Signup.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE))
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
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
                System.out.println("permission granted");
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}