package retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.Constant;

public class RestClient {

  private static final String BASE_URL = Constant.ENDPOINT;
  private static RestClient instance;
  private ApiService apiService;

  public RestClient() {
    instance = this;

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

    //For Log Purpose not required in final build
    if (Constant.isDebug) {
      HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
      logging.setLevel(HttpLoggingInterceptor.Level.BODY);
      httpClient.addInterceptor(logging);
    }

    Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient.build())
        .build();
    apiService = retrofit.create(ApiService.class);
  }

  public static RestClient getInstance() {
    return instance;
  }

  public ApiService getApiService() {
    return apiService;
  }
}
