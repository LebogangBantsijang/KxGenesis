package com.lebogang.kxgenesis.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lebogang.kxgenesis.Preferences;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    //private SharedPreferences preferences;
    private Preferences preferences;

    public SettingsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        preferences = new Preferences(getContext());
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRepeatSwitch();
        initDisplayChips();
        initThemeChips();
    }

    private void initRepeatSwitch(){
        binding.repeatSwitch.setChecked(preferences.isSaveRepeatEnabled());
        binding.repeatSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> preferences.setSaveRepeatEnabled(isChecked));
    }

    private void initDisplayChips(){
        if (preferences.isDisplayGrid())
            binding.chipGroup.check(R.id.multiLineChip);
        else
            binding.chipGroup.check(R.id.singleLineChip);
        binding.chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.multiLineChip)
                preferences.setDisplayGrid(true);
            else
                preferences.setDisplayGrid(false);
        });
    }

    private void initThemeChips(){
        if (preferences.isThemeLight())
            binding.themeChipGroup.check(R.id.lightThemeChip);
        else
            binding.themeChipGroup.check(R.id.darkThemeChip);
        binding.themeChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.lightThemeChip)
                preferences.setThemeLight(true);
            else
                preferences.setThemeLight(false);
            getActivity().recreate();
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        preferences.savePreferences();
    }
}
