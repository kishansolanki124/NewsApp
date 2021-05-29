package com.excuseme.newsapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.excuseme.newsapp.R;
import com.excuseme.newsapp.constant.Constant;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {

    LinearLayout lin_bookmark, lin_appinfo, lin_rateourapp, lin_invitefriends, lin_logout;
    TextView txt_welcome;
    ImageView img_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        if (Constant.get_sp(getApplicationContext(), "mobile").isEmpty()) {
            finish();
        }
        Constant.setBottomMenuSelected(Profile.this, 3);
        lin_bookmark = findViewById(R.id.lin_bookmark);
        img_logo = findViewById(R.id.img_logo);
        lin_appinfo = findViewById(R.id.lin_appinfo);
        lin_rateourapp = findViewById(R.id.lin_rateourapp);
        lin_invitefriends = findViewById(R.id.lin_invitefriends);
        lin_logout = findViewById(R.id.lin_logout);
        txt_welcome = findViewById(R.id.txt_welcome);

        Picasso.with(getApplicationContext())
                .load(Constant.get_sp(getApplicationContext(), Constant.APP_LOGO))
                .error(R.drawable.error_load)
                .placeholder(R.drawable.loading)
                .into(img_logo);

        txt_welcome.setText(HtmlCompat.fromHtml("Welcome <b>" + Constant.get_sp(Profile.this, "name") + "</b>", HtmlCompat.FROM_HTML_MODE_COMPACT));
        lin_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, Bookmark.class));
            }
        });
        lin_appinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, Inquiry.class));
            }
        });
        lin_rateourapp.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(Constant.get_sp(getApplicationContext(), Constant.Update_link))));

//                Intent sendIntent = new Intent();
//                sendIntent.setAction(Intent.ACTION_SEND);
//                sendIntent.putExtra(Intent.EXTRA_TEXT,
//                        "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
//                sendIntent.setType("text/plain");
//                startActivity(sendIntent);
        });
        lin_invitefriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                String shareBody = Constant.get_sp(getApplicationContext(), Constant.Appsharemsg);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Invite Friends");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, "Share using"));
            }
        });
        lin_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.save_sp(Profile.this, "", "");
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
}