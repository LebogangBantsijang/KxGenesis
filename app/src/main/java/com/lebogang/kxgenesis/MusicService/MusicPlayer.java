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

package com.lebogang.kxgenesis.MusicService;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;

import java.io.IOException;

public class MusicPlayer extends MediaSessionCompat.Callback implements AudioManager.OnAudioFocusChangeListener
        , MediaPlayer.OnCompletionListener {
    public static MediaPlayer mediaPlayer;
    private Context context;

    public MusicPlayer(Context context) {
        this.context = context;
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onPrepareFromUri(Uri uri, Bundle extras) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(context,uri);
            mediaPlayer.prepare();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onPrepare(Uri uri) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(context,uri);
            mediaPlayer.prepare();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPlayFromUri(Uri uri, Bundle extras) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(context,uri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (IOException e) {
            e.printStackTrace();
            //
        }
    }

    @Override
    public void onPlay() {
        mediaPlayer.start();
    }

    @Override
    public void onPause() {
        mediaPlayer.pause();
    }

    @Override
    public void onSeekTo(long position) {
        mediaPlayer.seekTo((int) position);
    }

    @Override
    public void onStop() {
        mediaPlayer.stop();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mediaPlayer.seekTo(0);
    }

    public static int getPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    public static int getSessionID(){
        return mediaPlayer.getAudioSessionId();
    }
}
