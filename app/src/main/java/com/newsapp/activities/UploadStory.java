package com.newsapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newsapp.AppUtilsKt;
import com.newsapp.R;
import com.newsapp.adpter.SelectedFilesAdapter;
import com.newsapp.api.ApiListeners;
import com.newsapp.constant.Constant;
import com.newsapp.dto.PopupBannerResponse;
import com.newsapp.model.Categories;
import com.newsapp.model.City;
import com.newsapp.model.PostSave;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadStory extends AppCompatActivity {

    public ApiListeners apiListeners;
    EditText edt_fullname, edt_heading, edt_inquiry;
    Button btn_submit;
    Spinner sp_city, sp_category;
    LinearLayout lin_filechoose;
    ProgressDialog pb;
    ArrayAdapter<String> ardb_city, ardb_category;
    ArrayList<String> ar_city_name, ar_city_id, ar_category_name, ar_category_id;
    RecyclerView multiple_images;
    SelectedFilesAdapter adapter_multiple_images;
    private Handler handler = null;
    private Runnable runnableCode = null;
    private ArrayList<Uri> imagesEncodedList;
    private String imageEncoded;
    private int selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_story);
        getSupportActionBar().hide();
        edt_fullname = findViewById(R.id.edt_fullname);
        edt_heading = findViewById(R.id.edt_heading);
        edt_inquiry = findViewById(R.id.edt_inquiry);
        btn_submit = findViewById(R.id.btn_submit);
        sp_city = findViewById(R.id.sp_city);
        sp_category = findViewById(R.id.sp_category);
        lin_filechoose = findViewById(R.id.lin_filechoose);
        multiple_images = findViewById(R.id.multiple_images);
        ar_city_name = new ArrayList<>();
        ar_city_id = new ArrayList<>();
        ar_category_name = new ArrayList<>();
        ar_category_id = new ArrayList<>();
        Init();
        imagesEncodedList = new ArrayList<>();

        if (Constant.get_sp(getApplicationContext(), "mobile").isEmpty()) {
            finish();
        }
        Constant.setBottomMenuSelected(UploadStory.this, 2);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = true;
                String msg = "";
