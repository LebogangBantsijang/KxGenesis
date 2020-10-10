package com.lebogang.kxgenesis.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.ChipGroup;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private SharedPreferences preferences;
    private boolean saveRepeatMode;
    private int theme;
    private int itemType = 1;
    public static final int ITEM_DISPLAY_TILES = 1;
    public static final int ITEM_DISPLAY_SINGLE_LINE = 2;

    public SettingsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        setupView();
        setupRadioButtons();
        setupChips();
    }

    private void init(){
        preferences = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        saveRepeatMode = preferences.getBoolean("saveRepeatMode", false);
        theme = preferences.getInt("Theme", R.style.AppTheme);
        itemType = preferences.getInt("Item_Display", ITEM_DISPLAY_TILES);
    }

    private void setupView(){
        binding.switch2.setChecked(saveRepeatMode);
        binding.switch2.setOnCheckedChangeListener((buttonView, isChecked) -> saveRepeatMode = isChecked);
    }

    private void setupRadioButtons(){
        if (theme == R.style.AppTheme)
            binding.radioGroup.check(R.id.lightRadioButton);
        else if (theme == R.style.AppTheme_Dark)
            binding.radioGroup.check(R.id.darkRadioButton);
        else
            binding.radioGroup.check(R.id.systemRadioButton);
        binding.radioGroup.setOnCheckedChangeListener((RadioGroup group, int checkedId)-> {
            if (checkedId == R.id.lightRadioButton)
                theme = R.style.AppTheme;
            if (checkedId == R.id.darkRadioButton)
                theme = R.style.AppTheme_Dark;
            if (checkedId == R.id.systemRadioButton)
                theme = R.style.AppTheme_DayNight;
            requireActivity().recreate();
        });
    }

    private void setupChips(){
        if (itemType == ITEM_DISPLAY_TILES)
            binding.chipGroup.check(R.id.multiLineChip);
        else
            binding.chipGroup.check(R.id.singleLineChip);
        binding.chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.multiLineChip)
                itemType = ITEM_DISPLAY_TILES;
            else
                itemType = ITEM_DISPLAY_SINGLE_LINE;
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        preferences.edit()
                .putBoolean("saveRepeatMode", saveRepeatMode)
                .putInt("Theme", theme)
                .putInt("Item_Display", itemType)
                .apply();
    }
}
