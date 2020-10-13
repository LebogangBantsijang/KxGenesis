package com.lebogang.kxgenesis.ViewModels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.lebogang.audiofilemanager.GenreManagement.GenreCallbacks;
import com.lebogang.audiofilemanager.GenreManagement.GenreManager;

public class GenreViewModel extends ViewModel {
    private GenreManager genreFileManager;

    public void init(Context context){
        genreFileManager = new GenreManager(context);
    }

    public void registerCallbacks(GenreCallbacks genreCallBacks, LifecycleOwner owner){
        genreFileManager.registerCallbacks(owner, genreCallBacks);
    }

}
