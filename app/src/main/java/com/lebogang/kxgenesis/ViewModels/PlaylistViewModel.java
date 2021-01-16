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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.lebogang.audiofilemanager.AudioManagement.AudioCallbacks;
import com.lebogang.audiofilemanager.AudioManagement.AudioManager;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.Room.Model.PlaylistAudio;
import com.lebogang.kxgenesis.Room.Model.PlaylistDetails;
import com.lebogang.kxgenesis.Room.Repository;

import java.util.ArrayList;
import java.util.List;


public class PlaylistViewModel extends ViewModel {
    private final AudioManager audioManager;
    private final Repository repository;
    private AudioCallbacks callbacks;


    public PlaylistViewModel(Context context) {
        audioManager = new AudioManager(context);
        repository = new Repository(context);
    }

    public LiveData<List<PlaylistDetails>> getPlaylist(){
        return repository.getPlaylists();
    }

    public void updatePlaylist(PlaylistDetails details){
        repository.updatePlaylist(details);
    }

    public void insertPlaylist(PlaylistDetails details){
        repository.insertPlaylist(details);
    }

    public void deletePlaylist(PlaylistDetails details){
        repository.deletePlaylist(details);
    }

    public void clearPlaylists(){
        repository.clearPlaylists();
    }

    public void getPlaylistAudio(LifecycleOwner owner, AudioCallbacks audioCallbacks, int playlistId){
        this.callbacks = audioCallbacks;
        repository.getPlaylistAudioIds(playlistId).observe(owner, audioIds->{
            List<Audio> list = audioManager.getAudio(audioIds);
            callbacks.onQueryComplete(list);
        });
    }

    public void addPlaylistAudio(PlaylistAudio playlistAudio){
        repository.addPlaylistAudio(playlistAudio);
    }

    public void getAudio(LifecycleOwner owner, AudioCallbacks audioCallbacks){
        audioManager.registerCallbacks(audioCallbacks, owner);
    }

    public void clearPlaylistAudio(int playlistId){
        repository.clearPlaylistAudio(playlistId);
    }

    public void deletePlaylistAudio(int playlistId, long audioId){
        repository.deletePlaylistAudio(playlistId, audioId);
    }

    public static class PlaylistViewModelFactory implements ViewModelProvider.Factory {
        private final Context context;
        public PlaylistViewModelFactory(Context context) {
            this.context = context;
        }
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(PlaylistViewModel.class)){
                return (T) new PlaylistViewModel(context);
            }
            throw new IllegalArgumentException();
        }
    }
}
