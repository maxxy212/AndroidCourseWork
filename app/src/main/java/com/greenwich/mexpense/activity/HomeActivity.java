package com.greenwich.mexpense.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.greenwich.mexpense.R;
import com.greenwich.mexpense.adapter.TripAdapter;
import com.greenwich.mexpense.databinding.ActivityHomeBinding;
import com.greenwich.mexpense.model.Media;
import com.greenwich.mexpense.model.Trip;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private OrderedRealmCollection<Trip> data;
    private Realm realm;
    private String mCurrentPhotoPath;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Media media;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        realm = Realm.getDefaultInstance();
        media = realm.where(Media.class).findFirst();

        if (media != null){
            mCurrentPhotoPath = media.photo_path;
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            binding.profileImage.setImageBitmap(bitmap);
        }

        AppBarLayout.LayoutParams parameters = (AppBarLayout.LayoutParams)binding.collapse.getLayoutParams();
        parameters.setScrollFlags(0);
        binding.addTrip.setOnClickListener(v -> AddNewActivity.start(this));
        binding.profileImage.setOnClickListener(view -> takePicture());
    }

    public static void start(Context context){
        context.startActivity(new Intent(context, HomeActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        data = realm.where(Trip.class).findAll();
        if (data.isEmpty()) {
            binding.emptyText.setVisibility(View.VISIBLE);
            binding.trips.setVisibility(View.GONE);
        }else {
            binding.emptyText.setVisibility(View.GONE);
            binding.trips.setVisibility(View.VISIBLE);
        }
        setTripAdapter();
    }

    private void setTripAdapter(){
        TripAdapter adapter = new TripAdapter(data, realm);
        RecyclerView.LayoutManager _mLayoutManager = new LinearLayoutManager(this);
        binding.trips.setLayoutManager(_mLayoutManager);
        binding.trips.setItemAnimator(new DefaultItemAnimator());
        binding.trips.setAdapter(adapter);
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            Log.d("TAG", "FILE-URL:"+ (photoFile != null ? photoFile.getAbsolutePath() : null));
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.greenwich.mexpense.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* key */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG", "onActivityResult: "+requestCode);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            File photoFile = new File(mCurrentPhotoPath);
            if (photoFile.exists()){
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.greenwich.mexpense.fileprovider",
                        photoFile);
                // I can do anything I want with the image URI
                setPic();
            }
        }
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = binding.profileImage.getWidth();
        int targetH = binding.profileImage.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        binding.profileImage.setImageBitmap(bitmap);
        setImagePathToDB();
    }

    private void setImagePathToDB() {
        realm.executeTransaction(realm1 -> {
            if (media == null) {
                media = new Media();
                media.photo_path = mCurrentPhotoPath;
                realm.insert(media);
            }else {
                media.photo_path = mCurrentPhotoPath;
            }
        });
    }
}