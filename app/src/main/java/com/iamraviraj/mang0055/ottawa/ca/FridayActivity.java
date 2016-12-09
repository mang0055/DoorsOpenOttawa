package com.iamraviraj.mang0055.ottawa.ca;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by aark on 2016-12-08.
 */

public class FridayActivity extends AppCompatActivity {

  MainFragment mainFragment;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_friday);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.root, mainFragment = MainFragment.newInstance())
          .commit();
    } else {
      Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.root);
      if (fragment instanceof MainFragment) {
        mainFragment = (MainFragment) fragment;
      }
    }
  }

  @Override public void onBackPressed() {
    if (mainFragment == null || !mainFragment.isAdded() || !mainFragment.deselectIfSelected()) {
      super.onBackPressed();
    }
  }

  @Override public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    super.onSaveInstanceState(outState, outPersistentState);
  }

  @Override
  public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
    super.onRestoreInstanceState(savedInstanceState, persistentState);
  }
}
