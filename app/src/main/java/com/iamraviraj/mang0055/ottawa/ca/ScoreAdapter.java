package com.iamraviraj.mang0055.ottawa.ca;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import modal.Building;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.AthleticHolder> {

  private final List<Building> mItems = new ArrayList<>();

  public void addItems(@NonNull Collection<Building> items) {
    mItems.addAll(items);
    notifyItemRangeInserted(mItems.size() - items.size() - 1, items.size());
  }

  public void clear() {
    mItems.clear();
    notifyDataSetChanged();
  }

  @Override public AthleticHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
    return new AthleticHolder(view);
  }

  @Override public void onBindViewHolder(AthleticHolder holder, int position) {
    Building item = mItems.get(position);
    holder.tvAthleticName.setText(item.getName());
    holder.tvCountry.setText(item.getName());
    holder.tvScore.setText(String.valueOf(item.getBuildingId()));
    //Picasso.with(this).load(Constant.ENDPOINT + item.getImage()).into(holder.ivAthleticFlag);

  }

  @Override public int getItemCount() {
    return mItems.size();
  }

  class AthleticHolder extends RecyclerView.ViewHolder {
    ImageView ivAthleticFlag;
    TextView tvCountry;
    TextView tvAthleticName;
    TextView tvScore;

    AthleticHolder(View itemView) {
      super(itemView);
      ivAthleticFlag = (ImageView) itemView.findViewById(R.id.ivAthleticFlag);
      tvCountry = (TextView) itemView.findViewById(R.id.tvCountry);
      tvAthleticName = (TextView) itemView.findViewById(R.id.tvAthleticName);
      tvScore = (TextView) itemView.findViewById(R.id.tvScore);
    }
  }
}
