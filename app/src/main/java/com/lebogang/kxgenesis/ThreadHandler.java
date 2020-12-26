/*
 * Copyright (c) 2020. Lebogang Bantsijang
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lebogang.kxgenesis;

import android.os.Handler;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.lebogang.kxgenesis.Service.MusicPlayer;
import com.lebogang.kxgenesis.AppUtils.TimeUnitConvert;

public class ThreadHandler implements Runnable{

    private AppCompatActivity activity;
    private Handler handler;
    private boolean isRunning = false;
    private TextView startDurationTextView;
    private SeekBar seekBar;

    public ThreadHandler(LifecycleOwner lifecycleOwner, AppCompatActivity activity) {
        this.activity = activity;
        this.handler = new Handler();
        lifecycleOwner.getLifecycle().addObserver(getDefaultLifecycleObserver());
    }

    public void setStartDurationTextView(TextView startDurationTextView) {
        this.startDurationTextView = startDurationTextView;
    }

    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }

    private DefaultLifecycleObserver getDefaultLifecycleObserver(){
        return new DefaultLifecycleObserver() {
            @Override
            public void onResume(@NonNull LifecycleOwner owner) {
                MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(activity);
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
        seekBar.setProgress(MusicPlayer.getPosition());
        startDurationTextView.setText(TimeUnitConvert.toMinutes(MusicPlayer.getPosition()));
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
