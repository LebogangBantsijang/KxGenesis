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
        binding.chipGroup.check(preferences.isDisplayGrid()? R.id.multiLineChip: R.id.singleLineChip);
        binding.chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.multiLineChip)
                preferences.setDisplayGrid(true);
            else
                preferences.setDisplayGrid(false);
        });
    }

    private void initThemeChips(){
        binding.themeSwitch.setChecked(preferences.isThemeLight());
        binding.themeSwitch.setOnCheckedChangeListener((buttonView, isChecked)-> preferences.setThemeLight(isChecked));

        switch (preferences.getThemeIndex()){
            case 0 :
                binding.themeColorChipGroup.check(R.id.colorOneChip);
                break;
            case 1 :
                binding.themeColorChipGroup.check(R.id.colorTwoChip);
                break;
            case 2 :
                binding.themeColorChipGroup.check(R.id.colorThreeChip);
                break;
            case 3 :
                binding.themeColorChipGroup.check(R.id.colorFourChip);
                break;
            case 4:
                binding.themeColorChipGroup.check(R.id.colorFiveChip);
                break;
        }

        binding.themeColorChipGroup.setOnCheckedChangeListener(((group, checkedId) -> {
            switch (checkedId){
                case R.id.colorOneChip :
                    preferences.setThemeIndex(0);
                    break;
                case R.id.colorTwoChip :
                    preferences.setThemeIndex(1);
                    break;
                case R.id.colorThreeChip :
                    preferences.setThemeIndex(2);
                    break;
                case R.id.colorFourChip :
                    preferences.setThemeIndex(3);
                    break;
                case R.id.colorFiveChip :
                    preferences.setThemeIndex(4);
                    break;
            }
        }));
    }

}
