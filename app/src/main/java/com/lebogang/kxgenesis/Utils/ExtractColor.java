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

package com.lebogang.kxgenesis.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.palette.graphics.Palette;

import com.lebogang.kxgenesis.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ExtractColor {

    public static int getColor(Context context, Uri uri){
        Bitmap bitmap;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            bitmap = null;
        }
        if (bitmap != null){
            Palette.Swatch swatch = Palette.from(bitmap).generate().getMutedSwatch();
            if (swatch != null)
                return swatch.getRgb();
            swatch = Palette.from(bitmap).generate().getDominantSwatch();
            if (swatch != null)
                return swatch.getRgb();
            swatch = Palette.from(bitmap).generate().getVibrantSwatch();
            if (swatch != null)
                return swatch.getRgb();
        }
        return context.getResources().getColor(R.color.colorTranslucent);
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
}
