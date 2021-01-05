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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lebogang.kxgenesis.AppUtils.AppSettings;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.FragmentDisplaySettingsBinding;

public class DisplaySettingsFragment extends Fragment {
    private FragmentDisplaySettingsBinding binding;

    public DisplaySettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDisplaySettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initOtherViews();
        initArtistDisplayViews();
        initAlbumDisplayViews();
    }

    private void initOtherViews(){
        binding.backButton.setOnClickListener(v->{
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_host);
            navController.navigateUp();
        });
    }

    private void initArtistDisplayViews(){
        boolean value = AppSettings.getArtistDisplayGrid(requireContext());
        binding.artistChipGroup.check(value? R.id.artistMultiLineChip: R.id.artistSingleLineChip);
        binding.artistChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            AppSettings.setArtistGridDisplay(requireContext(), checkedId == R.id.artistMultiLineChip);
        });
    }

    private void initAlbumDisplayViews(){
        boolean value = AppSettings.getAlbumDisplayGrid(requireContext());
        binding.albumChipGroup.check(value? R.id.albumMultiLineChip: R.id.albumSingleLineChip);
        binding.albumChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            AppSettings.setAlbumGridDisplay(requireContext(), checkedId == R.id.albumMultiLineChip);
        });
    }
}