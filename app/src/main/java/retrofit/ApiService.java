package retrofit;

import modal.Buildings;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by aark on 2016-10-18.
 */

public interface ApiService {

  @GET("buildings") Call<Buildings> eventsList();
}
