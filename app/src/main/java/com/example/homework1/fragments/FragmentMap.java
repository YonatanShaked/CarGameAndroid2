package com.example.homework1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.homework1.models.Record;
import com.example.homework1.models.TopTen;
import com.example.homework1.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class FragmentMap extends Fragment {
    private GoogleMap mMap;
    private final TopTen topTen;

    public FragmentMap(TopTen topTen) {
        this.topTen = topTen;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        // Init map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        // Async map
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            if (topTen != null) {
                ArrayList<Record> records = topTen.getRecords();
                for (int i = 0; i < records.size(); i++) {
                    Record record = records.get(i);
                    LatLng latLng = new LatLng(record.getCarPosition().getLatitude(), record.getCarPosition().getLongitude());
                    addMarker(latLng);
                }
            }
        });
        return view;
    }

    public void addMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        mMap.addMarker(markerOptions);
    }

    public void zoomToMarker(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
    }
}