package com.newsapp.main;

import android.app.Application;
import android.content.Intent;

import com.newsapp.Custom.FontsOverride;
import com.newsapp.R;
import com.newsapp.activities.DetailScreen;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;

import org.json.JSONObject;


public class MyApp extends Application {
  @Override
  public void onCreate() {
     super.onCreate();
    // OneSignal Initialization
    OneSignal.initWithContext(this);
    OneSignal.setAppId(getString(R.string.service_key));
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
  }