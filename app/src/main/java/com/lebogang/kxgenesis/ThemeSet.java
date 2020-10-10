package com.lebogang.kxgenesis;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

public class ThemeSet extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme = getSharedPreferences("Preferences", Context.MODE_PRIVATE).getInt("Theme", R.style.AppTheme);
        setTheme(theme);
    }

}
