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
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.AppUtils.AppSettings;
import com.lebogang.kxgenesis.AppUtils.AudioIndicator;

import java.util.ArrayList;
import java.util.List;

public class MusicService extends MediaBrowserServiceCompat {
    private MediaSessionCompat mediaSessionCompat;
    private final PlaybackStateCompat.Builder playbackStateBuilder = new PlaybackStateCompat.Builder();
    private NotificationHandler notificationHandler;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        mediaSessionCompat = new MediaSessionCompat(this, "MusicService");
        setSessionToken(mediaSessionCompat.getSessionToken());
        registerReceiver(new CustomReceiver(mediaSessionCompat.getController()), CustomReceiver.getIntentFilter());
        mediaSessionCompat.setPlaybackState(playbackStateBuilder.setState(PlaybackStateCompat.STATE_NONE
                , 0, 1).build());
        mediaSessionCompat.setActive(true);
        notificationHandler = new NotificationHandler(this, mediaSessionCompat.getSessionToken());
        mediaSessionCompat.setCallback(new MusicCallbacks(this));
    }

    @Override
    public void onDestroy() {
        mediaSessionCompat.getController().getTransportControls().stop();
        super.onDestroy();
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String s, int i, @Nullable Bundle bundle) {
        return new BrowserRoot("root", null);
    }

    @Override
    public void onLoadChildren(@NonNull String s, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        result.sendResult(new ArrayList<>());
    }

    public class MusicCallbacks extends AudioFocusManager{

        private Audio currentAudio;
        private ArrayList<Audio> currentPlaylist;

        public MusicCallbacks(Context context) {
            super(context);
        }

        @Override
        public void onPlayFromUri(Uri uri, Bundle extras) {
            super.onPlayFromUri(uri, extras);
            currentAudio = extras.getParcelable("Item");
            currentPlaylist = extras.getParcelableArrayList("List");
            playbackStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, 0,1f);
            mediaSessionCompat.setPlaybackState(playbackStateBuilder.build());
            startForeground(1021,notificationHandler.getNotification(currentAudio
                    , mediaSessionCompat.getController().getPlaybackState().getState()));
            AudioIndicator.setCurrentItem(getApplicationContext(),currentAudio);
        }

        @Override
        public void onPlay() {
            if (currentAudio != null){
                super.onPlay();
                playbackStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, 0,1f);
                mediaSessionCompat.setPlaybackState(playbackStateBuilder.build());
                startForeground(1021,notificationHandler.getNotification(currentAudio
                        , mediaSessionCompat.getController().getPlaybackState().getState()));
            }
        }

        @Override
        public void onPause() {
            if (currentAudio != null){
                super.onPause();
                playbackStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, 0,1f);
                mediaSessionCompat.setPlaybackState(playbackStateBuilder.build());
                notificationHandler.displayNotification(currentAudio,mediaSessionCompat.getController().getPlaybackState().getState());
                stopForeground(false);
            }
        }

        @Override
        public void onStop() {
            super.onStop();
            stopForeground(true);
            stopSelf();
        }

        @Override
        public void onSkipToNext() {
            int index = getItemIndex(true, currentAudio);
            if (index >= 0){
                super.onSkipToNext();
                playbackStateBuilder.setState(PlaybackStateCompat.STATE_SKIPPING_TO_NEXT, 0,1f);
                mediaSessionCompat.setPlaybackState(playbackStateBuilder.build());
                currentAudio = currentPlaylist.get(index);

                Bundle bundle = new Bundle();
                bundle.putParcelable("Item", currentAudio);
                bundle.putParcelableArrayList("List", currentPlaylist);
                onPlayFromUri(currentAudio.getUri(), bundle);
            }
        }

        @Override
        public void onSkipToPrevious() {
            int index = getItemIndex(false, currentAudio);
            if (index >= 0){
                super.onSkipToNext();
                playbackStateBuilder.setState(PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS, 0,1f);
                mediaSessionCompat.setPlaybackState(playbackStateBuilder.build());
                currentAudio = currentPlaylist.get(index);
                Bundle bundle = new Bundle();
                bundle.putParcelable("Item", currentAudio);
                bundle.putParcelableArrayList("List", currentPlaylist);
                onPlayFromUri(currentAudio.getUri(), bundle);
            }
        }

        private int getItemIndex(boolean areWeSkippingForward, Audio mediaItem){
            if (mediaSessionCompat.getController().getShuffleMode() == PlaybackStateCompat.SHUFFLE_MODE_NONE){
                int index = -1;
                if (mediaItem != null && currentPlaylist != null)
                    for (Audio item:currentPlaylist){
                        if (item.getId() == mediaItem.getId()){
                            if (areWeSkippingForward){
                                index = currentPlaylist.indexOf(item) + 1;
                                if (index >= currentPlaylist.size())
                                    index = 0;
                            }else {
                                index = currentPlaylist.indexOf(item) - 1;
                                if (index < 0)
                                    index = currentPlaylist.size()-1;
                            }
                            break;
                        }
                    }
                return index;
            }else {
                if(currentPlaylist != null)
                    return (int) (Math.random() * (currentPlaylist.size() -1));
                return  -1;
            }
        }

        @Override
        public void onSetRepeatMode(int repeatMode) {
            super.onSetRepeatMode(repeatMode);
            mediaSessionCompat.setRepeatMode(repeatMode);
            AppSettings.setRepeatMode(context,repeatMode);
        }

        @Override
        public void onSetShuffleMode(int shuffleMode) {
            super.onSetShuffleMode(shuffleMode);
            mediaSessionCompat.setShuffleMode(shuffleMode);
            AppSettings.setShuffleMode(context, shuffleMode);
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            super.onCompletion(mp);
            playbackStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, 0,1f);
            mediaSessionCompat.setPlaybackState(playbackStateBuilder.build());
            notificationHandler.displayNotification(currentAudio,mediaSessionCompat.getController().getPlaybackState().getState());
            if (mediaSessionCompat.getController().getRepeatMode() == PlaybackStateCompat.REPEAT_MODE_ONE)
                onPlay();
            if (mediaSessionCompat.getController().getRepeatMode() == PlaybackStateCompat.REPEAT_MODE_ALL)
                onSkipToNext();
        }

        @Override
        public void onAudioFocusChange(int focusChange) {
            super.onAudioFocusChange(focusChange);
            switch (focusChange){
                case AudioManager.AUDIOFOCUS_LOSS:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    onPause();
                    break;
            }
        }
    }
}
