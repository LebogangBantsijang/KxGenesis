package com.lebogang.kxgenesis.ViewModels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.lebogang.audiofilemanager.ArtistManagement.ArtistFileManager;
import com.lebogang.audiofilemanager.Callbacks.ArtistCallBacks;


public class ArtistViewModel extends ViewModel {
    private ArtistFileManager artistFileManager;

    public void initialize(Context context, LifecycleOwner owner){
        artistFileManager = new ArtistFileManager(context, owner);
    }

    public void getObserveItems(ArtistCallBacks artistCallBacks){
        artistFileManager.registerCallbacks(artistCallBacks);
    }

}
