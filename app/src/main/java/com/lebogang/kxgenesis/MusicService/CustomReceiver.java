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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;

public class CustomReceiver extends BroadcastReceiver {
    private MediaControllerCompat mediaController;
    public static final String ACTION_PLAY_PAUSE = "play_pause";
    public static final String ACTION_NEXT = "next";
    public static final String ACTION_PREVIOUS = "previous";


    public CustomReceiver(MediaControllerCompat mediaController) {
        this.mediaController = mediaController;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            case AudioManager.ACTION_AUDIO_BECOMING_NOISY:
                mediaController.getTransportControls().pause();
            case ACTION_PLAY_PAUSE:
                if (mediaController.getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING)
                    mediaController.getTransportControls().pause();
                else mediaController.getTransportControls().play();
                break;
            case ACTION_PREVIOUS:
                mediaController.getTransportControls().skipToPrevious();
                break;
            case ACTION_NEXT:
                mediaController.getTransportControls().skipToNext();
                break;
        }
    }

    public static IntentFilter getIntentFilter(){
        IntentFilter intent = new IntentFilter();
        intent.addAction(ACTION_PLAY_PAUSE);
        intent.addAction(ACTION_NEXT);
        intent.addAction(ACTION_PREVIOUS);
        return intent;
    }
}
