package com.iamraviraj.mang0055.ottawa.ca;

import android.content.ContentValues;
import android.content.Intent;
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
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import modal.Building;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit.ApiService;
import retrofit.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.Constant;
import utils.FileUtils;

/**
 * Created by aark on 2016-11-25.
 */

public class NewBuildingActivity extends BaseActivity
    implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
  ImageView buildingImage, buildingImageOverlay;
  Button btnCancel, btnSave, btnUpdate;
  static final int REQUEST_IMAGE_CAPTURE = 0;
  static final int REQUEST_IMAGE_GET = 1;
  TextInputLayout editBuildingName, editBuildingAddress, editBuildingDescription;
  Uri imageUri = null;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_building);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setUpView();

    if (getIntent().getExtras() != null) {
      Building editableBuilding =
          new Gson().fromJson(getIntent().getExtras().getString("Building"), Building.class);
      fillViewsData(editableBuilding);
      btnSave.setVisibility(View.GONE);
      btnUpdate.setVisibility(View.VISIBLE);
      btnUpdate.setTag(editableBuilding);
    } else {
      btnSave.setVisibility(View.VISIBLE);
      btnUpdate.setVisibility(View.GONE);
    }
  }

  private void setUpView() {
    buildingImage = (ImageView) findViewById(R.id.img_buildingImage);
    buildingImageOverlay = (ImageView) findViewById(R.id.img_buildingImageOverlay);
    editBuildingName = (TextInputLayout) findViewById(R.id.editBuildingName);
    editBuildingAddress = (TextInputLayout) findViewById(R.id.editBuildingAddress);
    editBuildingDescription = (TextInputLayout) findViewById(R.id.editBuildingDescription);
    btnCancel = (Button) findViewById(R.id.btnCancel);
    btnSave = (Button) findViewById(R.id.btnSave);
    btnUpdate = (Button) findViewById(R.id.btnUpdate);
    btnCancel.setOnClickListener(this);
    btnSave.setOnClickListener(this);
    btnUpdate.setOnClickListener(this);
    buildingImageOverlay.setOnClickListener(this);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle presses on the action bar items
    switch (item.getItemId()) {

      case android.R.id.home:
        finish();
        break;
    }
    return true;
  }

  private void fillViewsData(Building editableBuilding) {
    Picasso.with(getApplicationContext())
        .load(Constant.ENDPOINT + editableBuilding.getImage())
        .into(buildingImage);
    editBuildingName.getEditText().setText(editableBuilding.getName());
    editBuildingAddress.getEditText().setText(editableBuilding.getAddress());
    editBuildingDescription.getEditText().setText(editableBuilding.getDescription());
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
      case R.id.btnUpdate:
        Building mBuilding = (Building) btnUpdate.getTag();
        //updateBuilding(mBuilding);
        if (imageUri != null) uploadBuildingPicture(mBuilding.getBuildingId(), imageUri);
        break;
    }
  }

  private void postBuilding() {
    String name = editBuildingName.getEditText().getText().toString();
    String address = editBuildingAddress.getEditText().getText().toString();
    String description = editBuildingDescription.getEditText().getText().toString();
    Building mBuilding = new Building();
    mBuilding.setName(name);
    mBuilding.setAddress(address);
    mBuilding.setImage("image/abc.jpg");
    mBuilding.setDescription(description);
    if (!isNetworkAvailable()) {
      return;
    }
    Call<ResponseBody> callPostBuilding =
        RestClient.getInstance().getApiService().postBuilding(mBuilding);
    callPostBuilding.enqueue(new Callback<ResponseBody>() {
      @Override public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        Log.e("TAG", response.message());
      }

      @Override public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.e("TAG", "Fail: ", t);
      }
    });
  }

  private void updateBuilding(Building mBuilding) {
    String name = editBuildingName.getEditText().getText().toString();
    String address = editBuildingAddress.getEditText().getText().toString();
    String description = editBuildingDescription.getEditText().getText().toString();
    if (!name.equals(mBuilding.getName())) {
      mBuilding.setName(name);
    }
    if (!address.equals(mBuilding.getAddress())) {
      mBuilding.setAddress(address);
    }
    if (!description.equals(mBuilding.getDescription())) {
      mBuilding.setDescription(description);
    }
    if (!isNetworkAvailable()) {
      return;
    }
    Call<ResponseBody> callPostBuilding = RestClient.getInstance()
        .getApiService()
        .updateBuilding(mBuilding.getBuildingId(), mBuilding);
    callPostBuilding.enqueue(new Callback<ResponseBody>() {
      @Override public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        Log.e("TAG", "Update: " + response.message());
      }

      @Override public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.e("TAG", "Update Fail: ", t);
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
    imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        new ContentValues());
    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
      startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
      //Bundle extras = data.getExtras();
      //Bitmap imageBitmap = (Bitmap) extras.get("data");
      //buildingImage.setImageBitmap(imageBitmap);
      buildingImage.setImageURI(imageUri);
    }
    if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
      Uri fullPhotoUri = data.getData();
      buildingImage.setImageURI(fullPhotoUri);
      imageUri = fullPhotoUri;
    }
  }

  private void uploadBuildingPicture(int buildingId, Uri uri) {
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    httpClient.addInterceptor(logging);
    httpClient.addInterceptor(headers);
    Retrofit retrofit = new Retrofit.Builder().baseUrl(Constant.http_live_url)
        .addConverterFactory(GsonConverterFactory.create()).client(httpClient.build())
        .build();
    ApiService api = retrofit.create(ApiService.class);
    Log.i("TAG", "Uploading Building Picture");
    Call<ResponseBody> callPostBuilding =
        api.uploadBuildingPic(buildingId, prepareFilePart("image", uri));
    callPostBuilding.enqueue(new Callback<ResponseBody>() {
      @Override public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        Log.e("TAG", "Uploaded: " + response.message());
      }

      @Override public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.e("TAG", "Uploaded Fail: ", t);
      }
    });
  }

  private Interceptor headers = new Interceptor() {
    @Override public okhttp3.Response intercept(Chain chain) throws IOException {
      Request original = chain.request();

      Request request = original.newBuilder()
          //.header("Content-Type", "application/octet-stream")
          .header("Authorization", BaseActivity.getAPIAuthorisation())
          .method(original.method(), original.body())
          .build();
      return chain.proceed(request);
    }
  };

  private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
    String MULTIPART_FORM_DATA = "multipart/form-data";
    // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
    // use the FileUtils to get the actual file by uri
    File file = FileUtils.getFile(this, fileUri);

    // create RequestBody instance from file
    RequestBody requestFile = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);

    // MultipartBody.Part is used to send also the actual file name
    return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
  }
}
