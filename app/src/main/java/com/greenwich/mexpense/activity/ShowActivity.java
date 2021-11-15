package com.greenwich.mexpense.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.greenwich.mexpense.R;
import com.greenwich.mexpense.databinding.ActivityShowBinding;
import com.greenwich.mexpense.model.Trip;

import io.realm.Realm;

public class ShowActivity extends AppCompatActivity {

    private ActivityShowBinding binding;
    private static final String SHOW = "show_report";
    private Realm realm;
    private Trip trip;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show);
        realm = Realm.getDefaultInstance();
        int id = getIntent().getIntExtra(SHOW, 0);
        trip = realm.where(Trip.class).equalTo("id", id).findFirst();
        for (int i = 0; i < (trip != null ? trip.expense.size() : 0); i++) {
            String originalText = binding.expenseType.getText().toString();
            binding.expenseType.setText(originalText + " " + trip.expense.get(i));
        }
        binding.setTrip(trip);
    }

    public static void start(Context context, int tripID){
        Intent intent = new Intent(context, ShowActivity.class);
        intent.putExtra(SHOW, tripID);
        context.startActivity(intent);
    }
}