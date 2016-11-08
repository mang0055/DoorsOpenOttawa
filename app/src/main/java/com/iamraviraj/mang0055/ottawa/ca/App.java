package com.iamraviraj.mang0055.ottawa.ca;

import android.app.Application;

import com.squareup.picasso.Picasso;

import retrofit.RestClient;

/**
 * Created by aark on 2016-10-18.
 */

public class App extends Application {
    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        new RestClient();
    }

    public static synchronized App getInstance() {
        return mInstance;
    }
}
