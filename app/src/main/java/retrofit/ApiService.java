package retrofit;

import modal.Buildings;
import modal.MapAddressModel;
import org.json.JSONArray;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by aark on 2016-10-18.
 */

public interface ApiService {

  @GET("buildings") Call<Buildings> eventsList();

  @GET("https://maps.googleapis.com/maps/api/geocode/json?sensor=false")
  Call<MapAddressModel> callGoogleJSMapAPI(@Query("address") String address,@Query("key") String key);
}
