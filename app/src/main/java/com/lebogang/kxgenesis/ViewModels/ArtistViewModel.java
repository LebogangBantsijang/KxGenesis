package com.lebogang.kxgenesis.ViewModels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.lebogang.audiofilemanager.ArtistManagement.ArtistCallbacks;
import com.lebogang.audiofilemanager.ArtistManagement.ArtistManager;
import com.lebogang.audiofilemanager.Models.Album;
import com.lebogang.audiofilemanager.Models.Artist;

import java.util.ArrayList;
import java.util.List;


public class ArtistViewModel extends ViewModel implements ArtistCallbacks{
    private ArtistManager artistManager;
    private ArtistCallbacks callbacks;
    private List<Artist> list = new ArrayList<>();
    private boolean update = false;

    public void init(Context context){
        artistManager = new ArtistManager(context);
    }

    public void registerCallbacks(ArtistCallbacks artistCallBacks, LifecycleOwner owner){
        this.callbacks = artistCallBacks;
        artistManager.registerCallbacks(owner, this);
    }

    public ArtistManager getArtistManager() {
        return artistManager;
    }

    @Override
    public void onQueryComplete(List<Artist> artistList) {
        if (list.size() != artistList.size()){
            list = artistList;
            callbacks.onQueryComplete(artistList);
        }else {
            for (Artist artist: artistList)
                update = !list.contains(artist);
            if (update){
                list = artistList;
                callbacks.onQueryComplete(list);
            }
        }
    }
}
