package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by aark on 2016-01-25.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
  public static final String DATABASE_NAME = "DoorsOpen.db";
  public static final String TABLE_NAME = "BuildingFav";

  public static final int DATABASE_VERSION = 1;
  public static final String CREATE_GROUP = "Create table "
      + TABLE_NAME
      + " (_id integer primary key autoincrement, "
      + " buildingID integer, favTag TEXT)";
  private static DatabaseHelper databasehelper;

  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public static DatabaseHelper getInstance(Context context) {
    if (databasehelper == null) databasehelper = new DatabaseHelper(context);
    return databasehelper;
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_GROUP);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.i("TAG", "Upgrade: -->>" + oldVersion + " to" + newVersion);
    //Adding Column if the user already installed old version application

  }
}
