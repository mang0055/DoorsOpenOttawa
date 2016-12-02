package com.iamraviraj.mang0055.ottawa.ca;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import modal.Building;
import utils.Constant;

/**
 * @author Raviraj Mangukiya (mang0055@algonquinlive.com)
 */

public class HomeListAdapter extends BaseAdapter implements Filterable {

  Context context;
  List<Building> data;
  LayoutInflater inflater;
  private List<Building> temporarydata;

  public HomeListAdapter(Context applicationContext, List<Building> buildings) {
    this.context = applicationContext;
    this.data = buildings;
    this.temporarydata = buildings;
    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override public int getCount() {
    return data.size();
  }

  @Override public Building getItem(int position) {
    return data.get(position);
  }

  public List<Building> getData() {
    return data;
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    View view = convertView;
    if (convertView == null) view = inflater.inflate(R.layout.row_main, null);

    ImageView info_image = (ImageView) view.findViewById(R.id.info_image);
    TextView info_text = (TextView) view.findViewById(R.id.info_text);
    info_text.setText(data.get(position).getName());

    //Picasso ImageLoader popular library for Android
    //Learn more about Picasso http://square.github.io/picasso/

    Picasso.with(context).load(Constant.ENDPOINT + data.get(position).getImage()).into(info_image);

    return view;
  }

  @Override public Filter getFilter() {
    Filter filter = new Filter() {

      @Override protected void publishResults(CharSequence constraint, FilterResults results) {
        if (results.count == 0) {
          notifyDataSetInvalidated();
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
            if (j.getName().contains(constraint)) filteredList.add(j);
          }
          result.values = filteredList;
          result.count = filteredList.size();
        }

        return result;
      }
    };
    return filter;
  }

  enum BuildingComparator implements Comparator<Building> {
    ID_SORT {
      public int compare(Building o1, Building o2) {
        return Integer.valueOf(o1.getBuildingId()).compareTo(o2.getBuildingId());
      }
    },
    NAME_SORT {
      public int compare(Building o1, Building o2) {
        return o1.getName().compareTo(o2.getName());
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
}
