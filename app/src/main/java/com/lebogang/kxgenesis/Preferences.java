package com.lebogang.kxgenesis;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.media.session.PlaybackStateCompat;

public class Preferences {

    private Context context;
    private SharedPreferences sharedPreferences;
    private boolean isThemeLight;
    private boolean isDisplayGrid;
    private boolean saveRepeatEnabled;
    private int lastKnownRepeatMode;
    private int lastKnownShuffleMode;

    public Preferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        getData();
    }

    private void getData(){
        isThemeLight = sharedPreferences.getBoolean("isThemeLight", true);
        isDisplayGrid = sharedPreferences.getBoolean("isDisplayGrid", true);
        saveRepeatEnabled = sharedPreferences.getBoolean("saveRepeatEnabled", true);
        lastKnownRepeatMode = sharedPreferences.getInt("lastKnownRepeatMode", PlaybackStateCompat.REPEAT_MODE_NONE);
        lastKnownShuffleMode = sharedPreferences.getInt("lastKnownShuffleMode", PlaybackStateCompat.SHUFFLE_MODE_NONE);
    }

    public int getThemeResource(){
        if (isThemeLight)
            return R.style.AppTheme;
        return R.style.AppTheme_Dark;
    }

    public void savePreferences(){
        sharedPreferences.edit()
                .putBoolean("isThemeLight", isThemeLight)
                .putBoolean("isDisplayGrid", isDisplayGrid)
                .putBoolean("saveRepeatEnabled", saveRepeatEnabled)
                .putInt("lastKnownRepeatMode", lastKnownRepeatMode)
                .putInt("lastKnownShuffleMode", lastKnownShuffleMode)
                .apply();
    }

    public void setThemeLight(boolean themeLight) {
        isThemeLight = themeLight;
    }

    public void setDisplayGrid(boolean displayGrid) {
        isDisplayGrid = displayGrid;
    }

    public void setSaveRepeatEnabled(boolean saveRepeatEnabled) {
        this.saveRepeatEnabled = saveRepeatEnabled;
    }

    public void setLastKnownRepeatMode(int lastKnownRepeatMode) {
        this.lastKnownRepeatMode = lastKnownRepeatMode;
    }

    public void setLastKnownShuffleMode(int lastKnownShuffleMode) {
        this.lastKnownShuffleMode = lastKnownShuffleMode;
    }

    public int getLastKnownRepeatMode() {
        return lastKnownRepeatMode;
    }

    public int getLastKnownShuffleMode() {
        return lastKnownShuffleMode;
    }

    public boolean isThemeLight() {
        return isThemeLight;
    }

    public boolean isDisplayGrid() {
        return isDisplayGrid;
    }

    public boolean isSaveRepeatEnabled() {
        return saveRepeatEnabled;
    }
}
