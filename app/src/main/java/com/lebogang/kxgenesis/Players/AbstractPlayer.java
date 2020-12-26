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

package com.lebogang.kxgenesis.Players;

import android.widget.SeekBar;
import android.widget.TextView;

import com.lebogang.audiofilemanager.Models.Audio;

import java.util.List;

public abstract class AbstractPlayer {


    public abstract void onPlaybackChanged(int state);

    public abstract void onShuffleModeChanged(int state);

    public abstract void onRepeatModeChange(int state);

    public abstract void onMediaChanged(Audio audio);

    public abstract void onPlayClick();

    public abstract void onNextClick();

    public abstract void onPrevClick();

    public abstract void onRepeatClick();

    public abstract void onShuffleClick();

    public abstract void onSeek();

    public abstract void onCollapse();

    public abstract void setPagerData(List<Audio> list);

    public abstract SeekBar getSeekBar();

    public abstract TextView getStartDurationTextView();

    public void initViews(){
        onPlayClick();
        onPrevClick();
        onNextClick();
        onRepeatClick();
        onShuffleClick();
        onSeek();
        onCollapse();
    }
}
