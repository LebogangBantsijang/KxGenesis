package com.lebogang.kxgenesis.ViewModels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.lebogang.audiofilemanager.Callbacks.GenreCallBacks;
import com.lebogang.audiofilemanager.GenreManagement.GenreFileManager;

public class GenreViewModel extends ViewModel {
    private GenreFileManager genreFileManager;

    public void initialize(Context context, LifecycleOwner owner){
        genreFileManager = new GenreFileManager(context, owner);
    }

    public void getObserveItems(GenreCallBacks genreCallBacks){
        genreFileManager.registerCallbacks(genreCallBacks);
    }

}
