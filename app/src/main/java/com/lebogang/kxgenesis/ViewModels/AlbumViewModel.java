package com.lebogang.kxgenesis.ViewModels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.lebogang.audiofilemanager.AlbumManagement.AlbumCallbacks;
import com.lebogang.audiofilemanager.AlbumManagement.AlbumManager;
import com.lebogang.audiofilemanager.Models.Album;

import java.util.ArrayList;
import java.util.List;


public class AlbumViewModel extends ViewModel implements AlbumCallbacks{
    private AlbumManager albumManager;
    private AlbumCallbacks callbacks;
    private List<Album> list = new ArrayList<>();
    private boolean update = false;

    public void init(Context context){
        albumManager = new AlbumManager(context);
    }

    public void registerCallbacks(AlbumCallbacks callbacks, LifecycleOwner owner){
        this.callbacks = callbacks;
        albumManager.registerCallbacks(owner,this);
    }

    public AlbumManager getAlbumManager() {
        return albumManager;
    }

    @Override
    public void onQueryComplete(List<Album> albumList) {
        if (list.size() != albumList.size()) {
            list = albumList;
            callbacks.onQueryComplete(albumList);
        } else {
            for (Album album: albumList)
                update = !list.contains(album);
            if (update){
                list = albumList;
                callbacks.onQueryComplete(list);
            }
        }
    }
}
