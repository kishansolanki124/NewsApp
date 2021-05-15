package com.newsapp.constant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.newsapp.R;
import com.newsapp.activities.Home;
import com.newsapp.activities.Profile;
import com.newsapp.activities.Search;
import com.newsapp.activities.UploadStory;
import com.newsapp.api.ApiListeners;
import com.newsapp.model.BookmarkSave;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Constant {

    //    public static final String MIAN_DOMAIN = "http://www.restrodies.com/";
    public static final String MIAN_DOMAIN = "https://colorsofgujarat.co.in/";
    public static final String BASE_URL = MIAN_DOMAIN + "newsapp/newsappapi/";
    public static final String IMAGE_BASE_URL = MIAN_DOMAIN + "newsapp/uploads/news_category_img/news_img/";
    public static final String CATEGORY_IMAGE = MIAN_DOMAIN + "newsapp/uploads/news_category_img/";
    public static final String POST = MIAN_DOMAIN + "newsapp/uploads/news_img/";
    public static final String AUTHER = MIAN_DOMAIN + "newsapp/uploads/author_img/";
    public static final String BANNER = MIAN_DOMAIN + "newsapp/uploads/banner_img/";
    public static final String LOGO = MIAN_DOMAIN + "newsapp/uploads/logo_img/logo.png";

    public static final String WS_REGISTER = BASE_URL + "save_user.php";
    public static final String WS_OTP = BASE_URL + "varify_otp.php";
    public static final String WS_NEWS = BASE_URL + "getnews.php";
    public static final String WS_CATEGORIES = BASE_URL + "getcategories.php";
    public static final String WS_Keyword = BASE_URL + "getkeyword.php";
    public static final String WS_NEWS_GALLRY = BASE_URL + "getnewsgallery.php";
    public static final String WS_NEWS_DETAIL = BASE_URL + "getnews_detail";
    public static final String WS_POPUP_BANNER = BASE_URL + "getpopup_banner";
    public static final String WS_STATIC_PAGE = BASE_URL + "getstaticpage.php";
    public static final String WS_FEEDBACK = BASE_URL + "feedback.php";
    public static final String WS_BOOKMARK = BASE_URL + "getbookmark.php";
    public static final String WS_SAVE_BOOKMARK = BASE_URL + "save_bookmark.php";
    public static final String WS_SAVE_POST = BASE_URL + "save_post.php";
    public static final String WS_CITIES = BASE_URL + "getcities.php";
    public static final String WS_SETTINGS = BASE_URL + "getsettings.php";
    public static final String WS_HOME_BANNER = BASE_URL + "gethome_banner.php";
    public static final boolean SettingAPI_CALLED = false;
    public static final String NEWS_ID = "id";
    public static int ShareDescWords = 50;
    //********** SP ***************************
    public static String Android_version = "Android_version";
    public static String Appsharemsg = "Appsharemsg";
    public static String Isfourceupdate = "Isfourceupdate";
    public static String Postsharemsg = "Postsharemsg";
    public static String Register_form = "Register_form";
    public static String Updatemsg = "Updatemsg";
    public static String Update_link = "Update_link";

    public static void save_sp(Context context, String name, String mobile) {
        SharedPreferences.Editor SPEdit = context.getSharedPreferences("App", Context.MODE_PRIVATE).edit();
        SPEdit.putString("name", name);
        SPEdit.commit();
        SPEdit.apply();
        SharedPreferences.Editor SPEdit1 = context.getSharedPreferences("App", Context.MODE_PRIVATE).edit();
        SPEdit1.putString("mobile", mobile);
        SPEdit1.commit();
        SPEdit1.apply();
    }

    public static void save_sp_genral(Context context, String key, String val) {
        SharedPreferences.Editor SPEdit = context.getSharedPreferences("App", Context.MODE_PRIVATE).edit();
        SPEdit.putString(key, val);
        SPEdit.commit();
        SPEdit.apply();
    }

    public static String get_sp(Context context, String name) {
        SharedPreferences SP = context.getSharedPreferences("App", Context.MODE_PRIVATE);
        return SP.getString(name, "");
    }

    public static void setBottomMenuSelected(final Context c, int current) {
        LinearLayout lin_home = ((Activity) c).findViewById(R.id.lin_home);
        LinearLayout lin_search = ((Activity) c).findViewById(R.id.lin_search);
        LinearLayout lin_upload = ((Activity) c).findViewById(R.id.lin_upload);
        LinearLayout lin_profile = ((Activity) c).findViewById(R.id.lin_profile);

        final ImageView img_home = ((Activity) c).findViewById(R.id.img_home);
        final ImageView img_search = ((Activity) c).findViewById(R.id.img_search);
        final ImageView img_upload = ((Activity) c).findViewById(R.id.img_upload);
        final ImageView img_profile = ((Activity) c).findViewById(R.id.img_profile);

        final TextView txt_home = ((Activity) c).findViewById(R.id.txt_home);
        final TextView txt_search = ((Activity) c).findViewById(R.id.txt_search);
        final TextView txt_upload = ((Activity) c).findViewById(R.id.txt_upload);
        final TextView txt_profile = ((Activity) c).findViewById(R.id.txt_profile);

        if (current == 0) {
            img_home.setImageResource(R.drawable.menu_icon_home_active);
            img_search.setImageResource(R.drawable.menu_icon_search);
            img_upload.setImageResource(R.drawable.menu_icon_upload);
            img_profile.setImageResource(R.drawable.menu_icon_user);

            txt_home.setTextColor(ContextCompat.getColor(c, R.color.pink));
            txt_search.setTextColor(ContextCompat.getColor(c, R.color.black));
            txt_upload.setTextColor(ContextCompat.getColor(c, R.color.black));
            txt_profile.setTextColor(ContextCompat.getColor(c, R.color.black));
        } else if (current == 1) {
            img_home.setImageResource(R.drawable.menu_icon_home);
            img_search.setImageResource(R.drawable.menu_icon_search_active);
            img_upload.setImageResource(R.drawable.menu_icon_upload);
            img_profile.setImageResource(R.drawable.menu_icon_user);

            txt_home.setTextColor(ContextCompat.getColor(c, R.color.black));
            txt_search.setTextColor(ContextCompat.getColor(c, R.color.pink));
            txt_upload.setTextColor(ContextCompat.getColor(c, R.color.black));
            txt_profile.setTextColor(ContextCompat.getColor(c, R.color.black));
        } else if (current == 2) {
            img_home.setImageResource(R.drawable.menu_icon_home);
            img_search.setImageResource(R.drawable.menu_icon_search);
            img_upload.setImageResource(R.drawable.menu_icon_upload_active);
            img_profile.setImageResource(R.drawable.menu_icon_user);

            txt_home.setTextColor(ContextCompat.getColor(c, R.color.black));
            txt_search.setTextColor(ContextCompat.getColor(c, R.color.black));
            txt_upload.setTextColor(ContextCompat.getColor(c, R.color.pink));
            txt_profile.setTextColor(ContextCompat.getColor(c, R.color.black));
        } else if (current == 3) {
            img_home.setImageResource(R.drawable.menu_icon_home);
            img_search.setImageResource(R.drawable.menu_icon_search);
            img_upload.setImageResource(R.drawable.menu_icon_upload);
            img_profile.setImageResource(R.drawable.menu_icon_user_active);

            txt_home.setTextColor(ContextCompat.getColor(c, R.color.black));
            txt_search.setTextColor(ContextCompat.getColor(c, R.color.black));
            txt_upload.setTextColor(ContextCompat.getColor(c, R.color.black));
            txt_profile.setTextColor(ContextCompat.getColor(c, R.color.pink));
        }

        lin_home.setOnClickListener(view -> {
            ((Activity) c).finish();
            c.startActivity(new Intent(c, Home.class));
        });
        lin_search.setOnClickListener(view -> {
            ((Activity) c).finish();
            c.startActivity(new Intent(c, Search.class));
        });
        lin_upload.setOnClickListener(view -> {
            ((Activity) c).finish();
            c.startActivity(new Intent(c, UploadStory.class));
        });
        lin_profile.setOnClickListener(view -> {
            ((Activity) c).finish();
            c.startActivity(new Intent(c, Profile.class));
        });
    }

    public static void saveBookmark(final Context context, final String nid, final String mobile, final String id) {
        ApiListeners apiListeners;
        final ProgressDialog pb;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiListeners = retrofit.create(ApiListeners.class);
        pb = new ProgressDialog(context);
        pb.setMessage("Please Wait...!");
        pb.setCancelable(false);
        pb.show();
        final Call<BookmarkSave> getUserInfoVoCall = apiListeners.Bookmark_Save(nid, mobile, id);
        getUserInfoVoCall.enqueue(new Callback<BookmarkSave>() {
            @Override
            public void onResponse(Call<BookmarkSave> call, Response<BookmarkSave> response) {
                pb.dismiss();
                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BookmarkSave> call, Throwable t) {
                pb.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void hideKeyboard(Context context) {
        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /*For Compress ImageSize*/
    public static String compressImage(String imageUri, Context context) {

        String filePath = getRealPathFromURI(imageUri, context);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "NewsApp");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private static String getRealPathFromURI(String contentURI, Context context) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public static void showExitDialog(final Context context) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Confirmation");
        dialog.setMessage("Are you sure, You wan to exit ?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                ((Activity) context).finishAffinity();

//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                context.startActivity(intent);
//                ((Activity)context).finish();

//                Intent intent = new Intent(context, Signup.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                        Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                intent.putExtra("EXIT", true);
//                context.startActivity(intent);
//                ((Activity)context).finish();
//                System.exit(0);
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    public static void UpdateVersion(final Context context, boolean ForcedUpdate) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Update");
        dialog.setMessage(Constant.get_sp(context, Constant.Updatemsg));
        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(get_sp(context, Update_link))));
                } catch (android.content.ActivityNotFoundException anfe) {
                    anfe.printStackTrace();
                }
            }
        });
        if (ForcedUpdate) {
            dialog.setCancelable(false);
        }
        if (!ForcedUpdate) {
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        }
        dialog.show();
    }

    public static void shareImage(final Context context, final String message, String url, Bitmap bitmap) {
//        Uri uri= Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), BitmapFactory.decodeFile(fileUri),null,null));
        // use intent to share image
        Picasso.with(context).load(url).into(new Target() {
            private Uri fileUri;

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent share = new Intent(Intent.ACTION_SEND);
                String shareBody = message;
                share.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                share.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(context, bitmap));
                share.setType("image/*");
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(Intent.createChooser(share, "Share Using"));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

    }

    public static Uri getLocalBitmapUri(Context context, Bitmap bmp) {
        Uri uri = null;
        try {
            File file = new File(context.getExternalCacheDir(), "newApp.png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            uri = Uri.fromFile(file);
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    public interface AppFullScreenBannerAd {
        long adDelayTime = 5000;
        long adBetweenTime = 25000;
        String home = "news_screen";
        String news_detail_screen = "news_detail_screen";
        String search_screen = "search_screen";
        String search_result_screen = "search_result_screen";
        String upload_screen = "upload_screen";
    }
}