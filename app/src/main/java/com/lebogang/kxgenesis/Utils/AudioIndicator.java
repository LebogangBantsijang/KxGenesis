package com.lebogang.kxgenesis.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.TypedValue;

import androidx.lifecycle.MutableLiveData;
import androidx.palette.graphics.Palette;

import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.ActivityMain;
import com.lebogang.kxgenesis.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AudioIndicator {
    private static MutableLiveData<Audio> currentItem = new MutableLiveData<>();
    private static Palette.Swatch swatch;

    public static void setCurrentItem(Context context, Audio item) {
        Colors.setContext(context, item.getAlbumArtUri());
        currentItem.setValue(item);
    }

    public static MutableLiveData<Audio> getCurrentItem() {
        return currentItem;
    }

    public static class Colors{
        private static int defaultColor;
        private static int windowColor;
        private static int drawableTint;
        private static Palette.Swatch swatch;

        protected static void setContext(Context context, Uri uri){
            defaultColor = ActivityMain.color;
            windowColor = context.getResources().getColor(R.color.colorTranslucent);
            drawableTint = context.getResources().getColor(R.color.colorDrawableTint);
            Bitmap bitmap = getBitmap(context, uri);
            if (bitmap != null){
                swatch = Palette.from(bitmap).generate().getMutedSwatch();
                if (swatch == null)
                    swatch = Palette.from(bitmap).generate().getDominantSwatch();
                if (swatch == null)
                    swatch = Palette.from(bitmap).generate().getVibrantSwatch();
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
