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

package com.lebogang.kxgenesis;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.media.session.PlaybackStateCompat;

public class Preferences {
    private SharedPreferences sharedPreferences;

    public Preferences(Context context) {
        sharedPreferences = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
    }

    public int getThemeResource(){
        int themeIndex = getThemeIndex();
        boolean isThemeLight = isThemeLight();
        if (isThemeLight){
            switch (themeIndex){
                case 0: return R.style.ThemeOne_Light;
                case 2: return R.style.ThemeThree_Light;
                case 3: return R.style.ThemeFour_Light;
                case 4: return R.style.ThemeFive_Light;
                default: return R.style.ThemeTwo_Light;
            }
        } else {
            switch (themeIndex){
                case 0: return R.style.ThemeOne_Dark;
                case 2: return R.style.ThemeThree_Dark;
                case 3: return R.style.ThemeFour_Dark;
                case 4: return R.style.ThemeFive_Dark;
                default: return R.style.ThemeTwo_Dark;
            }
        }
    }

    public void setThemeLight(boolean themeLight) {
        sharedPreferences.edit()
                .putBoolean("isThemeLight", themeLight)
                .apply();
    }

    public void setDisplayGrid(boolean displayGrid) {
        sharedPreferences.edit()
                .putBoolean("isDisplayGrid", displayGrid)
                .apply();
    }

    public void setSaveRepeatEnabled(boolean saveRepeatEnabled) {
        sharedPreferences.edit()
                .putBoolean("saveRepeatEnabled", saveRepeatEnabled)
                .apply();
    }

    public int getThemeIndex() {
        return sharedPreferences.getInt("ThemeIndex", 1);
    }

    public void setThemeIndex(int themeIndex) {
        sharedPreferences.edit()
                .putInt("ThemeIndex", themeIndex)
                .apply();
    }

    public void setLastKnownRepeatMode(int lastKnownRepeatMode) {
        sharedPreferences.edit()
                .putInt("lastKnownRepeatMode", lastKnownRepeatMode)
                .apply();
    }

    public void setLastKnownShuffleMode(int lastKnownShuffleMode) {
        sharedPreferences.edit()
                .putInt("lastKnownShuffleMode", lastKnownShuffleMode)
                .apply();
    }

    public int getLastKnownRepeatMode() {
        return sharedPreferences.getInt("lastKnownRepeatMode", PlaybackStateCompat.REPEAT_MODE_NONE);
    }

    public int getLastKnownShuffleMode() {
        return sharedPreferences.getInt("lastKnownShuffleMode", PlaybackStateCompat.SHUFFLE_MODE_NONE);
    }

    public boolean isThemeLight() {
        return sharedPreferences.getBoolean("isThemeLight", true);
    }

    public boolean isDisplayGrid() {
        return sharedPreferences.getBoolean("isDisplayGrid", true);
    }

    public boolean isSaveRepeatEnabled() {
        return sharedPreferences.getBoolean("saveRepeatEnabled", true);
    }
}
