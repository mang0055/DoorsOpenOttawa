package modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * To create Building model
 *
 * @author Raviraj Mangukiya (mang0055@algonquinlive.com)
 */

public class Building {
  /**
   * Actual Json to understand how each Building JSON Object Parameters are assigned as variable.
   *
   * {
   * "buildingId":0,
   *
   * "name":"AIDS Committee of Ottawa",
   *
   * "address":"19 Main St.",
   *
   * "image":"images/AIDSOttawa.jpg",
   *
   * "open_hours":[{"date":"Saturday, June 4, 2016 - 10:00 to 16:00"},{"date":"Sunday, June 5, 2016 - 10:00 to 16:00"}],
   *
   * "description":"The AIDS Committee of Ottawa (ACO) has served the HIV/AIDS community for 30 years,
   * providing education and support services around HIV/AIDS. Built in 1961 and overlooking the
   * Rideau Canal, the home of the ACO features a drop-in centre known as the Living Room - a place of
   * comfort and care that pays tribute to the origins of the HIV/AIDS movement in Canada in the early
   * 80s.",
   *
   * "features":[{"accessible":true},{"bike_parking":true}]
   * }
   */
  @Expose
  private Integer buildingId;
  @Expose
  private String name;
  @Expose
  private String address;
  @Expose
  private String image;
  @Expose
  private String description;
  @SerializedName("open_hours") @Expose private List<Calendar> calendar = new ArrayList<Calendar>();
  private List<Feature> features = new ArrayList<Feature>();
  private boolean favorite = false;
  public Building(Integer buildingId, String name, String address, String image, String description,
      List<Calendar> calendar, List<Feature> features,boolean favorite) {
    this.buildingId = buildingId;
    this.name = name;
    this.address = address;
    this.image = image;
    this.description=description;
    this.calendar = calendar;
    this.features = features;
    this.favorite = favorite;
  }

  public Building() {

  }

  public Integer getBuildingId() {
    return buildingId;
  }

  public void setBuildingId(Integer buildingId) {
    this.buildingId = buildingId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address+" Ottawa, ON";
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Calendar> getCalendar() {
    return calendar;
  }

  public void setCalendar(List<Calendar> calendar) {
    this.calendar = calendar;
  }

  public List<Feature> getFeatures() {
    return features;
  }

  public void setFeatures(List<Feature> features) {
    this.features = features;
  }

  public boolean isFavorite() {
    return favorite;
  }

  public void setFavorite(boolean favorite) {
    this.favorite = favorite;
  }
}
