package modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import hugo.weaving.DebugLog;
import java.util.ArrayList;
import java.util.List;

/**
 * To hold all Building Model. Parent of @class Building model
 *
 * @author Raviraj Mangukiya (mang0055@algonquinlive.com)
 */

public class Buildings {
  /**
   * Actual Json to understand how each JSON Parameters are assigned as variable.
   *
   * buildings[{Building1},{Building2}] - Json Array contains Buildings.
   */

  @SerializedName("buildings") @Expose public List<Building> buildings = new ArrayList<Building>();

  public Buildings(List<Building> buildings) {
    this.buildings = buildings;
  }
  @DebugLog
  public List<Building> getBuildings() {
    return buildings;
  }

  public void setBuildings(List<Building> buildings) {
    this.buildings = buildings;
  }
}
