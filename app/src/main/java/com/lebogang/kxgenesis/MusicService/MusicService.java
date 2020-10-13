package com.lebogang.kxgenesis.MusicService;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.lebogang.kxgenesis.Utils.AudioIndicator;

import java.util.ArrayList;
import java.util.List;

public class MusicService extends MediaBrowserServiceCompat {

    private boolean saveLastRepeatMode;
    private MediaSessionCompat mediaSessionCompat;
    private PlaybackStateCompat.Builder playbackStateBuilder = new PlaybackStateCompat.Builder();
    private NotificationHandler notificationHandler;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaSessionCompat = new MediaSessionCompat(this, "MusicService");
        setSessionToken(mediaSessionCompat.getSessionToken());
        //For notifications
        registerReceiver(new CustomReceiver(mediaSessionCompat.getController()), CustomReceiver.getIntentFilter());
        mediaSessionCompat.setPlaybackState(playbackStateBuilder.setState(PlaybackStateCompat.STATE_NONE
                , 0, 1).build());
        mediaSessionCompat.setActive(true);
        notificationHandler = new NotificationHandler(this, mediaSessionCompat.getSessionToken());

        sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        mediaSessionCompat.setCallback(new MusicCallBacks(this));
    }

    @Override
    public void onDestroy() {
        mediaSessionCompat.getController().getTransportControls().stop();
        super.onDestroy();
    }

    private void saveData(){
        saveLastRepeatMode = sharedPreferences.getBoolean("saveRepeatMode", false);
        if (saveLastRepeatMode)
            sharedPreferences.edit().putInt("RepeatMode",mediaSessionCompat.getController().getRepeatMode())
                    .putInt("ShuffleMode",mediaSessionCompat.getController().getShuffleMode())
                    .apply();
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
            AudioIndicator.setCurrentItem(mediaItem);
        }

        @Override
        public void onPlayFromUri(Uri uri, Bundle extras) {
            super.onPlayFromUri(uri, extras);
            mediaItem = extras.getParcelable("Item");
            playbackStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, 0,1f);
            mediaSessionCompat.setPlaybackState(playbackStateBuilder.build());
            startForeground(1021,notificationHandler.getNotification(mediaItem
                    , mediaSessionCompat.getController().getPlaybackState().getState()));
            AudioIndicator.setCurrentItem(mediaItem);
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
                saveData();
                super.onPause();
                playbackStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, 0,1f);
                mediaSessionCompat.setPlaybackState(playbackStateBuilder.build());
                notificationHandler.displayNotification(mediaItem,mediaSessionCompat.getController().getPlaybackState().getState());
                stopForeground(false);
            }
        }

        //Call Stop to stop when shutting down the app lol
        @Override
        public void onStop() {
            super.onStop();
            saveData();
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
                return (int) (Math.random() * (mediaItemList.size() -1));
            }
        }

        @Override
        public void onSetRepeatMode(int repeatMode) {
            super.onSetRepeatMode(repeatMode);
            mediaSessionCompat.setRepeatMode(repeatMode);
        }

        @Override
        public void onSetShuffleMode(int shuffleMode) {
            super.onSetShuffleMode(shuffleMode);
            mediaSessionCompat.setShuffleMode(shuffleMode);
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
