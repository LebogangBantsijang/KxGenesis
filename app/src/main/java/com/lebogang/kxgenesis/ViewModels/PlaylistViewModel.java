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

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.lebogang.audiofilemanager.Models.Playlist;
import com.lebogang.audiofilemanager.PlaylistManagement.PlaylistCallbacks;
import com.lebogang.audiofilemanager.PlaylistManagement.PlaylistManager;

import java.util.ArrayList;
import java.util.List;


public class PlaylistViewModel extends ViewModel implements PlaylistCallbacks{
    private PlaylistManager playlistManager;
    private PlaylistCallbacks callbacks;
    private List<Playlist> list = new ArrayList<>();
    private boolean update = false;

    public void init(Context context){
        playlistManager = new PlaylistManager(context);
    }

    public void registerCallbacks(PlaylistCallbacks playlistCallBacks, LifecycleOwner owner){
        this.callbacks = playlistCallBacks;
        playlistManager.registerCallbacks(this, owner);
    }

    public PlaylistManager getPlaylistManager() {
        return playlistManager;
    }

    @Override
    public void onQueryComplete(List<Playlist> playlists) {
        if (list.size() != playlists.size()){
            list = playlists;
            callbacks.onQueryComplete(playlists);
        } else {
            for (Playlist playlist: playlists)
                update = !list.contains(playlist);
            if (update){
                list = playlists;
                callbacks.onQueryComplete(list);
            }
        }
    }
}
