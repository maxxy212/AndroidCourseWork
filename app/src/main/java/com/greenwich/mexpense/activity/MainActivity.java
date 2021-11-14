package com.greenwich.mexpense.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.greenwich.mexpense.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HomeActivity.start(this);
        finish();
    }
}