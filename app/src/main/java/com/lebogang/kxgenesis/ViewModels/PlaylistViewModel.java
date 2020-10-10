package com.lebogang.kxgenesis.ViewModels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.lebogang.audiofilemanager.Callbacks.PlaylistCallBacks;
import com.lebogang.audiofilemanager.PlaylistManagement.PlaylistFileManager;


public class PlaylistViewModel extends ViewModel {
    private PlaylistFileManager playlistFileManager;

    public void initialize(Context context, LifecycleOwner owner){
        playlistFileManager = new PlaylistFileManager(context, owner);
    }

    public void getObserveItems(PlaylistCallBacks playlistCallBacks){
        playlistFileManager.registerCallbacks(playlistCallBacks);
    }

    public PlaylistFileManager getPlaylistFileManager() {
        return playlistFileManager;
    }
}
