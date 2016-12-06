package com.iamraviraj.mang0055.ottawa.ca;

import android.app.Application;
import android.content.SharedPreferences;
import retrofit.RestClient;

/**
 * @author Raviraj Mangukiya (mang0055@algonquinlive.com)
 */

public class App extends Application {

  private static App mInstance;
  public static SharedPreferences mediapref = null;

  public static synchronized App getInstance() {
    return mInstance;
  }

  @Override public void onCreate() {
    super.onCreate();
    mInstance = this;
    mediapref = this.getSharedPreferences(getResources().getString(R.string.app_name), 0);
    new RestClient();
  }

  public static void storeBoolean(String key, boolean value) {
    mediapref.edit().putBoolean(key, value).commit();
  }

  public static boolean getStoredBoolean(String key) {
    return mediapref.getBoolean(key, false);
  }
}
