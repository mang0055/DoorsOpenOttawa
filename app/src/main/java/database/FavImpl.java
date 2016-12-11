package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aark on 2016-12-09.
 */

public class FavImpl implements DatabaseOperation {
  SQLiteDatabase database;
  String[] columnArray = { "_id, buildingID, favTag " };

  public FavImpl(Context context) {
    DatabaseHelper databasehelper = DatabaseHelper.getInstance(context);
    database = databasehelper.getWritableDatabase();
  }

  public long insert(Object object) {
    Fav items = (Fav) object;
    long result = database.insert(DatabaseHelper.TABLE_NAME, null, this.getContentValues(object));
    Log.v("TAG", "Insert Result - " + result);
    return result;
  }

  public int update(Object object) {
    Fav items = (Fav) object;
    String[] whereArgs = { String.valueOf(items.get_id()) };
    return update(object, "_id = ?", whereArgs);
  }

  public int update(Object object, String whereClause, String[] whereArgs) {
    Fav items = (Fav) object;
    int result =
        database.update(DatabaseHelper.TABLE_NAME, this.getContentValues(object), whereClause,
            whereArgs);
    Log.v("TAG", "Update Result - " + result);

    return result;
  }

  public int delete(Object object) {
    Fav items = (Fav) object;
    String[] whereArgs = { String.valueOf(items.get_id()) };
    int result = database.delete(DatabaseHelper.TABLE_NAME, "_id = ?", whereArgs);
    Log.v("TAG", "Delete Result - " + result);
    return result;
  }

  public ContentValues getContentValues(Object object) {
    Fav items = (Fav) object;
    ContentValues contentValues = new ContentValues();
    contentValues.put("buildingID", items.getBuildingID());
    contentValues.put("favTag", String.valueOf(items.isFavTag()));
    return contentValues;
  }

  public List getFavById(int buildingID) {
    List<Fav> itemsList = new ArrayList<Fav>();
    Cursor cursor =
        database.query(DatabaseHelper.TABLE_NAME, columnArray, "buildingID=" + buildingID, null,
            null, null, null);
    if (cursor.getCount() > 0) {
      cursor.moveToFirst();
      do {
        Fav items = new Fav();
        items.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
        items.setBuildingID(cursor.getInt(cursor.getColumnIndex("buildingID")));
        items.setFavTag(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("favTag"))));

        itemsList.add(items);
      } while (cursor.moveToNext());
      cursor.close();
    } else {
      Log.v("TAG", "getAll - FAV Not value found");
    }
    return itemsList;
  }

  @SuppressWarnings("rawtypes") public List getAll() {
    List<Fav> itemsList = new ArrayList<Fav>();
    Cursor cursor =
        database.query(DatabaseHelper.TABLE_NAME, columnArray, null, null, null, null, null);
    if (cursor.getCount() > 0) {
      cursor.moveToFirst();
      do {
        Fav items = new Fav();
        items.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
        items.setBuildingID(cursor.getInt(cursor.getColumnIndex("buildingID")));
        items.setFavTag(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("favTag"))));
        itemsList.add(items);
      } while (cursor.moveToNext());
      cursor.close();
    } else {
      Log.v("TAG", "getAll - FAV Not value found");
    }
    return itemsList;
  }

  public Fav getFav(String id) {
    Fav items = new Fav();
    Cursor cursor =
        database.query(DatabaseHelper.TABLE_NAME, columnArray, "buildingID=" + id, null, null, null,
            null);
    if (cursor.getCount() > 0) {
      cursor.moveToFirst();
      items.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
      items.setBuildingID(cursor.getInt(cursor.getColumnIndex("buildingID")));
      items.setFavTag(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("favTag"))));
      cursor.close();
    } else {
      Log.v("TAG", "getAll - Fav Not value found");
    }
    return items;
  }
}
