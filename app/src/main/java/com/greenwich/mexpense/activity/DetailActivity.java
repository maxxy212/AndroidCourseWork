package com.greenwich.mexpense.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.google.gson.Gson;
import com.greenwich.mexpense.R;
import com.greenwich.mexpense.databinding.ActivityDetailBinding;
import com.greenwich.mexpense.model.Trip;
import com.greenwich.mexpense.utils.UI;

import javax.inject.Inject;

import io.realm.Realm;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private static final String REPORT_FINALE = "finale_report";
    private Trip trip;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        realm = Realm.getDefaultInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        Gson gson = new Gson();
        trip = gson.fromJson(getIntent().getStringExtra(REPORT_FINALE), Trip.class);
        binding.setTrip(trip);
        binding.addTrip.setOnClickListener(view -> addTrip());
    }

    public static void start(Context context, Trip trip){
        Gson gson = new Gson();
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(REPORT_FINALE, gson.toJson(trip));
        context.startActivity(intent);
    }

    private void addTrip() {
        realm.executeTransaction(realm1 -> {
            // increment index
            Number currentIdNum = realm.where(Trip.class).max("id");
            int nextId;
            if(currentIdNum == null) {
                nextId = 1;
            } else {
                nextId = currentIdNum.intValue() + 1;
            }
            trip.id = nextId;
            realm.insertOrUpdate(trip);
        });

        new iOSDialogBuilder(this)
                .setTitle(getString(R.string.success))
                .setSubtitle(getString(R.string.trip_successful))
                .setBoldPositiveLabel(false)
                .setCancelable(false)
                .setPositiveListener(getString(R.string.ok), dialog -> {
                    dialog.dismiss();
                    HomeActivity.start(this);
                    finishAffinity();
                })
                .build()
                .show();
    }
}