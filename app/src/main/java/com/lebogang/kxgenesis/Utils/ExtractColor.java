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
        //Convert the uri to bitmap
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            bitmap = null;
        }
        //If the conversion fail then return the window color
        //if not then get the dominant color from the image
        //if the dominant color is light and the theme is dark then return window color, the same applies with light theme
        if (bitmap != null){
            int colorDominantColor = Palette.from(bitmap).generate().getMutedColor(context.getResources().getColor(R.color.colorPrimary));
            return colorDominantColor;
        }
        return context.getResources().getColor(R.color.colorPrimary);
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
