package com.iamraviraj.mang0055.ottawa.ca;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
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
    implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

  public static final String KEY_BUILDING = "BUILDING";
  AboutDialogFragment mAboutDialog;
  HomeListAdapter adapter;
  SwipeRefreshLayout swipeRefreshList;
  FloatingActionButton btn_addBuilding;
  View empty;
  private RecyclerView buildingView;

  public static String AssetJSONFile(String filename, Context context) throws IOException {
    AssetManager manager = context.getAssets();
    InputStream file = manager.open(filename);
    byte[] formArray = new byte[file.available()];
    file.read(formArray);
    file.close();

    return new String(formArray);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getSupportActionBar().setHomeButtonEnabled(true);
    mAboutDialog = new AboutDialogFragment();

    buildingView = (RecyclerView) findViewById(R.id.list);
    //list.setOnItemClickListener(this);

    // in content do not change the layout size of the RecyclerView
    buildingView.setHasFixedSize(true);

    swipeRefreshList = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_list);
    swipeRefreshList.setOnRefreshListener(this);

    //Empty view if there is not data
    empty = findViewById(R.id.emptyView);

    btn_addBuilding = (FloatingActionButton) findViewById(R.id.btn_addBuilding);
    btn_addBuilding.setOnClickListener(this);
    //Doing network request while loading first time.

    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
    buildingView.setLayoutManager(mLayoutManager);
    buildingView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
    buildingView.setItemAnimator(new DefaultItemAnimator());

    buildingView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy > 0 || dy < 0 && btn_addBuilding.isShown()) btn_addBuilding.hide();
      }

      @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
          btn_addBuilding.show();
        }
        super.onScrollStateChanged(recyclerView, newState);
      }
    });
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
        Collections.sort(adapter.getData(), acending(getComparator(NAME_SORT, ID_SORT)));
        adapter.notifyDataSetChanged();
        buildingView.scrollToPosition(0);
        break;
      case R.id.action_sort_name_dsc:
        Collections.sort(adapter.getData(), decending(getComparator(NAME_SORT, ID_SORT)));
        adapter.notifyDataSetChanged();
        buildingView.scrollToPosition(0);
        break;
      case R.id.action_sort_id:
        Collections.sort(adapter.getData(), decending(getComparator(ID_SORT)));
        adapter.notifyDataSetChanged();
        buildingView.scrollToPosition(0);
        break;
    }
    item.setChecked(true);
    return true;
  }

  @Override public void onRefresh() {
    if (!isNetworkAvailable()) {
      return;
    }
    Call<Buildings> buildingsCall = RestClient.getInstance().getApiService().eventsList();
    buildingsCall.enqueue(new Callback<Buildings>() {
      @Override public void onResponse(Call<Buildings> call, Response<Buildings> response) {
        //Log.e("TAG", response.toString());
        if (response.code() == 404) {
          loadDataLocally();
          return;
        }
        Buildings buildings = response.body();
        if (buildings != null) {
          setDisplayData(buildings);
          swipeRefreshList.setRefreshing(false);
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

  private void loadDataLocally() {
    try {
      String jsonLocation = AssetJSONFile("data.json", getApplicationContext());
      Buildings buildings = new Gson().fromJson(jsonLocation, Buildings.class);
      setDisplayData(buildings);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void setDisplayData(Buildings b) {
    if (b.getBuildings().size() == 0) {
      empty.setVisibility(View.VISIBLE);
    } else {
      adapter = new HomeListAdapter(getApplicationContext(), b.getBuildings());
      buildingView.setAdapter(adapter);
      empty.setVisibility(View.GONE);
      //Collections.sort(adapter.getData(), decending(getComparator(ID_SORT)));
      //adapter.notifyDataSetChanged();
    }
  }

  /**
   * Converting dp to pixel
   */
  private int dpToPx(int dp) {
    Resources r = getResources();
    return Math.round(
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
  }

  /**
   * RecyclerView item decoration - give equal margin around grid item
   */
  public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
      this.spanCount = spanCount;
      this.spacing = spacing;
      this.includeEdge = includeEdge;
    }

    @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
        RecyclerView.State state) {
      int position = parent.getChildAdapterPosition(view); // item position
      int column = position % spanCount; // item column

      if (includeEdge) {
        outRect.left = spacing
            - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
        outRect.right =
            (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

        if (position < spanCount) { // top edge
          outRect.top = spacing;
        }
        outRect.bottom = spacing; // item bottom
      } else {
        outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
        outRect.right = spacing
            - (column + 1) * spacing
            / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
        if (position >= spanCount) {
          outRect.top = spacing; // item top
        }
      }
    }
  }
}
