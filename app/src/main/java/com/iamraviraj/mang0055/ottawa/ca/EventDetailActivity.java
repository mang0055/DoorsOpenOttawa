package com.iamraviraj.mang0055.ottawa.ca;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import modal.Building;
import modal.Feature;
import modal.MapAddressModel;
import retrofit.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;

/**
 * @author Raviraj Mangukiya (mang0055@algonquinlive.com)
 */
public class EventDetailActivity extends BaseActivity implements OnMapReadyCallback {
  ImageView imgBuilding;
  TextView buildingName, buildingAddress, buildingDescription, buildingOpenHours, buildingFeatures;
  private GoogleMap mMap;
  private Geocoder mGeocoder;
  Building building = null;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_event_detail);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setUpViews();

    Bundle mBundle = this.getIntent().getExtras();
    if (mBundle != null) {
      building = new Gson().fromJson(mBundle.getString(MainActivity.KEY_BUILDING), Building.class);
      buildingName.setText(building.getName());
      buildingAddress.setText(building.getAddress());
      buildingDescription.setText(building.getDescription());
      Picasso.with(getApplicationContext())
          .load(Constant.ENDPOINT + building.getImage())
          .into(imgBuilding);
      if (building.getCalendar().size() > 0) {
        StringBuffer mOpenHours = new StringBuffer();
        mOpenHours.append("Open Doors:: \n");
        for (modal.Calendar c : building.getCalendar()) {
          mOpenHours.append(c.getDate() + " \n");
        }
        buildingOpenHours.setText(mOpenHours.toString());
      }
      if (building.getFeatures() != null && building.getFeatures().size() > 0) {
        buildingFeatures.setText(printFeatures(building.getFeatures().get(0)));
      }
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_edit_building, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle presses on the action bar items
    switch (item.getItemId()) {

      case android.R.id.home:
        finish();
        break;
      case R.id.action_edit:
        if (building.getBuildingId().intValue() > Constant.TOTAL_BUILDING) {
          startActivity(
              new Intent(getApplicationContext(), NewBuildingActivity.class).putExtra("Building",
                  new Gson().toJson(building)));
        } else {
          Snackbar.make(imgBuilding, "You can not edit this building.", Snackbar.LENGTH_LONG)
              .show();
        }
        break;
    }
    return true;
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
    if (building != null && !building.getAddress().isEmpty()) {
      fetchLatLongFromAddress(building.getAddress());
    }
  }

  private void showLocation(String locationName, LatLng ll) {
    try {
      mMap.addMarker(new MarkerOptions().position(ll).title(locationName));
      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 10));
      Toast.makeText(this, "Pinned: " + locationName, Toast.LENGTH_SHORT).show();
    } catch (Exception e) {
      Toast.makeText(this, "Not found: " + locationName, Toast.LENGTH_SHORT).show();
    }
  }

  public void fetchLatLongFromAddress(String locationName) {
    if (!mGeocoder.isPresent()) {
      Log.w("TAG", "Geocoder implementation not present !");
    }

    try {
      List<Address> addresses = mGeocoder.getFromLocationName(locationName, 100);
      int tentatives = 0;
      while (addresses.size() == 0 && (tentatives < 10)) {
        addresses = mGeocoder.getFromLocationName(locationName, 1);
        tentatives++;
      }

      if (addresses.size() > 0) {
        Log.d("TAG",
            "reverse Geocoding : locationName " + locationName + "Latitude " + addresses.get(0)
                .getLatitude());
        showLocation(locationName,
            new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude()));
      } else {
        //use http api
        Log.d("TAG", "Use HTTP API to fetch Location");
        if (isNetworkAvailable()) fetchLocationInfo(locationName);
      }
    } catch (IOException e) {
      Log.d(EventDetailActivity.class.getName(),
          "not possible finding LatLng for Address : " + locationName);
    }
  }

  private void fetchLocationInfo(final String locationName) {

    Call<MapAddressModel> tempJson = RestClient.getInstance()
        .getApiService()
        .callGoogleJSMapAPI(locationName, getString(R.string.google_maps_key));
    tempJson.enqueue(new Callback<MapAddressModel>() {
      @Override
      public void onResponse(Call<MapAddressModel> call, Response<MapAddressModel> response) {
        MapAddressModel mMapAddressModel = response.body();
        Log.e("TAG", new Gson().toJson(mMapAddressModel));
        showLocation(locationName,
            new LatLng(mMapAddressModel.getResults().get(0).getGeometry().getLocation().getLat(),
                mMapAddressModel.getResults().get(0).getGeometry().getLocation().getLng()));
      }

      @Override public void onFailure(Call<MapAddressModel> call, Throwable t) {
        Log.e("TAG", "Error", t);
      }
    });
  }
}
