/*
 * Copyright (c) 2020. Lebogang Bantsijang
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lebogang.kxgenesis.ViewModels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lebogang.audiofilemanager.ArtistManagement.ArtistCallbacks;
import com.lebogang.audiofilemanager.ArtistManagement.ArtistManager;
import com.lebogang.audiofilemanager.Models.Artist;

import java.util.ArrayList;
import java.util.List;


public class ArtistViewModel extends ViewModel implements ArtistCallbacks{
    private final ArtistManager artistManager;
    private ArtistCallbacks callbacks;
    private List<Artist> list = new ArrayList<>();
    private boolean update = false;

    public ArtistViewModel(Context context) {
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

    public static class ArtistViewModelFactory implements ViewModelProvider.Factory{
        private final Context context;

        public ArtistViewModelFactory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ArtistViewModel.class)){
                return (T) new ArtistViewModel(context);
            }
            throw new IllegalArgumentException();
        }
    }
}
