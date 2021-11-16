package com.greenwich.mexpense.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.greenwich.mexpense.R;
import com.greenwich.mexpense.adapter.TransportAdapter;
import com.greenwich.mexpense.databinding.ActivityAddNewBinding;
import com.greenwich.mexpense.model.Transport;
import com.greenwich.mexpense.model.Trip;
import com.greenwich.mexpense.utils.DateUtil;
import com.greenwich.mexpense.utils.UI;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.realm.Realm;

@AndroidEntryPoint
public class AddNewActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    private ActivityAddNewBinding binding;
    private final List<Transport> transportList = new ArrayList<>();
    private Realm realm;
    private String riskReq = "";
    private final Calendar calendar = Calendar.getInstance();
    private Transport selectedTransport = new Transport();
    @Inject
    UI ui;
    @Inject
    DateUtil dateUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new);
        realm = Realm.getDefaultInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        binding.reqRisk.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton rbut = findViewById(i);
            if (rbut.getText().toString().equalsIgnoreCase("Yes"))
            { riskReq = getString(R.string.yes); }
            else if (rbut.getText().toString().equalsIgnoreCase("No"))
            { riskReq = getString(R.string.no); }
        });
        setTransportSpinner();
        binding.addTrip.setOnClickListener(view -> validateDetails());
        binding.name.setOnFocusChangeListener(this);
        binding.start.setOnFocusChangeListener(this);
        binding.destination.setOnFocusChangeListener(this);
        binding.date.setOnFocusChangeListener(this);
        datePickerHandler();
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
                selectedTransport = transportList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    private void validateDetails() {
        if (isDetailValid()) {
            assembleData();
        }
    }

    private boolean isDetailValid() {
        boolean validate = true;

        if (binding.name.getText().toString().isEmpty() || binding.name.getText().toString().length() < 2) {
            validate = false;
            binding.name.setError(getString(R.string.name_req));
        }

        if (binding.start.getText().toString().isEmpty() || binding.start.getText().toString().length() < 3) {
            validate = false;
            binding.start.setError(getString(R.string.start_req));
        }

        if (binding.destination.getText().toString().isEmpty() || binding.destination.getText().toString().length() < 3) {
            validate = false;
            binding.destination.setError(getString(R.string.destination_req));
        }

        if (binding.date.getText().toString().isEmpty()) {
            validate = false;
            binding.date.setError(getString(R.string.date_trip_req));
        }

        if (riskReq.isEmpty()) {
            validate = false;
            ui.showErrorDialog(getString(R.string.error), getString(R.string.risk_ass_req));
        }

        return validate;
    }

    private void assembleData() {
        Trip trip = new Trip();
        trip.name = binding.name.getText().toString();
        trip.starting_point = binding.start.getText().toString();
        trip.destination = binding.destination.getText().toString();
        trip.date_of_trip = binding.date.getText().toString();
        trip.req_risk = riskReq;
        trip.description = binding.desc.getText().toString();
        trip.mode_of_transport = selectedTransport.name;

        DetailActivity.start(this, trip);
    }

    private void datePickerHandler() {
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            binding.date.setText(dateUtil.formatDate(calendar.getTime(), null));
            binding.date.setError(null);
        };

        binding.date.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    date, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
            datePickerDialog.show();
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.name:
                if (!binding.name.getText().toString().isEmpty()) {
                    binding.name.setError(null);
                }
                break;
            case R.id.start:
                if (!binding.start.getText().toString().isEmpty()) {
                    binding.start.setError(null);
                }
                break;
            case R.id.destination:
                if (!binding.destination.getText().toString().isEmpty()) {
                    binding.destination.setError(null);
                }
                break;
            case R.id.date:
                if (!binding.date.getText().toString().isEmpty()) {
                    binding.date.setError(null);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }
}