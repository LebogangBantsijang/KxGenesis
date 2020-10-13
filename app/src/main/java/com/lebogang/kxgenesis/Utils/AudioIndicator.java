package com.lebogang.kxgenesis.Utils;

import androidx.lifecycle.MutableLiveData;

import com.lebogang.audiofilemanager.Models.Audio;

public class AudioIndicator {
    private static MutableLiveData<Audio> currentItem = new MutableLiveData<>();

    public static void setCurrentItem(Audio item) {
        currentItem.setValue(item);
    }

    public static MutableLiveData<Audio> getCurrentItem() {
        return currentItem;
    }

    public static Audio getCurrentAudio(){
        return currentItem.getValue();
    }

}
