package modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aark on 2016-10-18.
 */

public class Buildings {
    @SerializedName("buildings")
    @Expose
    public List<Building> buildings = new ArrayList<Building>();

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }
}
