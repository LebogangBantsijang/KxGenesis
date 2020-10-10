package com.lebogang.kxgenesis.Utils;

import android.util.ArraySet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.lebogang.audiofilemanager.Models.AudioMediaItem;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class AudioIndicator {
    private static MutableLiveData<AudioMediaItem> currentItem = new MutableLiveData<>();

    public static void setCurrentItem(AudioMediaItem item) {
        currentItem.setValue(item);
    }

    public static MutableLiveData<AudioMediaItem> getCurrentItem() {
        return currentItem;
    }

    public static AudioMediaItem getCurrentAudio(){
        return currentItem.getValue();
    }

}
