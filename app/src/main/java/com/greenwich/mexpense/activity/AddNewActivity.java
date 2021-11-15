package com.greenwich.mexpense.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.greenwich.mexpense.R;
import com.greenwich.mexpense.adapter.TransportAdapter;
import com.greenwich.mexpense.databinding.ActivityAddNewBinding;
import com.greenwich.mexpense.model.Transport;
import com.greenwich.mexpense.model.Trip;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;


public class AddNewActivity extends AppCompatActivity {

    private ActivityAddNewBinding binding;
    private final List<Transport> transportList = new ArrayList<>();
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new);
        realm = Realm.getDefaultInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        binding.reqRisk.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbut = (RadioButton)findViewById(i);
                if (rbut.getText().toString().equalsIgnoreCase("Yes"))
                { }
                else if (rbut.getText().toString().equalsIgnoreCase("No"))
                { }
            }
        });

        setTransportSpinner();
    }

    public static void start(Context context){
        context.startActivity(new Intent(context, AddNewActivity.class));
    }

    private void setTransportSpinner() {
        transportList.add(new Transport(R.drawable.ic_avatar, "DLR"));
        transportList.add(new Transport(R.drawable.ic_avatar, "Bus"));
        transportList.add(new Transport(R.drawable.ic_avatar, "Train"));
        transportList.add(new Transport(R.drawable.ic_avatar, "Underground"));

        TransportAdapter adapter = new TransportAdapter(this, transportList);
        binding.transport.setAdapter(adapter);

        binding.transport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
            Trip trip = new Trip(); // unmanaged
            //...
            realm.insertOrUpdate(trip); // using insert API
        });
    }
}