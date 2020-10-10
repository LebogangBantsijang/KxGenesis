package com.lebogang.kxgenesis.MusicService;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.lebogang.kxgenesis.ActivityMain;

import java.lang.reflect.Array;

public class MusicConnection extends MediaBrowserCompat.ConnectionCallback  {
    private MediaBrowserCompat mediaBrowser;
    private ActivityMain activityMain;
    private boolean saveLastRepeatMode;
    private SharedPreferences sharedPreferences;

    public MusicConnection(ActivityMain activityMain) {
        this.activityMain = activityMain;
        mediaBrowser = new MediaBrowserCompat(activityMain,
                new ComponentName(activityMain, MusicService.class),this,null);
        if(!mediaBrowser.isConnected()){
            mediaBrowser.connect();
        }
    }

    @Override
    public void onConnected() {
        super.onConnected();
        MediaControllerCompat mediaController = new MediaControllerCompat(activityMain, mediaBrowser.getSessionToken());
        mediaController.registerCallback(controllerCallback());
        MediaControllerCompat.setMediaController(activityMain, mediaController);
        sharedPreferences = activityMain.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        saveLastRepeatMode = sharedPreferences.getBoolean("saveRepeatMode", false);
        if (saveLastRepeatMode){
            int repeatMode = sharedPreferences.getInt("RepeatMode", PlaybackStateCompat.REPEAT_MODE_NONE);
            int shuffleMode = sharedPreferences.getInt("ShuffleMode", PlaybackStateCompat.SHUFFLE_MODE_NONE);
            mediaController.getTransportControls().setRepeatMode(repeatMode);
            mediaController.getTransportControls().setShuffleMode(shuffleMode);
        }
    }

    @Override
    public void onConnectionFailed() {
        super.onConnectionFailed();
        activityMain.onConnectionFailed();
    }

    private MediaControllerCompat.Callback controllerCallback() {
        return new MediaControllerCompat.Callback() {
            @Override
            public void onPlaybackStateChanged(PlaybackStateCompat state) {
                super.onPlaybackStateChanged(state);
                activityMain.onPlaybackChanged(state.getState());
            }

            @Override
            public void onShuffleModeChanged(int shuffleMode) {
                super.onShuffleModeChanged(shuffleMode);
                activityMain.onShuffleModeChanged(shuffleMode);
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                super.onRepeatModeChanged(repeatMode);
                activityMain.onRepeatModeChange(repeatMode);
            }
        };
    }


}
