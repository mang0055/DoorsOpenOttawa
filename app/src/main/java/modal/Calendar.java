package modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * To hold Each Dates from Calender Json array of Building.
 *
 * @author Raviraj Mangukiya (mang0055@algonquinlive.com)
 */

public class Calendar {

  /**
   * "open_hours": [{"date": "Saturday, June 4, 2016 - 10:00 to 16:00"}],
   */

  @SerializedName("date") @Expose private String date;

  public Calendar(String date) {
    this.date = date;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

}
