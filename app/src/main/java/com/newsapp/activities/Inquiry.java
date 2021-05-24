package com.newsapp.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.newsapp.R;
import com.newsapp.api.ApiListeners;
import com.newsapp.constant.Constant;
import com.newsapp.model.Feedback;
import com.newsapp.model.StaticPage;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Inquiry extends AppCompatActivity {

    public ApiListeners apiListeners;
    ImageView imgback, imglogo;
    LinearLayout lin_about, lin_termscondition, lin_contact;
    LinearLayout lincontact, lintnc, linabout;
    WebView webtnc, webabout;
    EditText edt_fullname, edt_mobile, edt_inquiry, edt_email;
    Button btn_submit;
    TextView txtabout, txttnc, txtcontact;
    ProgressDialog pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        Objects.requireNonNull(getSupportActionBar()).hide();
        if (Constant.get_sp(getApplicationContext(), "mobile").isEmpty()) {
            finish();
        }
        lin_about = findViewById(R.id.lin_about);
        imglogo = findViewById(R.id.imglogo);
        lin_termscondition = findViewById(R.id.lin_termscondition);
        lin_contact = findViewById(R.id.lin_contact);
        lincontact = findViewById(R.id.lincontact);
        lintnc = findViewById(R.id.lintnc);
        linabout = findViewById(R.id.linabout);
        imgback = findViewById(R.id.imgback);
        webtnc = findViewById(R.id.webtnc);
        webabout = findViewById(R.id.webabout);
        edt_fullname = findViewById(R.id.edt_fullname);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_email = findViewById(R.id.edt_email);
        edt_inquiry = findViewById(R.id.edt_inquiry);
        txtabout = findViewById(R.id.txtabout);
        txttnc = findViewById(R.id.txttnc);
        txtcontact = findViewById(R.id.txtcontact);
        btn_submit = findViewById(R.id.btn_submit);

        Init();

        Picasso.with(getApplicationContext())
                .load(Constant.get_sp(getApplicationContext(), Constant.APP_LOGO))
                .error(R.drawable.error_load)
                .placeholder(R.drawable.loading)
                .into(imglogo);

        imgback.setOnClickListener(view -> finish());
        lin_about.setOnClickListener(view -> {
            txtabout.setBackgroundResource(R.drawable.rounded_tab);
            txtabout.setTextColor(Color.WHITE);
            txttnc.setBackgroundResource(R.drawable.rounded_tab_unselec);
            txttnc.setTextColor(Color.BLACK);
            txtcontact.setBackgroundResource(R.drawable.rounded_tab_unselec);
            txtcontact.setTextColor(Color.BLACK);

            lincontact.setVisibility(View.GONE);
            lintnc.setVisibility(View.GONE);
            linabout.setVisibility(View.VISIBLE);
        });
        lin_termscondition.setOnClickListener(view -> {
            txtabout.setBackgroundResource(R.drawable.rounded_tab_unselec);
            txtabout.setTextColor(Color.BLACK);
            txttnc.setBackgroundResource(R.drawable.rounded_tab);
            txttnc.setTextColor(Color.WHITE);
            txtcontact.setBackgroundResource(R.drawable.rounded_tab_unselec);
            txtcontact.setTextColor(Color.BLACK);

            lincontact.setVisibility(View.GONE);
            lintnc.setVisibility(View.VISIBLE);
            linabout.setVisibility(View.GONE);
        });
        lin_contact.setOnClickListener(view -> {
            txtabout.setBackgroundResource(R.drawable.rounded_tab_unselec);
            txtabout.setTextColor(Color.BLACK);
            txttnc.setBackgroundResource(R.drawable.rounded_tab_unselec);
            txttnc.setTextColor(Color.BLACK);
            txtcontact.setBackgroundResource(R.drawable.rounded_tab);
            txtcontact.setTextColor(Color.WHITE);

            lincontact.setVisibility(View.VISIBLE);
            lintnc.setVisibility(View.GONE);
            linabout.setVisibility(View.GONE);
        });
        btn_submit.setOnClickListener(
                view -> {
                    boolean isValid = true;
                    String msg = "";
                    if (edt_fullname.getText().toString().isEmpty()) {
                        isValid = true;
                        msg = "Full name must  required.";
                        Toast.makeText(Inquiry.this, msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (edt_mobile.getText().toString().isEmpty()) {
                        isValid = true;
                        msg = "Mobile number must  required.";
                        Toast.makeText(Inquiry.this, msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (edt_email.getText().toString().isEmpty()) {
                        isValid = true;
                        msg = "Email must  required.";
                        Toast.makeText(Inquiry.this, msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (edt_inquiry.getText().toString().isEmpty()) {
                        isValid = true;
                        msg = "Inquiry must  required.";
                        Toast.makeText(Inquiry.this, msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (isValid) {
                        sendFeedback();
                    }
                });
        getStaticContent();
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
        pb = new ProgressDialog(Inquiry.this);
        pb.setMessage("Please Wait...!");
        pb.setCancelable(false);
    }

    public void getStaticContent() {
        pb.show();
        final Call<StaticPage> getUserInfoVoCall = apiListeners.StaticPage();

        getUserInfoVoCall.enqueue(new Callback<StaticPage>() {
            @Override
            public void onResponse(Call<StaticPage> call, Response<StaticPage> response) {
                pb.dismiss();
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            String text;
                            if (response.body().getData().get(i).getName().equalsIgnoreCase("Terms & Conditions")) {
                                text = "<html><body  style=\"text-align:justify;\">";
                                text += response.body().getData().get(i).getDescription();
                                text += "</body></html>";
                                webtnc.loadData(text, "text/html", "UTF-8");
                            }
                            if (response.body().getData().get(i).getName().equalsIgnoreCase("About Us")) {
                                text = "<html><body  style=\"text-align:justify;\">";
                                text += response.body().getData().get(i).getDescription();
                                text += "</body></html>";
                                webabout.loadData(text, "text/html", "UTF-8");
                            }
                        }
                    } else {
                        Toast.makeText(Inquiry.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<StaticPage> call, Throwable t) {
                pb.dismiss();
                Toast.makeText(Inquiry.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendFeedback() {
        pb.show();
        final Call<Feedback> getUserInfoVoCall = apiListeners.Feedback(edt_fullname.getText().toString(), edt_mobile.getText().toString(), edt_inquiry.getText().toString(), edt_email.getText().toString());

        getUserInfoVoCall.enqueue(new Callback<Feedback>() {
            @Override
            public void onResponse(Call<Feedback> call, Response<Feedback> response) {
                pb.dismiss();
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        Toast.makeText(Inquiry.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(Inquiry.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Feedback> call, Throwable t) {
                pb.dismiss();
                Toast.makeText(Inquiry.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
}