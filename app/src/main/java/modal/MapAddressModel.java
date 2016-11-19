package modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aark on 2016-11-18.
 */

public class MapAddressModel {

  @SerializedName("results") @Expose public List<Result> results = new ArrayList<Result>();

  public List<Result> getResults() {
    return results;
  }

  public class Result {

    @SerializedName("geometry") @Expose public Geometry geometry;

    public Geometry getGeometry() {
      return geometry;
    }
  }

  public class Geometry {

    @SerializedName("location") @Expose public Location location;

    public Location getLocation() {
      return location;
    }
  }

  public class Location {

    @SerializedName("lat") @Expose public double lat;
    @SerializedName("lng") @Expose public double lng;

    public double getLat() {
      return lat;
    }

    public double getLng() {
      return lng;
    }
  }
}
