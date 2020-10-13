package com.lebogang.kxgenesis.ViewModels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.lebogang.audiofilemanager.PlaylistManagement.PlaylistCallbacks;
import com.lebogang.audiofilemanager.PlaylistManagement.PlaylistManager;


public class PlaylistViewModel extends ViewModel {
    private PlaylistManager playlistManager;

    public void init(Context context){
        playlistManager = new PlaylistManager(context);
    }

    public void registerCallbacks(PlaylistCallbacks playlistCallBacks, LifecycleOwner owner){
        playlistManager.registerCallbacks(playlistCallBacks, owner);
    }

    public PlaylistManager getPlaylistManager() {
        return playlistManager;
    }
}
