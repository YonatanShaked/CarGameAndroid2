package com.example.homework1.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homework1.R;
import com.example.homework1.fragments.FragmentMap;
import com.example.homework1.fragments.FragmentTrainer;

public class TopTenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);
        initViews();
    }

    private void initViews() {
        FragmentTrainer fragmentTrainer = new FragmentTrainer(getIntent().getExtras().getString("trainer"));
        getSupportFragmentManager().beginTransaction().add(R.id.topTen_LAY_list, fragmentTrainer).commit();

        FragmentMap fragmentMap = new FragmentMap();
        getSupportFragmentManager().beginTransaction().replace(R.id.topTen_LAY_map, fragmentMap).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
