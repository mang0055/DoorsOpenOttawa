package com.iamraviraj.mang0055.ottawa.ca;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import modal.Building;
import okhttp3.ResponseBody;
import retrofit.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by aark on 2016-11-25.
 */

public class NewBuildingActivity extends BaseActivity
    implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
  ImageView buildingImage, buildingImageOverlay;
  Button btnCancel, btnSave;
  static final int REQUEST_IMAGE_CAPTURE = 0;
  static final int REQUEST_IMAGE_GET = 1;
  TextInputLayout editBuildingName, editBuildingAddress, editBuildingDescription;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_building);

    setUpView();
  }

  private void setUpView() {
    buildingImage = (ImageView) findViewById(R.id.img_buildingImage);
    buildingImageOverlay = (ImageView) findViewById(R.id.img_buildingImageOverlay);
    editBuildingName = (TextInputLayout) findViewById(R.id.editBuildingName);
    editBuildingAddress = (TextInputLayout) findViewById(R.id.editBuildingAddress);
    editBuildingDescription = (TextInputLayout) findViewById(R.id.editBuildingDescription);
    btnCancel = (Button) findViewById(R.id.btnCancel);
    btnSave = (Button) findViewById(R.id.btnSave);
    btnCancel.setOnClickListener(this);
    btnSave.setOnClickListener(this);
    buildingImageOverlay.setOnClickListener(this);
  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.img_buildingImageOverlay:
        //Add Image Button
        showChooser();
        break;
      case R.id.btnSave:
        postBuilding();
        break;
      case R.id.btnCancel:
        finish();
        break;
    }
  }

  private void postBuilding() {
    String name = editBuildingName.getEditText().getText().toString();
    String address = editBuildingAddress.getEditText().getText().toString();
    String description = editBuildingDescription.getEditText().getText().toString();
    Building mBulding = new Building();
    mBulding.setName(name);
    mBulding.setAddress(address);
    mBulding.setDescription(description);

    Call<ResponseBody> callPostBuilding =
        RestClient.getInstance().getApiService().postBuilding(getAPIAuthorisation(), mBulding);
    callPostBuilding.enqueue(new Callback<ResponseBody>() {
      @Override public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        Log.e("TAG", response.message());
      }

      @Override public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.e("TAG", "Fail: ", t);
      }
    });
  }

  private void showChooser() {
    PopupMenu popup = new PopupMenu(this, buildingImageOverlay);
    MenuInflater inflater = popup.getMenuInflater();
    popup.setOnMenuItemClickListener(this);
    inflater.inflate(R.menu.menu_capture_picture, popup.getMenu());
    popup.show();
  }

  @Override public boolean onMenuItemClick(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.item_camera:
        capturePicRequest();
        return true;
      case R.id.item_gallary:
        choosePicRequest();
        return true;
      default:
        return false;
    }
  }

  private void choosePicRequest() {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("image/*");
    if (intent.resolveActivity(getPackageManager()) != null) {
      startActivityForResult(intent, REQUEST_IMAGE_GET);
    }
  }

  private void capturePicRequest() {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
      startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
      Bundle extras = data.getExtras();
      Bitmap imageBitmap = (Bitmap) extras.get("data");
      buildingImage.setImageBitmap(imageBitmap);
    }
    if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
      Uri fullPhotoUri = data.getData();
      buildingImage.setImageURI(fullPhotoUri);
    }
  }
}
