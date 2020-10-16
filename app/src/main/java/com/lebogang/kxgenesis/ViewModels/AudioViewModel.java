package com.lebogang.kxgenesis.ViewModels;

import android.content.ContentValues;
import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.lebogang.audiofilemanager.AudioManagement.AudioCallbacks;
import com.lebogang.audiofilemanager.AudioManagement.AudioManager;
import com.lebogang.audiofilemanager.Models.Artist;
import com.lebogang.audiofilemanager.Models.Audio;

import java.util.ArrayList;
import java.util.List;

public class AudioViewModel extends ViewModel implements AudioCallbacks{
    private AudioManager audioFileManger;
    private AudioCallbacks callbacks;
    private List<Audio> list = new ArrayList<>();
    private boolean update = false;

    public void init(Context context){
        audioFileManger = new AudioManager(context);
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

    public void registerCallbacksForAudioIds(AudioCallbacks callbacks, LifecycleOwner owner, String[] audioIds){
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
}
