package modal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aark on 2016-10-18.
 */

public class Building {

  private Integer buildingId;
  private String name;
  private String address;
  private String image;
  private List<Calendar> calendar = new ArrayList<Calendar>();
  private List<Feature> features = new ArrayList<Feature>();

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
    return address;
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
}
