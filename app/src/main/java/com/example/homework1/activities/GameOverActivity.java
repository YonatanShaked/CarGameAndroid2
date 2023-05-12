package com.example.homework1.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homework1.interfaces.Constants;
import com.example.homework1.R;

public class GameOverActivity extends AppCompatActivity implements Constants {
    private TextView gameOver_LBL_Distance;
    private TextView gameOver_LBL_Score;
    private TextView gameOver_LBL_Rating;
    private Button gameOver_BTN_topTen;
    private Button gameOver_BTN_menu;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        findViews();
        gameOver_LBL_Distance.setText(getIntent().getBundleExtra(getString(R.string.gameoverBundle)).getString(EXTRA_KEY_DISTANCE) + "m");
        gameOver_LBL_Score.setText(getIntent().getBundleExtra(getString(R.string.gameoverBundle)).getString(EXTRA_KEY_SCORE));
        String rating = getIntent().getBundleExtra(getString(R.string.gameoverBundle)).getString(EXTRA_KEY_TOP);

        if(!rating.equals("-1"))
            gameOver_LBL_Rating.setText("You took "+rating+" place");

        gameOver_BTN_topTen.setOnClickListener(v -> openTopTenActivity(GameOverActivity.this));
        gameOver_BTN_menu.setOnClickListener(v -> closeActivity(GameOverActivity.this));
    }

    private void findViews() {
        gameOver_LBL_Distance = findViewById(R.id.gameOver_LBL_Distance);
        gameOver_LBL_Score = findViewById(R.id.gameOver_LBL_Score);
        gameOver_LBL_Rating = findViewById(R.id.gameOver_LBL_Rating);
        gameOver_BTN_menu = findViewById(R.id.gameOver_BTN_menu);
        gameOver_BTN_topTen = findViewById(R.id.gameOver_BTN_topTen);
    }

    private void openTopTenActivity(Activity activity) {
        Intent myIntent = new Intent(activity, TopTenActivity.class);
        startActivity(myIntent);
    }

    private void closeActivity(Activity activity) {
        Intent myIntent = new Intent(activity, GameMenuActivity.class);
        startActivity(myIntent);
        finish();
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
