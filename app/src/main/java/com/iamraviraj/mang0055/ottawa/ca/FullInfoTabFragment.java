package com.iamraviraj.mang0055.ottawa.ca;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import modal.Building;
import modal.Buildings;
import retrofit.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;

public class FullInfoTabFragment extends Fragment {

  private static final String EXTRA_SRORT_CARD_MODEL = "EXTRA_SRORT_CARD_MODEL";
  //    String transitionTag;
  private Building sportCardModel;
  private Toolbar toolbar;
  private ImageView ivPhoto;
  private RecyclerView rvAthletics;

  public static FullInfoTabFragment newInstance(Building sportCardModel) {
    FullInfoTabFragment fragment = new FullInfoTabFragment();
    Bundle args = new Bundle();
    args.putString(EXTRA_SRORT_CARD_MODEL, new Gson().toJson(sportCardModel));
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      //sportCardModel = getArguments().getParcelable(EXTRA_SRORT_CARD_MODEL);
      sportCardModel=new Gson().fromJson(getArguments().getString(EXTRA_SRORT_CARD_MODEL),Building.class);
    }
    if (savedInstanceState != null) {
      //sportCardModel = savedInstanceState.getParcelable(EXTRA_SRORT_CARD_MODEL);
      sportCardModel=new Gson().fromJson(savedInstanceState.getString(EXTRA_SRORT_CARD_MODEL),Building.class);
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_full_info, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
    rvAthletics = (RecyclerView) view.findViewById(R.id.rvAthletics);
    return view;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    toolbar.setTitle(sportCardModel.getName());
    toolbar.setNavigationIcon(android.R.drawable.ic_media_previous);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        getActivity().onBackPressed();
      }
    });
    //toolbar.setBackgroundColor(
    //    ContextCompat.getColor(getContext(), sportCardModel.getBackgroundColorResId()));

    Picasso.with(getContext()).load(Constant.ENDPOINT + sportCardModel.getImage()).into(ivPhoto);
    //

    ScoreAdapter scoreAdapter = new ScoreAdapter();
    onRefresh(scoreAdapter);

    rvAthletics.setAdapter(scoreAdapter);
    rvAthletics.setLayoutManager(
        new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    rvAthletics.setItemAnimator(new DefaultItemAnimator());
    rvAthletics.addItemDecoration(new DividerItemDecoration(getContext()));
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    outState.putString(EXTRA_SRORT_CARD_MODEL, new Gson().toJson(sportCardModel));
    super.onSaveInstanceState(outState);
  }

  static class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[] { android.R.attr.listDivider };

    private Drawable mDivider;

    /**
     * Default divider will be used
     */
    public DividerItemDecoration(Context context) {
      final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
      mDivider = styledAttributes.getDrawable(0);
      styledAttributes.recycle();
    }

    /**
     * Custom divider will be used
     */
    public DividerItemDecoration(Context context, int resId) {
      mDivider = ContextCompat.getDrawable(context, resId);
    }

    @Override public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
      int left = parent.getPaddingLeft();
      int right = parent.getWidth() - parent.getPaddingRight();

      int childCount = parent.getChildCount();
      for (int i = 0; i < childCount; i++) {
        View child = parent.getChildAt(i);

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

        int top = child.getBottom() + params.bottomMargin;
        int bottom = top + mDivider.getIntrinsicHeight();

        mDivider.setBounds(left, top, right, bottom);
        mDivider.draw(c);
      }
    }
  }

  public void onRefresh(final ScoreAdapter scoreAdapter) {

    Call<Buildings> buildingsCall = RestClient.getInstance().getApiService().eventsList();
    buildingsCall.enqueue(new Callback<Buildings>() {
      @Override public void onResponse(Call<Buildings> call, Response<Buildings> response) {
        //Log.e("TAG", response.toString());
        Buildings buildings = response.body();
        if (buildings != null) {
          scoreAdapter.addItems(buildings.getBuildings());
          scoreAdapter.notifyDataSetChanged();
        }
      }

      @Override public void onFailure(Call<Buildings> call, Throwable t) {
        Log.e("TAG", "Request Fail ", t);
      }
    });
  }
}
