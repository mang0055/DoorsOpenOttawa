package com.iamraviraj.mang0055.ottawa.ca;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

/**
 * @author Raviraj Mangukiya (mang0055@algonquinlive.com)
 */

public class BaseActivity extends AppCompatActivity {

  boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager =
        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }

  String getAPIAuthorisation() {
    String basicAuth = "Basic " + Base64.encodeToString(String.format("%s:%s", "mang0055", "password").getBytes(), Base64.NO_WRAP);
    return basicAuth;
  }
}
