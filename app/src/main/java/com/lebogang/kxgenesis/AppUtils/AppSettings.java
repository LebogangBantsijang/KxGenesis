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

package com.lebogang.kxgenesis.AppUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.media.session.PlaybackStateCompat;

import com.lebogang.kxgenesis.R;

public class AppSettings {

    public static int getTheme(Context context){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        boolean isThemeLight = preferences.getBoolean("isThemeLight", true);
        int lastKnownTheme = preferences.getInt("lastKnownTheme", 0);
        if (isThemeLight)
            switch (lastKnownTheme){
                case 0:
                default:
                    return R.style.ThemeOne_Light;
                case 1: return R.style.ThemeTwo_Light;
                case 2: return R.style.ThemeThree_Light;
                case 3: return R.style.ThemeFour_Light;
                case 4: return R.style.ThemeFive_Light;
            }
        else
            switch (lastKnownTheme){
                case 0:
                default: return R.style.ThemeOne_Dark;
                case 1: return R.style.ThemeTwo_Dark;
                case 2: return R.style.ThemeThree_Dark;
                case 3: return R.style.ThemeFour_Dark;
                case 4: return R.style.ThemeFive_Dark;
            }
    }

    public static int getThemeIndex(Context context){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        return preferences.getInt("lastKnownTheme", 0);
    }

    public static void setThemeIndex(Context context, int themeRes){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        preferences.edit()
                .putInt("lastKnownTheme", themeRes)
                .apply();
    }

    public static boolean isThemeLight(Context context){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        return preferences.getBoolean("isThemeLight", true);
    }

    public static void setLightTheme(Context context, boolean value){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("isThemeLight", value).apply();
    }

    public static void setRepeatMode(Context context, int repeatMode){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        preferences.edit()
                .putInt("RepeatMode", repeatMode)
                .apply();
    }

    public static void setShuffleMode(Context context, int shuffleMode){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        preferences.edit()
                .putInt("ShuffleMode", shuffleMode)
                .apply();
    }

    public static int getRepeatMode(Context context){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        return preferences.getInt("RepeatMode", PlaybackStateCompat.REPEAT_MODE_NONE);
    }

    public static int getShuffleMode(Context context){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        return preferences.getInt("ShuffleMode", PlaybackStateCompat.SHUFFLE_MODE_NONE);
    }

    public static boolean isRepeatModeSaved(Context context){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        return preferences.getBoolean("isRepeatModeSaved", true);
    }

    public static void setRepeatModeSaved(Context context, boolean value){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("isRepeatModeSaved",value).apply();
    }

    public static int getSelectedPlayer(Context context){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        int index = preferences.getInt("Player", 0);
        switch (index){
            case 0:
                return R.layout.player_view_one;
            case 1:
                return R.layout.player_view_four;
            case 2:
                return R.layout.player_view_five;
            case 3:
                return R.layout.player_view_two;
            case 4:
                return R.layout.player_view_three;
        }
        return R.layout.player_view_one;
    }

    public static int getSelectedPlayerIndex(Context context){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        return preferences.getInt("Player", 0);
    }

    public static void setPlayer(Context context, int index){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        preferences.edit().putInt("Player", index).apply();
    }

    public static boolean getAlbumDisplayGrid(Context context){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        return preferences.getBoolean("AlbumGrid", true);
    }

    public static boolean getArtistDisplayGrid(Context context){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        return preferences.getBoolean("ArtistGrid", true);
    }

    public static void setAlbumGridDisplay(Context context, boolean value){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("AlbumGrid",value).apply();
    }

    public static void setArtistGridDisplay(Context context, boolean value){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("ArtistGrid",value).apply();
    }

    public static void setFilter(Context context, int duration){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        preferences.edit().putInt("Filter", duration).apply();
    }

    public static int getFilterDuration(Context context, boolean asIndex){
        SharedPreferences preferences = context.getApplicationContext()
                .getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        if (asIndex){
            return preferences.getInt("Filter", 0);
        }else {
            return (preferences.getInt("Filter", 0) * 30000);
        }
    }

}
