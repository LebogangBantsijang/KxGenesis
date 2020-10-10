package com.lebogang.kxgenesis.ViewModels;

import android.content.ContentValues;
import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;


import com.lebogang.audiofilemanager.AudioManagement.AudioFileManger;
import com.lebogang.audiofilemanager.Callbacks.AudioCallBacks;
import com.lebogang.audiofilemanager.Models.AudioMediaItem;

public class AudioViewModel extends ViewModel {
    private AudioFileManger audioFileManger;

    public void initialize(Context context, LifecycleOwner owner){
        audioFileManger = new AudioFileManger(context, owner);
    }

    public void getObserveItems(AudioCallBacks audioCallBacks){
        audioFileManger.registerCallbacks(audioCallBacks);
    }

    public void getObserveItems(AudioCallBacks audioCallBacks, String mediaTitle, long mediaId, int type){
        audioFileManger.registerCallbacks(audioCallBacks,mediaTitle,mediaId, type);
    }

    public void updateItem(AudioMediaItem mediaItem, ContentValues values){
        audioFileManger.updateItem(mediaItem, values);
    }

    public void removeItem(AudioMediaItem mediaItem){
        audioFileManger.removeItem(mediaItem);
    }

    public AudioFileManger getAudioFileManger() {
        return audioFileManger;
    }
}
