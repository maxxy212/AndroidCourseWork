package com.greenwich.mexpense.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.greenwich.mexpense.R;
import com.greenwich.mexpense.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        AppBarLayout.LayoutParams parameters = (AppBarLayout.LayoutParams)binding.collapse.getLayoutParams();
        parameters.setScrollFlags(0);
        binding.addTrip.setOnClickListener(v -> AddNewActivity.start(this));
    }

    public static void start(Context context){
        context.startActivity(new Intent(context, HomeActivity.class));
    }
}