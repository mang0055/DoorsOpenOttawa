package com.iamraviraj.mang0055.ottawa.ca;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.google.gson.Gson;
import java.util.Collections;
import modal.Buildings;
import retrofit.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.iamraviraj.mang0055.ottawa.ca.HomeListAdapter.BuildingComparator.ID_SORT;
import static com.iamraviraj.mang0055.ottawa.ca.HomeListAdapter.BuildingComparator.NAME_SORT;
import static com.iamraviraj.mang0055.ottawa.ca.HomeListAdapter.BuildingComparator.acending;
import static com.iamraviraj.mang0055.ottawa.ca.HomeListAdapter.BuildingComparator.decending;
import static com.iamraviraj.mang0055.ottawa.ca.HomeListAdapter.BuildingComparator.getComparator;

/**
 * Application get events from Ottawa.ca and display it in List
 *
 * @author Raviraj Mangukiya (mang0055@algonquinlive.com)
 */
public class MainActivity extends BaseActivity
    implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
    View.OnClickListener {

  AboutDialogFragment mAboutDialog;
  GridView mListView;
  HomeListAdapter adapter;
  SwipeRefreshLayout swipeRefreshList;
  public static final String KEY_BUILDING = "BUILDING";
  FloatingActionButton btn_addBuilding;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mAboutDialog = new AboutDialogFragment();

    mListView = (GridView) findViewById(R.id.list);
    mListView.setOnItemClickListener(this);

    swipeRefreshList = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_list);
    swipeRefreshList.setOnRefreshListener(this);

    //Empty view if there is not data
    View empty = findViewById(R.id.emptyView);
    mListView.setEmptyView(empty);

    btn_addBuilding = (FloatingActionButton) findViewById(R.id.btn_addBuilding);
    btn_addBuilding.setOnClickListener(this);
    //Doing network request while loading first time.
    this.onRefresh();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

      @Override public boolean onQueryTextSubmit(String query) {
        adapter.getFilter().filter(query);
        return true;
      }

      @Override public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
      }
    });
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle presses on the action bar items
    switch (item.getItemId()) {
      case R.id.action_about:
        mAboutDialog.show(getFragmentManager(), "ABOUT_DIALOG_TAG");
        break;
      case R.id.action_sort_name_asc:
        Collections.sort(adapter.getData(), acending(getComparator(ID_SORT)));
        adapter.notifyDataSetChanged();
        break;
      case R.id.action_sort_name_dsc:
        Collections.sort(adapter.getData(), decending(getComparator(ID_SORT)));
        adapter.notifyDataSetChanged();
        break;
      case R.id.action_sort_name:
        Collections.sort(adapter.getData(), decending(getComparator(NAME_SORT)));
        adapter.notifyDataSetChanged();
        break;
    }
    item.setChecked(true);
    return true;
  }

  //List/Grid View Item Click
  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    switch (parent.getId()) {
      case R.id.list:
        HomeListAdapter mAdapter = (HomeListAdapter) parent.getAdapter();
        Log.e("TAG", new Gson().toJson(mAdapter.getItem(position)));

        Intent intentEventDetail = new Intent(getApplicationContext(), EventDetailActivity.class);
        intentEventDetail.putExtra(KEY_BUILDING, new Gson().toJson(mAdapter.getItem(position)));
        startActivity(intentEventDetail);

        break;
    }
  }

  @Override public void onRefresh() {
    if (!isNetworkAvailable()) {
      return;
    }
    Call<Buildings> buildingsCall = RestClient.getInstance().getApiService().eventsList();
    buildingsCall.enqueue(new Callback<Buildings>() {
      @Override public void onResponse(Call<Buildings> call, Response<Buildings> response) {
        //Log.e("TAG", response.toString());
        Buildings buildings = response.body();
        if (buildings != null) {
          adapter = new HomeListAdapter(getApplicationContext(), buildings.getBuildings());
          mListView.setAdapter(adapter);
          swipeRefreshList.setRefreshing(false);
          Collections.sort(adapter.getData(), decending(getComparator(ID_SORT)));
          adapter.notifyDataSetChanged();
        }
      }

      @Override public void onFailure(Call<Buildings> call, Throwable t) {
        Log.e("TAG", "Request Fail ", t);
        swipeRefreshList.setRefreshing(false);
      }
    });
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_addBuilding:
        startActivity(new Intent(this, NewBuildingActivity.class));
        break;
    }
  }
}
