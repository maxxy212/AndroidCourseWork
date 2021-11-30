package com.greenwich.mexpense.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.greenwich.mexpense.R;
import com.greenwich.mexpense.adapter.TransportAdapter;
import com.greenwich.mexpense.databinding.ActivityEditTripBinding;
import com.greenwich.mexpense.model.Transport;
import com.greenwich.mexpense.model.Trip;
import com.greenwich.mexpense.utils.DateUtil;
import com.greenwich.mexpense.utils.UI;
import com.zeeshan.material.multiselectionspinner.MultiSelectionSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.realm.Realm;

@AndroidEntryPoint
public class EditTripActivity extends AppCompatActivity implements View.OnFocusChangeListener {
    private ActivityEditTripBinding binding;
    private final List<Transport> transportList = new ArrayList<>();
    private static final String SHOW_TRIP = "show_report";
    private Realm realm;
    private Trip trip;
    private String riskReq = "";
    private Transport selectedTransport = new Transport();
    private TimePickerDialog picker;
    private final Calendar calendar = Calendar.getInstance();
    @Inject
    UI ui;
    @Inject
    DateUtil dateUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_trip);
        realm = Realm.getDefaultInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        int id = getIntent().getIntExtra(SHOW_TRIP, 0);
        trip = realm.where(Trip.class).equalTo("id", id).findFirst();
        binding.setTrip(trip);
        if (trip.req_risk.equalsIgnoreCase("yes")){
            binding.signYes.setChecked(true);
            riskReq = getString(R.string.yes);
        }else if (trip.req_risk.equalsIgnoreCase("no")){
            binding.signNo.setChecked(true);
            riskReq = getString(R.string.no);
        }
        multi_expense();
        setModeOfTransportSpinner();
        binding.update.setOnClickListener(view -> validateDetails());
        binding.reqRisk.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton rbut = findViewById(i);
            if (rbut.getText().toString().equalsIgnoreCase("Yes"))
            { riskReq = getString(R.string.yes); }
            else if (rbut.getText().toString().equalsIgnoreCase("No"))
            { riskReq = getString(R.string.no); }
        });
        setTime();
        datePickerHandler();
    }

    public static void start(Context context, int tripID){
        Intent intent = new Intent(context, EditTripActivity.class);
        intent.putExtra(SHOW_TRIP, tripID);
        context.startActivity(intent);
    }

    private void setModeOfTransportSpinner() {
        transportList.add(new Transport(R.drawable.ic_avatar, "DLR"));
        transportList.add(new Transport(R.drawable.ic_avatar, "Bus"));
        transportList.add(new Transport(R.drawable.ic_avatar, "Train"));
        transportList.add(new Transport(R.drawable.ic_avatar, "Underground"));

        int position = -1;
        for (Transport t : transportList) {
            position++;
            if (t.name.equalsIgnoreCase(trip.mode_of_transport)){
                break;
            }
        }

        TransportAdapter adapter = new TransportAdapter(this, transportList);
        binding.transport.setAdapter(adapter);
        binding.transport.setSelection(position);

        binding.transport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTransport = transportList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    private void multi_expense() {
        binding.multiSelection.setItems(getTypes());
        binding.multiSelection.setOnItemSelectedListener(new MultiSelectionSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, boolean isSelected, int position) {
                addExpenseToDB(getTypes().get(position), isSelected);
            }

            @Override
            public void onSelectionCleared() {
            }
        });

        for (int i = 0; i < (trip != null ? trip.expense.size() : 0); i++) {
            binding.multiSelection.setSelection(trip.expense.get(i));
        }
    }

    private List<String > getTypes() {
        ArrayList<String> typeList = new ArrayList<>();
        typeList.add("Travel");
        typeList.add("Food");
        typeList.add("Other");
        return typeList;
    }

    private void validateDetails() {
        if (isDetailValid()) {
            updateDetails();
        }
    }

    private void updateDetails() {
        realm.executeTransaction(realm1 -> {
            trip.name = binding.name.getText().toString();
            trip.starting_point = binding.start.getText().toString();
            trip.destination = binding.destination.getText().toString();
            trip.date_of_trip = binding.date.getText().toString();
            trip.req_risk = riskReq;
            trip.description = binding.desc.getText().toString();
            trip.mode_of_transport = selectedTransport.name;
            trip.amt_expense = binding.amount.getText().toString();
            trip.time_of_expense = binding.time.getText().toString();
            trip.comment = binding.comment.getText().toString();
        });

        ui.showOkayDialog(getString(R.string.success), getString(R.string.trip_updated), true);
    }

    private void addExpenseToDB(String expense, boolean selected) {
        realm.executeTransaction(realm1 -> {
            if (selected){
                trip.expense.add(expense);
            }else {
                trip.expense.remove(expense);
            }
        });
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
                    date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
        });
    }

    private void setTime() {
        binding.time.setOnClickListener(view -> {
            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            // time picker dialog
            picker = new TimePickerDialog(this,
                    (tp, sHour, sMinute) -> binding.time.setText(sHour + ":" + sMinute), hour, minutes, true);
            picker.show();
            picker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
            picker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
        });
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

        if (binding.amount.getText().toString().isEmpty()) {
            validate = false;
            binding.amount.setError(getString(R.string.amount_req));
        }

        if (binding.time.getText().toString().isEmpty()) {
            validate = false;
            binding.time.setError(getString(R.string.time_exp_req));
        }

        if (binding.multiSelection.getText().toString().isEmpty()) {
            validate = false;
            binding.multiSelection.setError(getString(R.string.type_exp_req));
        }

        return validate;
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
            case R.id.amount:
                if (!binding.amount.getText().toString().isEmpty()) {
                    binding.amount.setError(null);
                }
                break;
            case R.id.time:
                if (!binding.time.getText().toString().isEmpty()) {
                    binding.time.setError(null);
                }
                break;
            case R.id.multi_Selection:
                if (!binding.multiSelection.getText().toString().isEmpty()) {
                    binding.multiSelection.setError(null);
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