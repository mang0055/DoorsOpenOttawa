package com.iamraviraj.mang0055.ottawa.ca;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import modal.Building;
import utils.Constant;

class SportCardsAdapter extends RecyclerView.Adapter<SportCardsAdapter.SportCardViewHolder> {
  private final List<Building> items = new ArrayList<>();
  private Context context;
  private OnItemClickListener itemClickListener;

  SportCardsAdapter(Context context) {
    this.context = context;
  }

  public boolean add(Building item) {
    boolean isAdded = items.add(item);
    if (isAdded) {
      notifyItemInserted(items.size() - 1);
    }
    return isAdded;
  }

  boolean addAll(Collection<Building> items) {
    int start = this.items.size();
    boolean isAdded = this.items.addAll(items);
    if (isAdded) {
      notifyItemRangeInserted(start, items.size());
    }
    return isAdded;
  }

  public void clear() {
    items.clear();
    notifyDataSetChanged();
  }

  @Override public SportCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
    return new SportCardViewHolder(view);
  }

  @Override public void onBindViewHolder(final SportCardViewHolder holder, int position) {
    Building item = items.get(position);
    holder.tvSportTitle.setText(item.getName());
    holder.tvSportSubtitle.setText(item.getName() + " Sub");
    holder.tvSportRound.setText(item.getBuildingId().intValue() + "");
    Picasso.with(context).load(Constant.ENDPOINT + item.getImage()).into(holder.ivSportPreview);
    //holder.tvTime.setText(item.getTime());
    //holder.tvDayPart.setText(item.getDayPart());

    //((CardView) holder.itemView).setCardBackgroundColor(ContextCompat.getColor(context, item.getBackgroundColorResId()));

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      holder.ivSportPreview.setTransitionName("shared" + String.valueOf(position));
    }

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (itemClickListener != null) {
          itemClickListener.onItemClicked(holder.getAdapterPosition(), holder.ivSportPreview);
        }
      }
    });
  }

  @Override public int getItemCount() {
    return items.size();
  }

  public OnItemClickListener getItemClickListener() {
    return itemClickListener;
  }

  void setItemClickListener(OnItemClickListener itemClickListener) {
    this.itemClickListener = itemClickListener;
  }

  Building getModelByPos(int pos) {
    return items.get(pos);
  }

  interface OnItemClickListener {
    void onItemClicked(int pos, View view);
  }

  class SportCardViewHolder extends RecyclerView.ViewHolder {

    final TextView tvSportTitle;
    final TextView tvSportSubtitle;
    final TextView tvSportRound;
    final TextView tvTime;
    final TextView tvDayPart;
    ImageView ivSportPreview;

    SportCardViewHolder(View itemView) {
      super(itemView);
      tvSportTitle = (TextView) itemView.findViewById(R.id.tvSportTitle);
      tvSportSubtitle = (TextView) itemView.findViewById(R.id.tvSportSubtitle);
      tvSportRound = (TextView) itemView.findViewById(R.id.tvSportRound);
      ivSportPreview = (ImageView) itemView.findViewById(R.id.ivSportPreview);
      tvTime = (TextView) itemView.findViewById(R.id.tvTime);
      tvDayPart = (TextView) itemView.findViewById(R.id.tvDayPart);
    }
  }
}
