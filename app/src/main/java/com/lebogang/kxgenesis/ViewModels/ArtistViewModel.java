package com.lebogang.kxgenesis.ViewModels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.lebogang.audiofilemanager.ArtistManagement.ArtistCallbacks;
import com.lebogang.audiofilemanager.ArtistManagement.ArtistManager;


public class ArtistViewModel extends ViewModel {
    private ArtistManager artistManager;

    public void init(Context context){
        artistManager = new ArtistManager(context);
    }

    public void registerCallbacks(ArtistCallbacks artistCallBacks, LifecycleOwner owner){
        artistManager.registerCallbacks(owner, artistCallBacks);
    }

}
