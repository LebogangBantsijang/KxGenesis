/*
 * Copyright (c) 2021. Lebogang Bantsijang
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

package com.lebogang.kxgenesis.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lebogang.kxgenesis.AppUtils.AppSettings;
import com.lebogang.kxgenesis.MainActivity;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.FragmentThemeSettingsBinding;

public class ThemeSettingsFragment extends Fragment {
    private FragmentThemeSettingsBinding binding;

    public ThemeSettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentThemeSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initThemeViews();
        initOtherViews();
        initThemeColorsViews();
    }

    private void initOtherViews(){
        binding.backButton.setOnClickListener(v->{
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_host);
            navController.navigateUp();
        });
    }

    private void initThemeViews(){
        boolean value = AppSettings.isThemeLight(requireContext());
        binding.themeCheckBox.setChecked(value);
        binding.themeCheckBox.setOnCheckedChangeListener((buttonView, isChecked)-> {
            AppSettings.setLightTheme(requireContext(), isChecked);
        });
    }

    private void initThemeColorsViews(){
        int themeIndex = AppSettings.getThemeIndex(requireContext());
        switch (themeIndex){
            case 0:
                binding.themeColorChipGroup.check(R.id.colorOneChip);
                break;
            case 1:
                binding.themeColorChipGroup.check(R.id.colorTwoChip);
                break;
            case 2:
                binding.themeColorChipGroup.check(R.id.colorThreeChip);
                break;
            case 3:
                binding.themeColorChipGroup.check(R.id.colorFourChip);
                break;
            case 4:
                binding.themeColorChipGroup.check(R.id.colorFiveChip);
                break;
        }
        binding.themeColorChipGroup.setOnCheckedChangeListener(((group, checkedId) -> {
            switch (checkedId){
                case R.id.colorOneChip :
                    AppSettings.setThemeIndex(requireContext(), 0);
                    break;
                case R.id.colorTwoChip :
                    AppSettings.setThemeIndex(requireContext(), 1);
                    break;
                case R.id.colorThreeChip:
                    AppSettings.setThemeIndex(requireContext(), 2);
                    break;
                case R.id.colorFourChip :
                    AppSettings.setThemeIndex(requireContext(), 3);
                    break;
                case R.id.colorFiveChip :
                    AppSettings.setThemeIndex(requireContext(), 4);
                    break;
            }
        }));
    }
}