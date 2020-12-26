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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.palette.graphics.Palette;

import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.MainActivity;
import com.lebogang.kxgenesis.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AudioIndicator {
    private static MutableLiveData<Audio> currentItem = new MutableLiveData<>();
    private static Palette.Swatch swatch;

    public static void setCurrentItem(Context context, Audio item) {
        Colors.setContext(context.getApplicationContext(), item.getAlbumArtUri());
        currentItem.setValue(item);
    }

    public static MutableLiveData<Audio> getCurrentItem() {
        return currentItem;
    }

    public static Bitmap getBitmap(Context context, Uri uri){
        Bitmap bitmap;
        //Convert the uri to bitmap
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            bitmap = BitmapFactory.decodeResource(context.getResources(),R.raw.default_bg);
        }
        return bitmap;
    }

    public static class Colors{
        private static int defaultColor;
        private static int windowColor;
        private static int drawableTint;
        private static Palette.Swatch swatch;

        protected static void setContext(Context context, Uri uri){
            defaultColor = MainActivity.COLOR;
            windowColor = context.getApplicationContext().getResources().getColor(R.color.colorTranslucent);
            drawableTint = context.getApplicationContext().getResources().getColor(R.color.colorDrawableTint);
            Bitmap bitmap = getBitmap(context.getApplicationContext(), uri);
            if (bitmap != null){
                swatch = Palette.from(bitmap).generate().getDarkVibrantSwatch();
                if (swatch == null)
                    swatch = Palette.from(bitmap).generate().getLightVibrantSwatch();
                if (swatch == null)
                    swatch = Palette.from(bitmap).generate().getMutedSwatch();
            } else
                swatch = null;
        }

        public static int getDefaultColor(){
            if (swatch != null)
                return swatch.getRgb();
            return defaultColor;
        }

        public static int getBackgroundColor(){
            if (swatch != null)
                return swatch.getRgb();
            return windowColor;
        }

        public static int getDrawableTintColor(){
            if (swatch != null)
                return swatch.getTitleTextColor();
            return drawableTint;
        }

        private static Bitmap getBitmap(Context context, Uri uri){
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
