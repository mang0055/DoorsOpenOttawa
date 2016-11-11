package com.iamraviraj.mang0055.ottawa.ca;

import android.app.Application;
import retrofit.RestClient;

/**
 * @author Raviraj Mangukiya (mang0055@algonquinlive.com)
 */

public class App extends Application {

  private static App mInstance;

  public static synchronized App getInstance() {
    return mInstance;
  }

  @Override public void onCreate() {
    super.onCreate();
    mInstance = this;
    new RestClient();
  }
}
