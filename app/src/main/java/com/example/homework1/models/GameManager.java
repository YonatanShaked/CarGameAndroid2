package com.example.homework1.models;

import com.example.homework1.interfaces.Constants;
import com.example.homework1.R;
import com.example.homework1.utils.MusicPlayer;

import java.util.ArrayList;

public class GameManager implements Constants {
    private int numberOfHearts;
    private final boolean[] roadsArr;
    private int currentPos;
    private int distance;
    private int coin;
    private int randomCoin;
    private int currentTopPos;
    private TopTen topTen;
    private final CarPosition carPosition;
    private float currentZ;
    private final MusicPlayer mp;
    private boolean flag;

    public GameManager(TopTen topTen, CarPosition carPosition, float currentZ, MusicPlayer mp) {
        this.numberOfHearts = NUMBER_OF_HEARTS;
        this.currentPos = THIRD_ROAD;
        this.roadsArr = new boolean[NUMBER_OF_ROADS];
        this.distance = 0;
        this.coin = 0;
        this.randomCoin = 0;
        this.currentTopPos = -1;
        this.mp = mp;
        this.currentZ = currentZ;
        this.carPosition = carPosition;
        this.topTen = topTen;
        this.flag = true;
        if (this.topTen == null) {
            this.topTen = new TopTen();
        }
        randomSignOnRoads();
    }

    public int getNumberOfHearts() {
        return numberOfHearts;
    }

    public TopTen getTopTen() {
        return topTen;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getCurrentTopPos() {
        return currentTopPos;
    }

    public void setCurrentTopPos(int currentTopPos) {
        this.currentTopPos = currentTopPos;
    }

    public boolean[] getRoadsArr() {
        return roadsArr;
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public float getCurrentZ() {
        return currentZ;
    }

    public void setCurrentZ(float currentZ) {
        this.currentZ = currentZ;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }

    public int getCoin() {
        return coin;
    }

    public int getRandomCoin() {
        return randomCoin;
    }

    public void addToTopTen() {
        setFlag(true);

        if (topTen.getRecords().isEmpty()) { // TopTen list is empty
            addRecordToTopTen(0);
            setCurrentTopPos(1);
        } else { // TopTen list is not empty
            ArrayList<Record> records = topTen.getRecords();
            int gameDistance = getDistance();
            int i = 0;
            if (gameDistance > records.get(records.size() - 1).getDistance() || records.size() < TopTen.MAX_IN_LIST) {
                do {
                    int currentDistance = records.get(i).getDistance();
                    if (gameDistance > currentDistance && i < TopTen.MAX_IN_LIST) {
                        if (records.size() == TopTen.MAX_IN_LIST) { // if list if full remove last
                            records.remove(records.size() - 1);
                        }
                        addRecordToTopTen(i);
                        setCurrentTopPos(i + 1);
                        setFlag(false);
                    }
                    if (i == records.size() - 1 && i < TopTen.MAX_IN_LIST) { // winner distance is the lowest, and there is room in the list
                        addRecordToTopTen(i + 1);
                        setCurrentTopPos(i + 2);
                        setFlag(false);
                    }
                    i++;
                } while (i < records.size() && isFlag());
            }
        }
    }

    private void addRecordToTopTen(int index) {
        Record record = new Record()
                .setDate(System.currentTimeMillis())
                .setDistance(getDistance())
                .setScore(getCoin())
                .setCarPosition(carPosition);

        topTen.getRecords().add(index, record);
    }

    public void randomSignOnRoads() {
        int randomSign = isGameNotOver() ? (int) (Math.random() * NUMBER_OF_ROADS) : ERROR;
        randomCoin = (int) (Math.random() * NUMBER_OF_ROADS);

        while (randomSign == randomCoin)
            randomCoin = (int) (Math.random() * NUMBER_OF_ROADS);

        for (int i = 0; i < roadsArr.length; i++)
            roadsArr[i] = randomSign != i;
    }

    public boolean shiftCarLeft() {
        if (isGameNotOver() && this.currentPos > FIRST_ROAD) {
            this.currentPos--;
            return true;
        }
        return false;
    }

    public boolean shiftCarRight() {
        if (isGameNotOver() && this.currentPos < FIFTH_ROAD) {
            this.currentPos++;
            return true;
        }
        return false;
    }

    public boolean isAllFalse() {
        for (boolean b : this.roadsArr) {
            if (b)
                return false;
        }
        return true;
    }

    public boolean checkCrash(int roadIndex) {
        this.roadsArr[roadIndex] = false;
        if (currentPos == randomCoin && currentPos == roadIndex) {
            this.coin++;
            mp.playSound(R.raw.coin_pick);
            return false;
        }
        if (roadIndex == currentPos) {
            numberOfHearts--;
            mp.playSound(R.raw.car_crash);
            return true;
        }
        return false;
    }

    public boolean isGameNotOver() {
        return numberOfHearts != 0;
    }
}
