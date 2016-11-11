package com.iamraviraj.mang0055.ottawa.ca;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.google.gson.Gson;
import modal.Buildings;
import retrofit.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Application get events from Ottawa.ca and display it in List
 *
 * @author Raviraj Mangukiya (mang0055@algonquinlive.com)
 */
public class MainActivity extends BaseActivity
    implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

  AboutDialogFragment mAboutDialog;
  GridView mListView;
  HomeListAdapter adapter;
  SwipeRefreshLayout swipeRefreshList;

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

    //Doing network request while loading first time.
    this.onRefresh();
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle presses on the action bar items
    switch (item.getItemId()) {

      case R.id.action_about:
        mAboutDialog.show(getFragmentManager(), "ABOUT_DIALOG_TAG");
        return true;
      default:
        Toast.makeText(this, "MenuItem: " + item.getTitle(), Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }
  }

  //List/Grid View Item Click
  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    switch (parent.getId()) {
      case R.id.list:
        HomeListAdapter mAdapter = (HomeListAdapter) parent.getAdapter();
        Log.e("TAG", new Gson().toJson(mAdapter.getItem(position)));
        Toast.makeText(getApplicationContext(),
            mAdapter.getItem(position).getCalendar().size() + " Dates available.",
            Toast.LENGTH_SHORT).show();
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
        adapter = new HomeListAdapter(getApplicationContext(), buildings.getBuildings());
        mListView.setAdapter(adapter);
        swipeRefreshList.setRefreshing(false);
      }

      @Override public void onFailure(Call<Buildings> call, Throwable t) {
        Log.e("TAG", "Request Fail ", t);
        swipeRefreshList.setRefreshing(false);
      }
    });
  }
}
