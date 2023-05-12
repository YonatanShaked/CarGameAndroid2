package com.example.homework1.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homework1.interfaces.Constants;
import com.example.homework1.models.GameManager;
import com.example.homework1.models.CarPosition;
import com.example.homework1.models.TopTen;
import com.example.homework1.R;
import com.example.homework1.utils.MusicPlayer;
import com.example.homework1.utils.SP;
import com.google.gson.Gson;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements Constants {
    private ImageButton main_BTN_right;
    private ImageButton main_BTN_left;

    private ImageView[] main_IMG_car_arr;
    private ImageView[] main_IMG_heart_arr;
    private ImageView[] main_IMG_sign_arr;

    private TextView main_Text_distance_counter;
    private TextView main_Text_count_coins;


    private LinearLayout main_LAY_car;

    private GameManager gameManager;

    private ScheduledFuture<?> scheduledFuture;

    private final Handler timerHandler = new Handler();

    private MusicPlayer mp;
    private SP sp;
    private Gson gson;
    private float startPosZ;

    private long startTime = 0;


    private SensorManager sensorManager;
    private Sensor sensor;
    String sensorType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gson = new Gson();
        sp = SP.getInstance();
        mp = new MusicPlayer(this);
        TopTen topTen = null;
        String ttJson = SP.getInstance().getString(SP.KEY_TOP_TEN, "NA");
        if (!ttJson.equals("NA")) {
            topTen = new Gson().fromJson(ttJson, TopTen.class);
        }
        String locationJsonFromMainMenuActivity = getIntent().getBundleExtra(getString(R.string.bundle)).getString(EXTRA_KEY_GAME);
        CarPosition myPosition = new Gson().fromJson(locationJsonFromMainMenuActivity, CarPosition.class);
        sensorType = getIntent().getBundleExtra("bundle").getString(SENSOR_TYPE);

        if (!sensorType.equals("LIGHT")) {
            initSensor();
        }
        findViews();
        initViews();
        gameManager = new GameManager(topTen, myPosition, startPosZ, mp);
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(distanceCounter, 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        start();
    }


    @Override
    protected void onStop() {
        super.onStop();
        stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null)
            sensorManager.registerListener(accSensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null)
            sensorManager.unregisterListener(accSensorEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.releaseIfNotFinished();
    }

    private void initSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private final SensorEventListener accSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // DecimalFormat df = new DecimalFormat("##.##");
            float z = event.values[0];
            startPosZ = z;
            shiftCarAcc(z);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private final Runnable distanceCounter = new Runnable() {
        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            gameManager.setDistance((int) (millis / TIME_MILLIS_DELAY) - OFFSET_TIME);
            if (gameManager.getDistance() > 0)
                main_Text_distance_counter.setText(gameManager.getDistance() + " m");
            timerHandler.postDelayed(this, TIME_MILLIS_DELAY);
        }
    };

    private void start() {
        randomCreateSign();
        scheduledFuture = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE).scheduleWithFixedDelay(() -> runOnUiThread(this::moveSign),
                TIME_INTERVAL, DELAY, TimeUnit.MILLISECONDS);

    }

    private void stop() {
        scheduledFuture.cancel(false);
        timerHandler.removeCallbacks(distanceCounter);
    }


    private void findViews() {
        main_BTN_right = findViewById(R.id.main_BTN_right);
        main_BTN_left = findViewById(R.id.main_BTN_left);
        if (sensorType.equals("ACC")) {
            main_BTN_right.setVisibility(View.INVISIBLE);
            main_BTN_left.setVisibility(View.INVISIBLE);
        }
        main_IMG_car_arr = new ImageView[]{findViewById(R.id.main_IMG_first_Car), findViewById(R.id.main_IMG_second_Car),
                findViewById(R.id.main_IMG_third_Car), findViewById(R.id.main_IMG_fourth_Car),
                findViewById(R.id.main_IMG_fifth_Car)};

        main_IMG_sign_arr = new ImageView[]{findViewById(R.id.main_IMG_sign_first), findViewById(R.id.main_IMG_sign_second),
                findViewById(R.id.main_IMG_sign_third), findViewById(R.id.main_IMG_sign_fourth), findViewById(R.id.main_IMG_sign_fifth)};

        main_IMG_heart_arr = new ImageView[]{findViewById(R.id.main_IMG_heart_1), findViewById(R.id.main_IMG_heart_2),
                findViewById(R.id.main_IMG_heart_3)};
        main_LAY_car = findViewById(R.id.main_LAY_car);

        main_Text_distance_counter = findViewById(R.id.main_Text_distance_counter);
        main_Text_count_coins = findViewById(R.id.main_Text_count_coins);
    }

    private void initViews() {
        main_BTN_right.setOnClickListener(v -> shiftCarRight());
        main_BTN_left.setOnClickListener(v -> shiftCarLeft());
    }


    private void shiftCarLeft() {
        if (gameManager.shiftCarLeft()) {
            switch (gameManager.getCurrentPos()) {
                case FIRST_ROAD:
                    main_IMG_car_arr[SECOND_ROAD].setVisibility(View.INVISIBLE);
                    main_IMG_car_arr[FIRST_ROAD].setVisibility(View.VISIBLE);
                    break;
                case SECOND_ROAD:
                    main_IMG_car_arr[THIRD_ROAD].setVisibility(View.INVISIBLE);
                    main_IMG_car_arr[SECOND_ROAD].setVisibility(View.VISIBLE);
                    break;
                case THIRD_ROAD:
                    main_IMG_car_arr[FOURTH_ROAD].setVisibility(View.INVISIBLE);
                    main_IMG_car_arr[THIRD_ROAD].setVisibility(View.VISIBLE);
                    break;
                case FOURTH_ROAD:
                    main_IMG_car_arr[FIFTH_ROAD].setVisibility(View.INVISIBLE);
                    main_IMG_car_arr[FOURTH_ROAD].setVisibility(View.VISIBLE);
                    break;
                case FIFTH_ROAD:
                    break;
            }
        }
    }

    private void shiftCarRight() {
        if (gameManager.shiftCarRight()) {
            switch (gameManager.getCurrentPos()) {
                case FIRST_ROAD:
                    break;
                case SECOND_ROAD:
                    main_IMG_car_arr[SECOND_ROAD].setVisibility(View.VISIBLE);
                    main_IMG_car_arr[FIRST_ROAD].setVisibility(View.INVISIBLE);
                    break;
                case THIRD_ROAD:
                    main_IMG_car_arr[SECOND_ROAD].setVisibility(View.INVISIBLE);
                    main_IMG_car_arr[THIRD_ROAD].setVisibility(View.VISIBLE);
                    break;
                case FOURTH_ROAD:
                    main_IMG_car_arr[THIRD_ROAD].setVisibility(View.INVISIBLE);
                    main_IMG_car_arr[FOURTH_ROAD].setVisibility(View.VISIBLE);
                    break;
                case FIFTH_ROAD:
                    main_IMG_car_arr[FOURTH_ROAD].setVisibility(View.INVISIBLE);
                    main_IMG_car_arr[FIFTH_ROAD].setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private void shiftCarAcc(float z) {
        if (gameManager.getCurrentZ() <= 0 && z <= -2.5 * FACTOR && z > -5 * FACTOR) {
            gameManager.setCurrentZ(z);
            gameManager.setCurrentPos(FOURTH_ROAD);
            main_IMG_car_arr[THIRD_ROAD].setVisibility(View.INVISIBLE);
            main_IMG_car_arr[FIFTH_ROAD].setVisibility(View.INVISIBLE);
            main_IMG_car_arr[FOURTH_ROAD].setVisibility(View.VISIBLE);
        }
        if (gameManager.getCurrentZ() <= -2.5 * FACTOR && z < -5 * FACTOR) {
            gameManager.setCurrentZ(z);
            gameManager.setCurrentPos(FIFTH_ROAD);
            main_IMG_car_arr[FOURTH_ROAD].setVisibility(View.INVISIBLE);
            main_IMG_car_arr[FIFTH_ROAD].setVisibility(View.VISIBLE);

        }
        if (gameManager.getCurrentZ() < 5 * FACTOR && gameManager.getCurrentZ() > -5 * FACTOR && z > -2.5 * FACTOR && z < 2.5 * FACTOR) {
            gameManager.setCurrentZ(z);
            gameManager.setCurrentPos(THIRD_ROAD);
            main_IMG_car_arr[SECOND_ROAD].setVisibility(View.INVISIBLE);
            main_IMG_car_arr[FOURTH_ROAD].setVisibility(View.INVISIBLE);
            main_IMG_car_arr[THIRD_ROAD].setVisibility(View.VISIBLE);
        }
        if (gameManager.getCurrentZ() > -2.5 * FACTOR && z >= 2.5 * FACTOR && z < 5 * FACTOR) {
            gameManager.setCurrentZ(z);
            gameManager.setCurrentPos(SECOND_ROAD);
            main_IMG_car_arr[FIRST_ROAD].setVisibility(View.INVISIBLE);
            main_IMG_car_arr[THIRD_ROAD].setVisibility(View.INVISIBLE);
            main_IMG_car_arr[SECOND_ROAD].setVisibility(View.VISIBLE);
        }

        if (gameManager.getCurrentZ() >= 2.5 * FACTOR && z >= 5 * FACTOR) {
            gameManager.setCurrentZ(z);
            gameManager.setCurrentPos(FIRST_ROAD);
            main_IMG_car_arr[SECOND_ROAD].setVisibility(View.INVISIBLE);
            main_IMG_car_arr[FIRST_ROAD].setVisibility(View.VISIBLE);
        }
    }

    private void openGameOverActivity(Activity activity) {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent myIntent = new Intent(activity, GameOverActivity.class);
            Bundle bundle = new Bundle();

            bundle.putString(GameOverActivity.EXTRA_KEY_DISTANCE,""+gameManager.getDistance());
            bundle.putString(GameOverActivity.EXTRA_KEY_SCORE,""+gameManager.getCoin());
            bundle.putString(GameOverActivity.EXTRA_KEY_TOP,""+gameManager.getCurrentTopPos());
            myIntent.putExtra(getString(R.string.gameoverBundle),bundle);
            startActivity(myIntent);
            finish();
        }, DELAY_ACTIVITY);
    }



    private void randomCreateSign() {
        gameManager.randomSignOnRoads();
        main_IMG_sign_arr[gameManager.getRandomCoin()].setImageResource(R.drawable.coins);
        for (int i = 0; i < main_IMG_sign_arr.length; i++) {
            if (gameManager.getRoadsArr()[i]) {
                main_IMG_sign_arr[i].setY(main_IMG_sign_arr[i].getY() - (float) (Math.random() * HIGH_Y + LOW_Y));
                main_IMG_sign_arr[i].setVisibility(View.VISIBLE);
            }
        }
    }


    private void moveSign() {
        for (int i = 0; i < main_IMG_sign_arr.length; i++) {
            if (gameManager.getRoadsArr()[i]) {
                main_IMG_sign_arr[i].setY(main_IMG_sign_arr[i].getY() + DELTA_Y);
                checkCrash(main_IMG_sign_arr[i].getY() + main_IMG_sign_arr[i].getHeight(), main_LAY_car.getY(), i);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void checkCrash(float signPositionY, float carPositionY, int roadIndex) {
        if (signPositionY > carPositionY) {
            main_IMG_sign_arr[roadIndex].setVisibility(View.INVISIBLE);
            main_IMG_sign_arr[roadIndex].setY(0);
            main_IMG_sign_arr[roadIndex].setImageResource(R.drawable.barrier);

            if (gameManager.checkCrash(roadIndex)) {

                toast(getString(R.string.toast_msg));
                vibrate();

                switch (gameManager.getNumberOfHearts()) {
                    case FIRST_HEALTH:
                        main_IMG_heart_arr[FIRST_HEALTH].setVisibility(View.INVISIBLE);
                        gameOver();
                        break;
                    case SECOND_HEALTH:
                        main_IMG_heart_arr[SECOND_HEALTH].setVisibility(View.INVISIBLE);
                        break;
                    case THIRD_HEALTH:
                        main_IMG_heart_arr[THIRD_HEALTH].setVisibility(View.INVISIBLE);
                        break;
                }
            }

            main_Text_count_coins.setText(gameManager.getCoin() + "");
            if (gameManager.isAllFalse())

                randomCreateSign();
        }
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(VIBRATE_TIME, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void gameOver() {
        if (sensorManager != null)
            sensorManager.unregisterListener(accSensorEventListener);
        gameManager.addToTopTen();
        sp.putString(SP.KEY_TOP_TEN, gson.toJson(gameManager.getTopTen()));
        mp.playSound(R.raw.game_over);
        openGameOverActivity(MainActivity.this);
        stop();
    }

}