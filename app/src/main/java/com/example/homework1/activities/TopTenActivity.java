package com.example.homework1.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homework1.R;
import com.example.homework1.fragments.FragmentMap;
import com.example.homework1.fragments.FragmentTrainer;
import com.example.homework1.models.Trainer;

public class TopTenActivity extends AppCompatActivity {
    private Trainer trainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);

        trainer = new Trainer();
        trainer.setName("Moshe");
        trainer.setAfekaMons(3);

        initViews();

    }

    private void initViews() {
        FragmentTrainer fragmentTrainer = new FragmentTrainer(trainer);
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
