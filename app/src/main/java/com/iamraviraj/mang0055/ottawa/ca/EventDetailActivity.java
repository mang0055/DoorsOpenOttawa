package com.iamraviraj.mang0055.ottawa.ca;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Locale;
import modal.Building;
import modal.Feature;
import utils.Constant;

/**
 * @author Raviraj Mangukiya (mang0055@algonquinlive.com)
 */
public class EventDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
  ImageView imgBuilding;
  TextView buildingName, buildingAddress, buildingDescription, buildingOpenHours, buildingFeatures;
  private GoogleMap mMap;
  private Geocoder mGeocoder;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_event_detail);

    setUpViews();

    Bundle mBundle = this.getIntent().getExtras();
    if (mBundle != null) {
      Building building =
          new Gson().fromJson(mBundle.getString(MainActivity.KEY_BUILDING), Building.class);
      buildingName.setText(building.getName());
      buildingAddress.setText(building.getAddress());
      buildingDescription.setText(building.getDescription());
      Picasso.with(getApplicationContext())
          .load(Constant.ENDPOINT + building.getImage())
          .into(imgBuilding);
      if (building.getCalendar().size() > 0) {
        StringBuffer mOpenHours = new StringBuffer();
        //mOpenHours.append("Open Doors:: \n");
        for (modal.Calendar c : building.getCalendar()) {
          mOpenHours.append(c.getDate() + " \n");
        }
        buildingOpenHours.setText(mOpenHours.toString());
      }
      if (building.getFeatures()!=null && building.getFeatures().size() > 0) {
        buildingFeatures.setText(printFeatures(building.getFeatures().get(0)));
      }
      pin(building.getAddress());
    }
  }

  private String printFeatures(Feature feature) {
    StringBuffer mAmenities = new StringBuffer();
    mAmenities.append("Amenities:: \n");
    mAmenities.append(String.format(
        " Accessible: %s\n BikeParking? %s\n FamilyFriendly? %s\n FreeParking? %s\n NewBuilding? %s\n OCTranspoNearBy? %s\n PaidParking? %s\n Saturday? %s\n Shuttle? %s\n Sunday? %s\n Washrooms? %s",
        styleString(feature.getAccessible()), styleString(feature.getBikeParking()),
        styleString(feature.getFamilyFriendly()), styleString(feature.getFreeParking()),
        styleString(feature.getNewBuilding()), styleString(feature.getOcTranspoNearby()),
        styleString(feature.getPaidParking()), styleString(feature.getSaturday()),
        styleString(feature.getShuttle()), styleString(feature.getSunday()),
        styleString(feature.getWashrooms())));
    return mAmenities.toString();
  }

  private void setUpViews() {
    imgBuilding = (ImageView) findViewById(R.id.img_building);
    buildingName = (TextView) findViewById(R.id.text_title);
    buildingAddress = (TextView) findViewById(R.id.text_address);
    buildingDescription = (TextView) findViewById(R.id.text_description);
    buildingOpenHours = (TextView) findViewById(R.id.text_openHours);
    buildingFeatures = (TextView) findViewById(R.id.text_features);
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment =
        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
    mGeocoder = new Geocoder(this, Locale.CANADA);
  }

  private String styleString(Boolean value) {
    if (value != null) {
      return value ? "Yes" : "No";
    } else {
      return "information N/A ";
    }
  }

  @Override public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
  }
  /** Locate and pin locationName to the map. */
  private void pin(String locationName) {
    try {
      Address address = mGeocoder.getFromLocationName(locationName, 1).get(0);

      // Fetch the address lines using getAddressLine,
      // join them, and send them to the thread.
      for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
        Log.e("TAG",address.getAddressLine(i));
      }

      LatLng ll = new LatLng(address.getLatitude(), address.getLongitude());
      mMap.addMarker(new MarkerOptions().position(ll).title(locationName));
      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 10));
      Toast.makeText(this, "Pinned: " + locationName, Toast.LENGTH_SHORT).show();
    } catch (Exception e) {
      Toast.makeText(this, "Not found: " + locationName, Toast.LENGTH_SHORT).show();
    }
  }
}
