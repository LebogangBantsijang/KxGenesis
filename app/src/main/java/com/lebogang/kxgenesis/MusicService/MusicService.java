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
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.Preferences;
import com.lebogang.kxgenesis.Utils.AudioIndicator;

import java.util.ArrayList;
import java.util.List;

public class MusicService extends MediaBrowserServiceCompat {

    private boolean saveLastRepeatMode;
    private MediaSessionCompat mediaSessionCompat;
    private PlaybackStateCompat.Builder playbackStateBuilder = new PlaybackStateCompat.Builder();
    private NotificationHandler notificationHandler;
    private Preferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = new Preferences(this);
        mediaSessionCompat = new MediaSessionCompat(this, "MusicService");
        setSessionToken(mediaSessionCompat.getSessionToken());
        //For notifications
        registerReceiver(new CustomReceiver(mediaSessionCompat.getController()), CustomReceiver.getIntentFilter());
        mediaSessionCompat.setPlaybackState(playbackStateBuilder.setState(PlaybackStateCompat.STATE_NONE
                , 0, 1).build());
        mediaSessionCompat.setActive(true);
        notificationHandler = new NotificationHandler(this, mediaSessionCompat.getSessionToken());
        mediaSessionCompat.setCallback(new MusicCallBacks(this));
    }

    @Override
    public void onDestroy() {
        mediaSessionCompat.getController().getTransportControls().stop();
        super.onDestroy();
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return new BrowserRoot("root", null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        result.sendResult(new ArrayList<>());
    }

    public class MusicCallBacks extends AudioFocusManager{

        private List<Audio> mediaItemList;
        private Audio mediaItem;

        public MusicCallBacks(Context context) {
            super(context);
        }

        @Override
        public void onCustomAction(String action, Bundle extras) {
            super.onCustomAction(action, extras);
            mediaItemList = extras.getParcelableArrayList("Items");
        }

        @Override
        public void onPrepareFromUri(Uri uri, Bundle extras) {
            super.onPrepareFromUri(uri, extras);
            playbackStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, 0,1f);
            mediaSessionCompat.setPlaybackState(playbackStateBuilder.build());
            startForeground(1021,notificationHandler.getNotification(mediaItem
                    , mediaSessionCompat.getController().getPlaybackState().getState()));
            AudioIndicator.setCurrentItem(getApplicationContext(),mediaItem);
        }

        @Override
        public void onPlayFromUri(Uri uri, Bundle extras) {
            super.onPlayFromUri(uri, extras);
            mediaItem = extras.getParcelable("Item");
            playbackStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, 0,1f);
            mediaSessionCompat.setPlaybackState(playbackStateBuilder.build());
            startForeground(1021,notificationHandler.getNotification(mediaItem
                    , mediaSessionCompat.getController().getPlaybackState().getState()));
            AudioIndicator.setCurrentItem(getApplicationContext(),mediaItem);
        }

        @Override
        public void onPlay() {
            if (mediaItem != null){
                super.onPlay();
                playbackStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, 0,1f);
                mediaSessionCompat.setPlaybackState(playbackStateBuilder.build());
                startForeground(1021,notificationHandler.getNotification(mediaItem
                        , mediaSessionCompat.getController().getPlaybackState().getState()));
            }
        }

        @Override
        public void onPause() {
            if (mediaItem != null){
                super.onPause();
                playbackStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, 0,1f);
                mediaSessionCompat.setPlaybackState(playbackStateBuilder.build());
                notificationHandler.displayNotification(mediaItem,mediaSessionCompat.getController().getPlaybackState().getState());
                stopForeground(false);
            }
        }

        //Call Stop to stop when shutting down the app
        @Override
        public void onStop() {
            super.onStop();
            stopForeground(true);
            stopSelf();
        }

        @Override
        public void onSkipToNext() {
            int x = getItemIndex(true, mediaItem);
            if (x >= 0){
                super.onSkipToNext();
                playbackStateBuilder.setState(PlaybackStateCompat.STATE_SKIPPING_TO_NEXT, 0,1f);
                mediaSessionCompat.setPlaybackState(playbackStateBuilder.build());
                mediaItem = mediaItemList.get(x);
                Bundle bundle = new Bundle();
                bundle.putParcelable("Item", mediaItem);
                onPlayFromUri(mediaItem.getUri(), bundle);
            }
        }

        @Override
        public void onSkipToPrevious() {
            int x = getItemIndex(false, mediaItem);
            if (x >=0){
                super.onSkipToPrevious();
                playbackStateBuilder.setState(PlaybackStateCompat.STATE_SKIPPING_TO_NEXT, 0,1f);
                mediaSessionCompat.setPlaybackState(playbackStateBuilder.build());
                mediaItem = mediaItemList.get(x);
                Bundle bundle = new Bundle();
                bundle.putParcelable("Item", mediaItem);
                onPlayFromUri(mediaItem.getUri(), bundle);
            }
        }

        private int getItemIndex(boolean areWeSkippingForward, Audio mediaItem){
            if (mediaSessionCompat.getController().getShuffleMode() == PlaybackStateCompat.SHUFFLE_MODE_NONE){
                int index = -1;
                if (mediaItem != null && mediaItemList != null)
                    for (Audio item:mediaItemList){
                        if (item.getId() == mediaItem.getId()){
                            if (areWeSkippingForward){
                                index = mediaItemList.indexOf(item) + 1;
                                if (index >= mediaItemList.size())
                                    index = 0;
                            }else {
                                index = mediaItemList.indexOf(item) - 1;
                                if (index < 0)
                                    index = mediaItemList.size()-1;
                            }
                            break;
                        }
                    }
                return index;
            }else {
                if(mediaItemList != null)
                    return (int) (Math.random() * (mediaItemList.size() -1));
                return  -1;
            }
        }

        @Override
        public void onSetRepeatMode(int repeatMode) {
            super.onSetRepeatMode(repeatMode);
            mediaSessionCompat.setRepeatMode(repeatMode);
            preferences.setLastKnownRepeatMode(repeatMode);
        }

        @Override
        public void onSetShuffleMode(int shuffleMode) {
            super.onSetShuffleMode(shuffleMode);
            mediaSessionCompat.setShuffleMode(shuffleMode);
            preferences.setLastKnownShuffleMode(shuffleMode);
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            super.onCompletion(mp);
            playbackStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, 0,1f);
            mediaSessionCompat.setPlaybackState(playbackStateBuilder.build());
            notificationHandler.displayNotification(mediaItem,mediaSessionCompat.getController().getPlaybackState().getState());
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
