package com.lebogang.kxgenesis.ViewModels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.lebogang.audiofilemanager.AlbumManagement.AlbumCallbacks;
import com.lebogang.audiofilemanager.AlbumManagement.AlbumManager;


public class AlbumViewModel extends ViewModel {
    private AlbumManager albumManager;

    public void init(Context context){
        albumManager = new AlbumManager(context);
    }

    public void registerCallbacks(AlbumCallbacks callbacks, LifecycleOwner owner){
        albumManager.registerCallbacks(owner,callbacks);
    }

}
