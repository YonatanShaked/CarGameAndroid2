package com.example.homework1.models;

import java.util.ArrayList;

public class TopTen {
    public final static int MAX_IN_LIST = 10;
    private final ArrayList<Record> records = new ArrayList<>();

    public ArrayList<Record> getRecords() {
        return records;
    }
}