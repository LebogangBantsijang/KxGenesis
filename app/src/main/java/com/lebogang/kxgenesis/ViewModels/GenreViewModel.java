package com.lebogang.kxgenesis.ViewModels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.lebogang.audiofilemanager.GenreManagement.GenreCallbacks;
import com.lebogang.audiofilemanager.GenreManagement.GenreManager;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.audiofilemanager.Models.Genre;

import java.util.ArrayList;
import java.util.List;

public class GenreViewModel extends ViewModel implements GenreCallbacks{
    private GenreManager genreFileManager;
    private GenreCallbacks callbacks;
    private List<Genre> list = new ArrayList<>();
    private boolean update = false;

    public void init(Context context){
        genreFileManager = new GenreManager(context);
    }

    public void registerCallbacks(GenreCallbacks genreCallBacks, LifecycleOwner owner){
        this.callbacks = genreCallBacks;
        genreFileManager.registerCallbacks(owner, this);
    }

    @Override
    public void onQueryComplete(List<Genre> genreList) {
        if (list.size() != genreList.size()){
            list = genreList;
            callbacks.onQueryComplete(genreList);
        } else {
            for (Genre genre: genreList)
                update = !list.contains(genre);
            if (update){
                list = genreList;
                callbacks.onQueryComplete(list);
            }
        }
    }
}
