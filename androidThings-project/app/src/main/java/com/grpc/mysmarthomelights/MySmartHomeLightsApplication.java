package com.grpc.mysmarthomelights;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * MySmartHomeLightsApplication
 *
 * @author (c) 2017, New Means of Payment, BBVA
 */
public class MySmartHomeLightsApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
    }

}
