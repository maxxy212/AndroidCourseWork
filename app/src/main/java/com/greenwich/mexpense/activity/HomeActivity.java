package com.greenwich.mexpense.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.greenwich.mexpense.R;
import com.greenwich.mexpense.adapter.TripAdapter;
import com.greenwich.mexpense.databinding.ActivityHomeBinding;
import com.greenwich.mexpense.model.Trip;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private OrderedRealmCollection<Trip> data;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        realm = Realm.getDefaultInstance();

        AppBarLayout.LayoutParams parameters = (AppBarLayout.LayoutParams)binding.collapse.getLayoutParams();
        parameters.setScrollFlags(0);
        binding.addTrip.setOnClickListener(v -> AddNewActivity.start(this));
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
}