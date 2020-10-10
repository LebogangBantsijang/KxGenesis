package com.lebogang.kxgenesis;

import android.os.Handler;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.lebogang.kxgenesis.MusicService.MusicPlayer;
import com.lebogang.kxgenesis.Utils.TimeUnitConvert;
import com.lebogang.kxgenesis.databinding.ActivityMainLayoutBinding;

public class ThreadHandler implements Runnable{

    private ActivityMainLayoutBinding binding;
    private ActivityMain activityMain;
    private Handler handler;
    private boolean isRunning = false;

    public ThreadHandler(LifecycleOwner lifecycleOwner, ActivityMainLayoutBinding binding, ActivityMain activityMain) {
        this.binding = binding;
        this.activityMain = activityMain;
        this.handler = new Handler();
        lifecycleOwner.getLifecycle().addObserver(getDefaultLifecycleObserver());
    }

    private DefaultLifecycleObserver getDefaultLifecycleObserver(){
        return new DefaultLifecycleObserver() {
            @Override
            public void onResume(@NonNull LifecycleOwner owner) {
                MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(activityMain);
                if (mediaControllerCompat != null){
                    if (mediaControllerCompat.getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING){
                        if (!isRunning)
                            handler.post(ThreadHandler.this);
                    }
                }
            }

            @Override
            public void onPause(@NonNull LifecycleOwner owner) {
                handler.removeCallbacks(ThreadHandler.this);
                isRunning = false;
            }
        };
    }

    @Override
    public void run() {
        isRunning = true;
        binding.mainLayout.mainPlayer.wavSeekBar.setProgress(MusicPlayer.getPosition());
        binding.mainLayout.mainPlayer.startDuration.setText(TimeUnitConvert.toMinutes(MusicPlayer.getPosition()));
        handler.postDelayed(this, 1000);
    }

    public void onPlaybackStateChanged(int state){
        if (state == PlaybackStateCompat.STATE_PLAYING){
            if (!isRunning)
                handler.post(this);
        }else {
            isRunning = false;
            handler.removeCallbacks(this);
        }
    }
}
