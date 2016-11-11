package modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * To Hold all Amenities of Building
 *
 * @author Raviraj Mangukiya (mang0055@algonquinlive.com)
 */

public class Feature {
  /**
   * "features": [{"accessible": true},{"bike_parking": true},{"family_friendly":true},
   * {"free_parking": true},{"new_building": false},{"oc_transpo_nearby":true},
   * {"paid_parking": false},{"saturday": true},{"shuttle": false},{"sunday": true},
   * {"washrooms": true}]
   */
  @SerializedName("accessible") @Expose private Boolean accessible;
  @SerializedName("bike_parking") @Expose private Boolean bikeParking;
  @SerializedName("family_friendly") @Expose private Boolean familyFriendly;
  @SerializedName("free_parking") @Expose private Boolean freeParking;
  @SerializedName("new_building") @Expose private Boolean newBuilding;
  @SerializedName("oc_transpo_nearby") @Expose private Boolean ocTranspoNearby;
  @SerializedName("paid_parking") @Expose private Boolean paidParking;
  @SerializedName("saturday") @Expose private Boolean saturday;
  @SerializedName("shuttle") @Expose private Boolean shuttle;
  @SerializedName("sunday") @Expose private Boolean sunday;
  @SerializedName("washrooms") @Expose private Boolean washrooms;

  public Feature(Boolean accessible, Boolean bikeParking, Boolean familyFriendly,
      Boolean freeParking, Boolean newBuilding, Boolean ocTranspoNearby, Boolean paidParking,
      Boolean saturday, Boolean shuttle, Boolean sunday, Boolean washrooms) {
    this.accessible = accessible;
    this.bikeParking = bikeParking;
    this.familyFriendly = familyFriendly;
    this.freeParking = freeParking;
    this.newBuilding = newBuilding;
    this.ocTranspoNearby = ocTranspoNearby;
    this.paidParking = paidParking;
    this.saturday = saturday;
    this.shuttle = shuttle;
    this.sunday = sunday;
    this.washrooms = washrooms;
  }

  public Boolean getAccessible() {
    return accessible;
  }

  public void setAccessible(Boolean accessible) {
    this.accessible = accessible;
  }

  public Boolean getBikeParking() {
    return bikeParking;
  }

  public void setBikeParking(Boolean bikeParking) {
    this.bikeParking = bikeParking;
  }

  public Boolean getFamilyFriendly() {
    return familyFriendly;
  }

  public void setFamilyFriendly(Boolean familyFriendly) {
    this.familyFriendly = familyFriendly;
  }

  public Boolean getFreeParking() {
    return freeParking;
  }

  public void setFreeParking(Boolean freeParking) {
    this.freeParking = freeParking;
  }

  public Boolean getNewBuilding() {
    return newBuilding;
  }

  public void setNewBuilding(Boolean newBuilding) {
    this.newBuilding = newBuilding;
  }

  public Boolean getOcTranspoNearby() {
    return ocTranspoNearby;
  }

  public void setOcTranspoNearby(Boolean ocTranspoNearby) {
    this.ocTranspoNearby = ocTranspoNearby;
  }

  public Boolean getPaidParking() {
    return paidParking;
  }

  public void setPaidParking(Boolean paidParking) {
    this.paidParking = paidParking;
  }

  public Boolean getSaturday() {
    return saturday;
  }

  public void setSaturday(Boolean saturday) {
    this.saturday = saturday;
  }

  public Boolean getShuttle() {
    return shuttle;
  }

  public void setShuttle(Boolean shuttle) {
    this.shuttle = shuttle;
  }

  public Boolean getSunday() {
    return sunday;
  }

  public void setSunday(Boolean sunday) {
    this.sunday = sunday;
  }

  public Boolean getWashrooms() {
    return washrooms;
  }

  public void setWashrooms(Boolean washrooms) {
    this.washrooms = washrooms;
  }
}
