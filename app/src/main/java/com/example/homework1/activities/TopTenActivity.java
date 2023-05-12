package com.example.homework1.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homework1.interfaces.CallBackMap;
import com.example.homework1.fragments.FragmentList;
import com.example.homework1.fragments.FragmentMap;
import com.example.homework1.models.TopTen;
import com.example.homework1.R;
import com.example.homework1.utils.SP;
import com.google.gson.Gson;

public class TopTenActivity extends AppCompatActivity {
    private FragmentMap fragmentMap;
    private TopTen topTen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten);

        topTen = null;
        String ttJson = SP.getInstance().getString(SP.KEY_TOP_TEN, "NA");
        if (!ttJson.equals("NA")) {
            topTen = new Gson().fromJson(ttJson, TopTen.class);
        }

        initViews();

    }

    private void initViews() {
        FragmentList fragmentList = new FragmentList(this, topTen);
        fragmentList.setCallBackTop(callBackTop);
        getSupportFragmentManager().beginTransaction().add(R.id.topTen_LAY_list, fragmentList).commit();

        fragmentMap = new FragmentMap(topTen);
        getSupportFragmentManager().beginTransaction().replace(R.id.topTen_LAY_map, fragmentMap).commit();
    }

    private final CallBackMap callBackTop = new CallBackMap() {
        @Override
        public void zoomToMarker(double latitude, double longitude) {
            fragmentMap.zoomToMarker(latitude, longitude);
        }
    };

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
