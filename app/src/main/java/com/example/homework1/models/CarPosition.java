package com.example.homework1.models;

public class CarPosition {
    private final double latitude;
    private final double longitude;

    public CarPosition(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}