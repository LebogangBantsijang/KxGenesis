package com.lebogang.kxgenesis.Equalizer;

import android.media.audiofx.AudioEffect;
import android.media.audiofx.LoudnessEnhancer;

import com.lebogang.kxgenesis.MusicService.MusicPlayer;

public class EffectsManager {
    private static LoudnessEnhancer loudnessEnhancer;

    public EffectsManager() {

    }

    private void init(){
        if (loudnessEnhancer == null)
            loudnessEnhancer = new LoudnessEnhancer(MusicPlayer.getSessionID());
    }
    public void setControlStatusListener(AudioEffect.OnControlStatusChangeListener listener){
        init();
        loudnessEnhancer.setControlStatusListener(listener);
    }

    public void enable(){
        if (!loudnessEnhancer.getEnabled())
            loudnessEnhancer.setEnabled(true);
    }

    public void disable(){
        loudnessEnhancer.setEnabled(false);
    }

    public boolean isEnabled(){
        if (loudnessEnhancer == null){
            init();
            return false;
        }
        return loudnessEnhancer.getEnabled();
    }

    public void setLoudness(int loud){
        loudnessEnhancer.setTargetGain(loud);
    }

    public int getLoud(){
        if (loudnessEnhancer == null)
            return 0;
        return (int) loudnessEnhancer.getTargetGain();
    }

}
