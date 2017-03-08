package com.iamraviraj.mang0055.ottawa.ca;

import android.app.Application;
import android.content.SharedPreferences;
import com.facebook.stetho.Stetho;
import retrofit.RestClient;

/**
 * @author Raviraj Mangukiya (mang0055@algonquinlive.com)
 */

public class App extends Application {

  public static SharedPreferences mediapref = null;
  private static App mInstance;

  public static synchronized App getInstance() {
    return mInstance;
  }

  public static void storeBoolean(String key, boolean value) {
    mediapref.edit().putBoolean(key, value).commit();
  }

  public static boolean getStoredBoolean(String key) {
    return mediapref.getBoolean(key, false);
  }

  public static void storeString(String key, String value) {
    mediapref.edit().putString(key, value).commit();
  }

  public static String getString(String key) {
    return mediapref.getString(key,null);
  }

  @Override public void onCreate() {
    super.onCreate();
    mInstance = this;
    Stetho.initializeWithDefaults(this);
    mediapref = this.getSharedPreferences(getResources().getString(R.string.app_name), 0);
    new RestClient();
  }
}
