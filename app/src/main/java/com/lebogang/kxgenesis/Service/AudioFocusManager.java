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

package com.lebogang.kxgenesis.Service;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

public class AudioFocusManager extends MusicPlayer {

    private final AudioManager audioManager;
    private AudioFocusRequest focusRequest;

    public AudioFocusManager(Context context) {
        super(context);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setOnAudioFocusChangeListener(this)
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build())
                    .build();
        }
    }

    @Override
    public void onPlay() {
        requestAudioF();
        super.onPlay();
    }

    @Override
    public void onPause() {
        abandonAudioF();
        super.onPause();
    }

    @Override
    public void onStop() {
        abandonAudioF();
        super.onStop();
    }

    @Override
    public void onPlayFromMediaId(String mediaId, Bundle extras) {
        requestAudioF();
    }

    @Override
    public void onPlayFromSearch(String query, Bundle extras) {
        requestAudioF();
    }

    @Override
    public void onPlayFromUri(Uri uri, Bundle extras) {
        requestAudioF();
        super.onPlayFromUri(uri,extras);
    }

    @Override
    public void onSkipToNext() {
        abandonAudioF();
        super.onSkipToNext();
    }

    @Override
    public void onSkipToPrevious() {
        abandonAudioF();
        super.onSkipToPrevious();
    }

    public void requestAudioF(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.requestAudioFocus(focusRequest);
        }else {
            audioManager.requestAudioFocus(
                    this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
    }

    public void abandonAudioF(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            audioManager.abandonAudioFocusRequest(focusRequest);
        }else {
            audioManager.abandonAudioFocus(this);
        }
    }
}
