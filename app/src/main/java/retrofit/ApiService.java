package retrofit;

import modal.Building;
import modal.Buildings;
import modal.MapAddressModel;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by aark on 2016-10-18.
 */

public interface ApiService {

  @GET("buildings") Call<Buildings> eventsList();

  @GET("https://maps.googleapis.com/maps/api/geocode/json?sensor=false")
  Call<MapAddressModel> callGoogleJSMapAPI(@Query("address") String address,
      @Query("key") String key);

  @POST("buildings") Call<ResponseBody> postBuilding(@Body Building mBulding);

  @PUT("buildings/{buildingId}") Call<ResponseBody> updateBuilding(
      @Path("buildingId") int buildingId, @Body Building mBuilding);

  @Multipart @POST("buildings/{buildingId}/image") Call<ResponseBody> uploadBuildingPic(
      @Path("buildingId") int buildingId, @Part MultipartBody.Part file);

  @DELETE("buildings/{buildingId}") Call<ResponseBody> deleteBuilding(
      @Path("buildingId") int buildingId);

  @GET("buildings/{buildingId}") Call<Building> getEvent(@Path("buildingId") int buildingId);
}
