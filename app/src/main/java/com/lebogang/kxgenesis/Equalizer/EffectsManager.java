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

package com.lebogang.kxgenesis.Equalizer;

import android.media.audiofx.AudioEffect;
import android.media.audiofx.LoudnessEnhancer;

import com.lebogang.kxgenesis.Service.MusicPlayer;

public class EffectsManager {
    private static LoudnessEnhancer loudnessEnhancer;

    public EffectsManager() {

    }

    private void init() throws Exception{
        if (loudnessEnhancer == null)
            loudnessEnhancer = new LoudnessEnhancer(MusicPlayer.getSessionID());
    }
    public void setControlStatusListener(AudioEffect.OnControlStatusChangeListener listener) throws Exception {
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

    public boolean isEnabled() throws Exception {
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
