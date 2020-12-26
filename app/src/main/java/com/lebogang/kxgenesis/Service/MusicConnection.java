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

import android.content.ComponentName;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.lebogang.kxgenesis.AppUtils.AppSettings;
import com.lebogang.kxgenesis.MainActivity;

public class MusicConnection extends MediaBrowserCompat.ConnectionCallback  {
    private final MediaBrowserCompat mediaBrowser;
    private final MainActivity mainActivity;

    public MusicConnection(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        mediaBrowser = new MediaBrowserCompat(mainActivity,
                new ComponentName(mainActivity, MusicService.class),this,null);
    }

    public void connectService(){
        if(!mediaBrowser.isConnected()){
            mediaBrowser.connect();
        }
    }

    @Override
    public void onConnected() {
        super.onConnected();
        MediaControllerCompat mediaController = new MediaControllerCompat(mainActivity, mediaBrowser.getSessionToken());
        mediaController.registerCallback(controllerCallback());
        MediaControllerCompat.setMediaController(mainActivity, mediaController);
        if (AppSettings.isRepeatModeSaved(mainActivity)){
            mediaController.getTransportControls().setRepeatMode(AppSettings.getRepeatMode(mainActivity));
            mediaController.getTransportControls().setShuffleMode(AppSettings.getShuffleMode(mainActivity));
        }
    }

    @Override
    public void onConnectionFailed() {
        super.onConnectionFailed();
        mainActivity.onConnectionFailed();
    }

    private MediaControllerCompat.Callback controllerCallback() {
        return new MediaControllerCompat.Callback() {
            @Override
            public void onPlaybackStateChanged(PlaybackStateCompat state) {
                super.onPlaybackStateChanged(state);
                mainActivity.onPlaybackChanged(state.getState());
            }

            @Override
            public void onShuffleModeChanged(int shuffleMode) {
                super.onShuffleModeChanged(shuffleMode);
                mainActivity.onShuffleModeChanged(shuffleMode);
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                super.onRepeatModeChanged(repeatMode);
                mainActivity.onRepeatModeChange(repeatMode);
            }
        };
    }


}
