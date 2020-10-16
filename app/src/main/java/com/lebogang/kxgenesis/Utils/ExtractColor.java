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
