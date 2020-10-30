package com.lebogang.kxgenesis;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.media.session.PlaybackStateCompat;

public class Preferences {

    private Context context;
    private SharedPreferences sharedPreferences;

    public Preferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
    }

    public int getThemeResource(){
        int themeIndex = getThemeIndex();
        boolean isThemeLight = isThemeLight();
        if (isThemeLight){
            switch (themeIndex){
                case 1: return R.style.ThemeTwo_Light;
                case 2: return R.style.ThemeThree_Light;
                case 3: return R.style.ThemeFour_Light;
                case 4: return R.style.ThemeFive_Light;
                default: return R.style.ThemeOne_Light;
            }
        } else {
            switch (themeIndex){
                case 1: return R.style.ThemeTwo_Dark;
                case 2: return R.style.ThemeThree_Dark;
                case 3: return R.style.ThemeFour_Dark;
                case 4: return R.style.ThemeFive_Dark;
                default: return R.style.ThemeOne_Dark;
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
        return sharedPreferences.getInt("ThemeIndex", 0);
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
