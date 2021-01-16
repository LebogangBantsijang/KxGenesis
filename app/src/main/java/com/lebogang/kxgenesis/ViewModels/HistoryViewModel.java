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
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lebogang.audiofilemanager.AlbumManagement.AlbumManager;
import com.lebogang.audiofilemanager.AudioManagement.AudioManager;
import com.lebogang.audiofilemanager.Models.Album;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.AppUtils.OnGetAlbumHistory;
import com.lebogang.kxgenesis.AppUtils.OnGetSongHistory;
import com.lebogang.kxgenesis.Room.Model.AlbumHistory;
import com.lebogang.kxgenesis.Room.Model.SongHistory;
import com.lebogang.kxgenesis.Room.Repository;

import java.util.List;

public class HistoryViewModel extends ViewModel {

    private final Repository repository;
    private final AudioManager audioManager;
    private final AlbumManager albumManager;
    private OnGetAlbumHistory onGetAlbumHistory;
    private OnGetSongHistory onGetSongHistory;

    public HistoryViewModel(Context context) {
        this.repository = new Repository(context);
        audioManager = new AudioManager(context);
        albumManager = new AlbumManager(context);
    }

    public void insertSongHistory(SongHistory songHistory){
        repository.addSongHistory(songHistory);
    }

    public void deleteSongHistory(SongHistory songHistory){
        repository.removeSongHistory(songHistory);
    }

    public void clearSongHistory(){
        repository.clearSongHistory();
    }

    public void insertAlbumHistory(AlbumHistory albumHistory){
        repository.addAlbumHistory(albumHistory);
    }

    public void deleteAlbumHistory(AlbumHistory albumHistory){
        repository.removeAlbumHistory(albumHistory);
    }

    public void clearAlbumHistory(){
        repository.clearAlbumHistory();
    }

    public void getSongHistory(LifecycleOwner owner, OnGetSongHistory onGetSongHistory){
        repository.getSongHistory().observe(owner, songHistories -> {
            onGetSongHistory.resetSongData();
            if (songHistories.size() >= 40){
                for (int x = 0; x < 40; x++){
                    SongHistory songHistory = songHistories.get(x);
                    Audio audio = audioManager.getAudio(songHistory.getAudioId());
                    if (audio != null){
                        songHistory.setAudio(audio);
                        onGetSongHistory.onGetSongHistory(songHistory);
                    }
                }
            }else {
                for (SongHistory songHistory:songHistories){
                    Audio audio = audioManager.getAudio(songHistory.getAudioId());
                    if (audio != null){
                        songHistory.setAudio(audio);
                        onGetSongHistory.onGetSongHistory(songHistory);
                    }
                }
            }
        });
    }

    public void getAlbumHistory(LifecycleOwner owner, OnGetAlbumHistory onGetAlbumHistory){
        repository.getAlbumHistory().observe(owner, albumHistories -> {
            onGetAlbumHistory.resetAlbumData();
            if (albumHistories.size() >= 40){
                for (int x = 0; x < 40; x++){
                    AlbumHistory albumHistory = albumHistories.get(x);
                    Album album = albumManager.getAlbumByName(albumHistory.getAlbumName());
                    if (album != null){
                        albumHistory.setAlbum(album);
                        onGetAlbumHistory.onGetAlbumHistory(albumHistory);
                    }
                }
            }else {
                for (AlbumHistory albumHistory:albumHistories){
                    Album album = albumManager.getAlbumByName(albumHistory.getAlbumName());
                    if (album != null){
                        albumHistory.setAlbum(album);
                        onGetAlbumHistory.onGetAlbumHistory(albumHistory);
                    }
                }
            }
        });
    }

    public static class HistoryFactory implements ViewModelProvider.Factory {
        private final Context context;
        public HistoryFactory(Context context) {
            this.context = context;
        }
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(HistoryViewModel.class)){
                return (T) new HistoryViewModel(context);
            }
            throw new IllegalArgumentException();
        }
    }
}
