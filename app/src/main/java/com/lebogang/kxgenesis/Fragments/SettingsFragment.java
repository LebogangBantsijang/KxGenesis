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

package com.lebogang.kxgenesis.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.lebogang.kxgenesis.AppUtils.AppSettings;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;

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
        initRepeatSwitch();
        initDisplayChips();
        initThemeChips();
        initOtherViews();
        initPlayerSpinner();
    }

    private void initOtherViews(){
        binding.backButton.setOnClickListener(v->{
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_host);
            navController.navigateUp();
        });
    }

    private void initRepeatSwitch(){
        boolean value = AppSettings.isRepeatModeSaved(requireContext());
        binding.repeatSwitch.setChecked(value);
        binding.repeatSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> AppSettings.setRepeatModeSaved(requireContext(), isChecked));
    }

    private void initDisplayChips(){
        boolean value = AppSettings.displayGrid(requireContext());
        binding.chipGroup.check(value? R.id.multiLineChip: R.id.singleLineChip);
        binding.chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            AppSettings.setDisplayGrid(requireContext(), checkedId == R.id.multiLineChip);
        });
    }

    private void initThemeChips(){
        boolean value = AppSettings.isThemeLight(requireContext());
        binding.themeSwitch.setChecked(value);
        binding.themeSwitch.setOnCheckedChangeListener((buttonView, isChecked)-> AppSettings.setLightTheme(requireContext(), isChecked));
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
                case R.id.colorOneChip :AppSettings.setThemeIndex(requireContext(), 0);
                    break;
                case R.id.colorTwoChip :AppSettings.setThemeIndex(requireContext(), 1);
                    break;
                case R.id.colorThreeChip :AppSettings.setThemeIndex(requireContext(), 2);
                    break;
                case R.id.colorFourChip :AppSettings.setThemeIndex(requireContext(), 3);
                    break;
                case R.id.colorFiveChip :AppSettings.setThemeIndex(requireContext(), 4);
                    break;
            }
        }));
    }

    private void initPlayerSpinner(){
        binding.spinner.setSelection(AppSettings.getSelectedPlayerIndex(requireContext()));

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppSettings.setPlayer(requireContext(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
