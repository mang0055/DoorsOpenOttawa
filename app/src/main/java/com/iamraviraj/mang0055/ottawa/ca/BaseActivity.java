package com.iamraviraj.mang0055.ottawa.ca;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.Toast;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * @author Raviraj Mangukiya (mang0055@algonquinlive.com)
 */

public class BaseActivity extends AppCompatActivity {

  public static String getAPIAuthorisation() {
    String basicAuth =
        "Basic " + Base64.encodeToString(String.format("%s:%s", "mang0055", "password").getBytes(),
            Base64.NO_WRAP);
    return basicAuth;
  }

  boolean isNetworkAvailable() {

    ConnectivityManager connectivityManager =
        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    boolean flag = activeNetworkInfo != null && activeNetworkInfo.isConnected();
    if (!flag) {
      Toast.makeText(getApplicationContext(), "Internet connection is not available.",
          Toast.LENGTH_LONG).show();
    }
    return flag;
  }

  public Interceptor headers = new Interceptor() {
    @Override public okhttp3.Response intercept(Chain chain) throws IOException {
      Request original = chain.request();

      Request request = original.newBuilder()
          .header("Authorization", BaseActivity.getAPIAuthorisation())
          .method(original.method(), original.body())
          .build();
      return chain.proceed(request);
    }
  };
}
