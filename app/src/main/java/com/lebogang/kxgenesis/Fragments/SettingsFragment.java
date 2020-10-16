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
    private boolean displaySetting = true;
    private boolean lightTheme = true;

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
        setupChips();
    }

    private void init(){
        preferences = getContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        saveRepeatMode = preferences.getBoolean("saveRepeatMode", false);
        displaySetting = preferences.getBoolean("GridLayout", true);
        lightTheme = preferences.getBoolean("LightTheme", true);
    }

    private void setupView(){
        binding.switch2.setChecked(saveRepeatMode);
        binding.switch2.setOnCheckedChangeListener((buttonView, isChecked) -> saveRepeatMode = isChecked);
    }

    private void setupChips(){
        if (displaySetting)
            binding.chipGroup.check(R.id.multiLineChip);
        else
            binding.chipGroup.check(R.id.singleLineChip);
        binding.chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.multiLineChip)
                displaySetting = true;
            else
                displaySetting = false;
        });
        if (lightTheme)
            binding.themeChipGroup.check(R.id.lightThemeChip);
        else
            binding.themeChipGroup.check(R.id.darkThemeChip);
        binding.themeChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.lightThemeChip)
                lightTheme = true;
            else
                lightTheme = false;
            getActivity().recreate();
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        preferences.edit()
                .putBoolean("saveRepeatMode", saveRepeatMode)
                .putBoolean("GridLayout", displaySetting)
                .putBoolean("LightTheme", lightTheme)
                .apply();
    }
}
