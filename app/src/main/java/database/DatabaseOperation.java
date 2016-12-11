package database;

import android.content.ContentValues;

/**
 * Created by aark on 2016-01-27.
 */
public interface DatabaseOperation {
  long insert(Object object);

  int update(Object object);

  int delete(Object object);

  ContentValues getContentValues(Object object);
}
