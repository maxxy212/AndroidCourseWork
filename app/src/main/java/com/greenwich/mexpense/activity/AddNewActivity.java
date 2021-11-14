package com.greenwich.mexpense.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.greenwich.mexpense.R;
import com.greenwich.mexpense.databinding.ActivityAddNewBinding;

public class AddNewActivity extends AppCompatActivity {

    private ActivityAddNewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new);
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
    }

    public static void start(Context context){
        context.startActivity(new Intent(context, AddNewActivity.class));
    }
}