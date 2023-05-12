package com.example.homework1.utils;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicPlayer {
    private MediaPlayer mp;
    private final Context mContext;

    public MusicPlayer(Context context) {
        mContext = context;
    }

    public void playSound(int rawSound) {
        mp = android.media.MediaPlayer.create(mContext, rawSound);
        mp.setOnCompletionListener(mediaPlayer -> {
            mediaPlayer.release();
            mp = null;
        });
        mp.start();
    }

    public void releaseIfNotFinished() {
        if (mp != null) {
            mp.release();
        }
    }
}