package com.example.homework1.models;

public class Record {
    private int distance = 0;
    private long date = 0;
    private int score = 0;
    private CarPosition carPosition;

    public int getDistance() {
        return distance;
    }

    public Record setDistance(int distance) {
        this.distance = distance;
        return this;
    }

    public long getDate() {
        return date;
    }

    public Record setDate(long date) {
        this.date = date;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Record setScore(int score) {
        this.score = score;
        return this;
    }

    public CarPosition getCarPosition() {
        return carPosition;
    }

    public Record setCarPosition(CarPosition carPosition) {
        this.carPosition = carPosition;
        return this;
    }
}