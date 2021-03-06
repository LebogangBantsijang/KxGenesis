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

import com.lebogang.audiofilemanager.AudioManagement.AudioCallbacks;
import com.lebogang.audiofilemanager.AudioManagement.AudioManager;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.AppUtils.AppSettings;

import java.util.ArrayList;
import java.util.List;

public class AudioViewModel extends ViewModel implements AudioCallbacks{
    private AudioManager audioFileManger;
    private AudioCallbacks callbacks;
    private List<Audio> list = new ArrayList<>();
    private boolean update = false;

    public AudioViewModel(Context context) {
        audioFileManger = new AudioManager(context);
        audioFileManger.setDuration(AppSettings.getFilterDuration(context, false));
    }


    public void registerCallback(AudioCallbacks audioCallbacks, LifecycleOwner owner){
        this.callbacks = audioCallbacks;
        audioFileManger.registerCallbacks(this, owner);
    }

    public void registerCallbacksForAlbumAudio(AudioCallbacks callbacks, LifecycleOwner owner, String albumName){
        this.callbacks = callbacks;
        audioFileManger.registerCallbacksForAlbumAudio(this,owner,albumName);
    }

    public void registerCallbacksForArtistAudio(AudioCallbacks callbacks, LifecycleOwner owner, String artistName){
        this.callbacks = callbacks;
        audioFileManger.registerCallbacksForArtistAudio(this,owner,artistName);
    }

    public void registerCallbacksForAudioIds(AudioCallbacks callbacks, LifecycleOwner owner, List<Long> audioIds){
        this.callbacks = callbacks;
        audioFileManger.registerCallbacksForAudioIds(this, owner, audioIds);
    }

    public AudioManager getAudioManager() {
        return audioFileManger;
    }

    @Override
    public void onQueryComplete(List<Audio> audioList) {
        if (list.size() != audioList.size()){
            list = audioList;
            callbacks.onQueryComplete(audioList);
        } else {
            for (Audio audio: audioList)
                update = !list.contains(audio);
            if (update){
                list = audioList;
                callbacks.onQueryComplete(list);
            }
        }
    }

    public static class AudioViewModelFactory implements ViewModelProvider.Factory{

        private final Context context;

        public AudioViewModelFactory(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(AudioViewModel.class)){
                return (T) new AudioViewModel(context);
            }
            throw new IllegalArgumentException();
        }
    }
}
