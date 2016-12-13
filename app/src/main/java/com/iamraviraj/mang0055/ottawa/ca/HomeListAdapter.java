package com.iamraviraj.mang0055.ottawa.ca;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import database.Fav;
import database.FavImpl;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import modal.Building;
import utils.Constant;

import static com.iamraviraj.mang0055.ottawa.ca.MainActivity.KEY_BUILDING;

/**
 * @author Raviraj Mangukiya (mang0055@algonquinlive.com)
 */

public class HomeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements Filterable, View.OnClickListener {

  Context context;
  List<Building> data;
  List<Building> temporarydata;
  LayoutInflater inflater;
  FavImpl favImpl;

  public HomeListAdapter(Context applicationContext, List<Building> buildings) {
    this.context = applicationContext;
    this.data = buildings;
    this.temporarydata = buildings;
    this.favImpl = new FavImpl(context);
    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_main, parent, false);
    return new MyViewHolder(v);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
    final MyViewHolder holder = (MyViewHolder) viewHolder;
    Building building = data.get(position);
    holder.info_text.setText(building.getName());
    holder.btn_favorite.setTag(building.getBuildingId());
    holder.buildingItem.setOnClickListener(this);
    Picasso.with(context).load(Constant.ENDPOINT + building.getImage()).into(holder.info_image);
    holder.buildingItem.setTag(building);

    Picasso.with(context).load(Constant.ENDPOINT + building.getImage()).into(new Target() {
      @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        holder.imageLoading.setVisibility(View.INVISIBLE);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
          @Override public void onGenerated(Palette palette) {
            Palette.Swatch textSwatch = palette.getVibrantSwatch();
            if (textSwatch != null) holder.imageLoading.setBackgroundColor(textSwatch.getRgb());
          }
        });
      }

      @Override public void onBitmapFailed(Drawable errorDrawable) {

      }

      @Override public void onPrepareLoad(Drawable placeHolderDrawable) {

      }
    });
    Fav fav = favImpl.getFav(building.getBuildingId() + "");
    if (fav != null) {
      holder.btn_favorite.setFavorite(fav.isFavTag());
    }
    holder.btn_favorite.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        int bId = data.get(position).getBuildingId();
        Log.e("TAG", "BuildingID->> " + bId);
        Fav fav = favImpl.getFav(bId + "");
        if (fav != null) {
          fav.setBuildingID(bId);
          if (fav.isFavTag()) {
            fav.setFavTag(false);
            favImpl.update(fav);
            holder.btn_favorite.setFavorite(false);
          } else {
            fav.setFavTag(true);
            int a = favImpl.update(fav);
            if (a == 0) {
              favImpl.insert(fav);
            }
            holder.btn_favorite.setFavorite(true);
          }
          Log.e("TAG", new Gson().toJson(fav));
        } else {
          fav = new Fav(0, bId, true);
          favImpl.insert(fav);
          holder.btn_favorite.setFavorite(true);
          Log.e("TAG", "N " + new Gson().toJson(fav));
        }
      }
    });
  }

  @Override public int getItemCount() {
    return data.size();
  }

  public Building getItem(int position) {
    return data.get(position);
  }

  public List<Building> getData() {
    return data;
  }

  @Override public Filter getFilter() {
    Filter filter = new Filter() {

      @Override protected void publishResults(CharSequence constraint, FilterResults results) {
        if (results.count == 0) {
          notifyDataSetChanged();
        } else {
          data = (List<Building>) results.values;
          notifyDataSetChanged();
        }
      }

      @Override protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults result = new FilterResults();
        List<Building> allJournals = temporarydata;
        if (constraint == null || constraint.length() == 0) {
          result.values = allJournals;
          result.count = allJournals.size();
        } else {
          ArrayList<Building> filteredList = new ArrayList<Building>();
          for (Building j : allJournals) {
            if (j.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
              filteredList.add(j);
            }
          }
          result.values = filteredList;
          result.count = filteredList.size();
        }

        return result;
      }
    };
    return filter;
  }

  private Building getBuildingFromPosition(View v) {
    return (Building) v.getTag();
  }

  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.buildingItem:
        Intent intentEventDetail = new Intent(context, EventDetailActivity.class);
        intentEventDetail.putExtra(KEY_BUILDING, new Gson().toJson(getBuildingFromPosition(view)));
        intentEventDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentEventDetail);
        break;
    }
  }

  enum BuildingComparator implements Comparator<Building> {
    ID_SORT {
      public int compare(Building o1, Building o2) {
        return Integer.valueOf(o1.getBuildingId()).compareTo(o2.getBuildingId());
      }
    },
    NAME_SORT {
      public int compare(Building o1, Building o2) {
        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
      }
    };

    public static Comparator<Building> acending(final Comparator<Building> other) {
      return new Comparator<Building>() {
        public int compare(Building o1, Building o2) {
          return other.compare(o1, o2);
        }
      };
    }

    public static Comparator<Building> decending(final Comparator<Building> other) {
      return new Comparator<Building>() {
        public int compare(Building o1, Building o2) {
          return -1 * other.compare(o1, o2);
        }
      };
    }

    public static Comparator<Building> getComparator(final BuildingComparator... multipleOptions) {
      return new Comparator<Building>() {
        public int compare(Building o1, Building o2) {
          for (BuildingComparator option : multipleOptions) {
            int result = option.compare(o1, o2);
            if (result != 0) {
              return result;
            }
          }
          return 0;
        }
      };
    }
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView info_image;
    TextView info_text;
    MaterialFavoriteButton btn_favorite;
    CardView buildingItem;
    ProgressBar imageLoading;

    public MyViewHolder(View view) {
      super(view);
      buildingItem = (CardView) view.findViewById(R.id.buildingItem);
      info_image = (ImageView) view.findViewById(R.id.info_image);
      info_text = (TextView) view.findViewById(R.id.info_text);
      btn_favorite = (MaterialFavoriteButton) view.findViewById(R.id.btn_favorite);
      imageLoading = (ProgressBar) view.findViewById(R.id.imageLoading);
    }
  }
}
