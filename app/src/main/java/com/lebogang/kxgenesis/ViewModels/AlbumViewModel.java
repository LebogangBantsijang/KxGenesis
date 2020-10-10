package com.lebogang.kxgenesis.ViewModels;

import android.content.ContentValues;
import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.lebogang.audiofilemanager.AlbumManagement.AlbumFileManager;
import com.lebogang.audiofilemanager.Callbacks.AlbumCallBacks;


public class AlbumViewModel extends ViewModel {
    private AlbumFileManager albumFileManager;

    public void initialize(Context context, LifecycleOwner owner){
        albumFileManager = new AlbumFileManager(context, owner);
    }

    public void getObserveItems(AlbumCallBacks albumCallBacks){
        albumFileManager.registerCallbacks(albumCallBacks);
    }

}
