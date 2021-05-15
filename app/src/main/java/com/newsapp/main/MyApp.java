package com.newsapp.main;

import android.app.Application;
import android.content.Intent;

import com.newsapp.Custom.FontsOverride;
import com.newsapp.R;
import com.newsapp.activities.Home;
import com.newsapp.activities.NewsDetail;
import com.newsapp.constant.Constant;
import com.onesignal.OneSignal;

import org.json.JSONObject;


public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // OneSignal Initialization

//    OneSignal.initWithContext(this);
//    OneSignal.setAppId(getString(R.string.service_key));
        initOneSignal();

//    OneSignal.setNotificationWillShowInForegroundHandler(new OneSignal.OSNotificationWillShowInForegroundHandler() {
//      @Override
//      public void notificationWillShowInForeground(OSNotificationReceivedEvent notificationReceivedEvent) {
//        // Get custom additional data you sent with the notification
////        JSONObject data = notificationReceivedEvent.notification.getAdditionalData();
////
////        notificationReceivedEvent.complete(notification);
//        Intent in=new Intent(getApplicationContext(), DetailScreen.class);
//        in.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(in);
//      }
//    });
        FontsOverride.setDefaultFont(this, "DEFAULT", "Metropolis-Medium.otf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "Metropolis-Medium.otf");
        FontsOverride.setDefaultFont(this, "SERIF", "Metropolis-Medium.otf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "Metropolis-Medium.otf");
        //  This FontsOverride comes from the example I posted above
    }

    private void initOneSignal() {
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(getString(R.string.service_key));

        OneSignal.setNotificationOpenedHandler(
                result -> {
                    JSONObject jsonObject = result.getNotification().getAdditionalData();
                    //try {
                    if (null != jsonObject.opt(Constant.NEWS_ID) && null != jsonObject.opt(Constant.NEWS_ID).toString()) {
                        startActivity(new Intent(this, NewsDetail.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                .putExtra(Constant.NEWS_ID, Integer.parseInt(jsonObject.opt(Constant.NEWS_ID).toString())));
                    } else {
                        startActivity(new Intent(this, Home.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                });
    }
}