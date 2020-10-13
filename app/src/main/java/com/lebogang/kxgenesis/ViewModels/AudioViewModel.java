package com.lebogang.kxgenesis.ViewModels;

import android.content.ContentValues;
import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.lebogang.audiofilemanager.AudioManagement.AudioCallbacks;
import com.lebogang.audiofilemanager.AudioManagement.AudioManager;

public class AudioViewModel extends ViewModel {
    private AudioManager audioFileManger;

    public void init(Context context){
        audioFileManger = new AudioManager(context);
    }

    public void registerCallback(AudioCallbacks audioCallbacks, LifecycleOwner owner){
        audioFileManger.registerCallbacks(audioCallbacks, owner);
    }

    public void registerCallbacksForAlbumAudio(AudioCallbacks callbacks, LifecycleOwner owner, String albumName){
        audioFileManger.registerCallbacksForAlbumAudio(callbacks,owner,albumName);
    }

    public void registerCallbacksForArtistAudio(AudioCallbacks callbacks, LifecycleOwner owner, String artistName){
        audioFileManger.registerCallbacksForArtistAudio(callbacks,owner,artistName);
    }

    public void registerCallbacksForAudioIds(AudioCallbacks callbacks, LifecycleOwner owner, String[] audioIds){
        audioFileManger.registerCallbacksForAudioIds(callbacks, owner, audioIds);
    }

    public AudioManager getAudioManager() {
        return audioFileManger;
    }
}
