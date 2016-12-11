package database;

public class Fav {

  //private variables
  int _id;
  int buildingID;
  boolean favTag;

  // Empty constructor
  public Fav() {
    super();
  }

  public Fav(int _id, int buildingID, boolean favTag) {
    this._id = _id;
    this.buildingID = buildingID;
    this.favTag = favTag;
  }

  public int get_id() {
    return _id;
  }

  public void set_id(int _id) {
    this._id = _id;
  }

  public int getBuildingID() {
    return buildingID;
  }

  public void setBuildingID(int buildingID) {
    this.buildingID = buildingID;
  }

  public boolean isFavTag() {
    return favTag;
  }

  public void setFavTag(boolean favTag) {
    this.favTag = favTag;
  }
}