//                if(edt_fullname.getText().toString().isEmpty()){
//                    isValid=true;
//                    msg="Full name must  required.";
//                    return;
//                }
                if (sp_city.getSelectedItemPosition() == 0) {
                    isValid = true;
                    msg = "Please select city.";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    sp_city.requestFocus();
                    return;
                }
                if (sp_category.getSelectedItemPosition() == 0) {
                    isValid = true;
                    msg = "Please select category.";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    sp_category.requestFocus();
                    return;
                }
                if (edt_heading.getText().toString().isEmpty()) {
                    isValid = true;
                    msg = "Heading number must  required.";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    edt_heading.requestFocus();
                    return;
                }
                if (edt_inquiry.getText().toString().isEmpty()) {
                    isValid = true;
                    msg = "Inquiry must  required.";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    edt_inquiry.requestFocus();
                    return;
                }
                if (edt_inquiry.getText().toString().isEmpty()) {
                    isValid = true;
                    msg = "Inquiry must  required.";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    edt_inquiry.requestFocus();
                    return;
                }


                if (isValid) {
                    savePost();
                }
            }
        });
        lin_filechoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickGallery();
            }
        });
        getCities();
        getCategories();
        getPopupBanner();
    }

    private void Init() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .readTimeout(100, TimeUnit.SECONDS).build();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiListeners = retrofit.create(ApiListeners.class);
        pb = new ProgressDialog(UploadStory.this);
        pb.setMessage("Please Wait...!");
        pb.setCancelable(false);
    }

    private String getRealPathFromURIForServer(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private MultipartBody.Part prepareFilePart(String partName, String fileUri) {

        File file = new File(fileUri);
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public void pickGallery() {
        ImageSelectActivity.startImageSelectionForResult(this, true, true, true, false, 1213);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            Uri uri = Uri.parse(filePath);
            imagesEncodedList.add(uri);
            adapter_multiple_images = new SelectedFilesAdapter(imagesEncodedList, UploadStory.this);
            multiple_images.setLayoutManager(new GridLayoutManager(this, 3));
            multiple_images.setItemAnimator(new DefaultItemAnimator());
            multiple_images.setAdapter(adapter_multiple_images);
        }
    }

    public void savePost() {
        pb.show();
        //creating a file
        RequestBody cityid = RequestBody.create(MediaType.parse("text/plain"), ar_city_id.get(sp_city.getSelectedItemPosition()));
        RequestBody catid = RequestBody.create(MediaType.parse("text/plain"), ar_category_id.get(sp_category.getSelectedItemPosition()));
        RequestBody heading = RequestBody.create(MediaType.parse("text/plain"), edt_heading.getText().toString());
        RequestBody inquiry = RequestBody.create(MediaType.parse("text/plain"), edt_inquiry.getText().toString());
        RequestBody keyword = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody user = RequestBody.create(MediaType.parse("text/plain"), "user");
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), Constant.get_sp(UploadStory.this, "name"));
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), Constant.get_sp(UploadStory.this, "mobile"));
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int i = 0; i < imagesEncodedList.size(); i++) {
            String path = getRealPathFromURIForServer(imagesEncodedList.get(i));
//            String finalImage = Constant.compressImage(path, UploadStory.this);
            parts.add(prepareFilePart("files[]", path)); // Note: attachment[]. Not attachment
        }
        MultipartBody.Part[] arr = new MultipartBody.Part[parts.size()];
        for (int i = 0; i < parts.size(); i++) {
            arr[i] = parts.get(i);
        }
        final Call<PostSave> getUserInfoVoCall = apiListeners.SavePost(cityid, catid, heading, inquiry, name, keyword, user, mobile, arr);
        getUserInfoVoCall.enqueue(new Callback<PostSave>() {
            @Override
            public void onResponse(Call<PostSave> call, Response<PostSave> response) {
                pb.dismiss();
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        finish();
                        Intent intent = new Intent(UploadStory.this, UploadStory.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(UploadStory.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UploadStory.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PostSave> call, Throwable t) {
                pb.dismiss();
                Toast.makeText(UploadStory.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCities() {
        pb.show();
        final Call<City> getUserInfoVoCall = apiListeners.Cities();
        getUserInfoVoCall.enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {
                pb.dismiss();
                ar_city_name.add("Select City");
                ar_city_id.add("0");
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            City.Data obj = response.body().getData().get(i);
                            ar_city_name.add(obj.getName());
                            ar_city_id.add(obj.getId());
                        }
                        ardb_city = new ArrayAdapter(UploadStory.this, R.layout.adapter_spinner_feedback, ar_city_name);
                        sp_city.setAdapter(ardb_city);
                        sp_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                TextView tv = (TextView) view;
//                                tv.setTextSize(14);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } else {
                        Toast.makeText(UploadStory.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
                pb.dismiss();
                Toast.makeText(UploadStory.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCategories() {
        pb.show();
        final Call<Categories> getUserInfoVoCall = apiListeners.Categories();
        getUserInfoVoCall.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                pb.dismiss();
                ar_category_name.add("Select Category");
                ar_category_id.add("0");
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            Categories.Data obj = response.body().getData().get(i);
                            ar_category_name.add(obj.getName());
                            ar_category_id.add(obj.getId());

                        }
                        ardb_category = new ArrayAdapter(UploadStory.this, R.layout.adapter_spinner_feedback, ar_category_name);
                        sp_category.setAdapter(ardb_category);
                        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                TextView tv = (TextView) view;
//                                tv.setTextSize(14);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } else {
                        Toast.makeText(UploadStory.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                pb.dismiss();
                Toast.makeText(UploadStory.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constant.get_sp(getApplicationContext(), "mobile").isEmpty()) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), Home.class));
    }

    private void getPopupBanner() {
        final Call<PopupBannerResponse> getUserInfoVoCall = apiListeners.popupBanner(Constant.AppFullScreenBannerAd.upload_screen);
        getUserInfoVoCall.enqueue(new Callback<PopupBannerResponse>() {
            @Override
            public void onResponse(Call<PopupBannerResponse> call, Response<PopupBannerResponse> response) {
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        if (!response.body().getPopup_banner().isEmpty()) {
                            setupRepeatableBannerAd(response.body().getDelay_time(), response.body().getInitial_time(), response.body().getPopup_banner());
                        }
                    } else {
                        System.out.println(response.body().getMsg());
                    }
                }
            }

            @Override
            public void onFailure(Call<PopupBannerResponse> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void setupRepeatableBannerAd(String delayTime, String initialDelayTime, List<PopupBannerResponse.PopupBanner> popup_banner) {
        handler = new Handler();
        runnableCode = new Runnable() {
            @Override
            public void run() {
                if (!isDestroyed() && (!(UploadStory.this).isFinishing())) {
                    if ((UploadStory.this).getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                        AppUtilsKt.showProgressDialog(UploadStory.this, popup_banner);
                    }
                    handler.postDelayed(this, Long.parseLong(delayTime) * 1000);
                }
            }
        };

        if (!isDestroyed() && (!(UploadStory.this).isFinishing())) {
            handler.postDelayed(runnableCode, Long.parseLong(initialDelayTime) * 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != handler) {
            handler.removeCallbacks(runnableCode);
        }
    }
}