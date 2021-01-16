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

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.lebogang.kxgenesis.AppUtils.AudioIndicator;
import com.lebogang.kxgenesis.MainActivity;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.ViewModels.HistoryViewModel;
import com.lebogang.kxgenesis.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{
    private FragmentHomeBinding binding;
    private HistoryViewModel viewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        viewModel = new HistoryViewModel.HistoryFactory(requireContext()).create(HistoryViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initOtherViews();
        initButtons();
        observe();
    }

    private void initOtherViews(){
        binding.searchButton.setOnClickListener(v->{
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_host);
            navController.navigate(R.id.search_fragment);
        });
        binding.menuButton.setOnClickListener(v->{
            PopupMenu popup = new PopupMenu(getContext(), v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.toolbar_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        });
        binding.expandView.setOnClickListener(v->{
            ((MainActivity) requireActivity()).expandBottomSheets();
        });
    }

    private void initButtons(){
        binding.viewRecent.setOnClickListener(v->{
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_host);
            navController.navigate(R.id.recent_fragment);
        });
        binding.viewGenre.setOnClickListener(v->{
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_host);
            navController.navigate(R.id.genres_fragment);
        });
        binding.viewSettings.setOnClickListener(v->{
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_host);
            navController.navigate(R.id.settings_fragment);
        });
        binding.viewHistory.setOnClickListener(v->{
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_host);
            navController.navigate(R.id.history_fragment);
        });
    }

    private void observe(){
        AudioIndicator.getCurrentItem().observe(getViewLifecycleOwner(), mediaItem -> {
            binding.expandView.setVisibility(View.VISIBLE);
            binding.titleTextText.setText(mediaItem.getTitle());
            binding.subtitleTextView.setText(mediaItem.getAlbumTitle());
            Glide.with(this)
                    .load(mediaItem.getAlbumArtUri())
                    .error(R.drawable.ic_music_light)
                    .dontAnimate()
                    .into(binding.coverImageView)
                    .waitForLayout();
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_host);
        switch (item.getItemId()) {
            case R.id.menu_settings:
                navController.navigate(R.id.settings_fragment);
                return true;
            case R.id.menu_about:
                navController.navigate(R.id.about_fragment);
                return true;
            case R.id.menu_volume:
                navController.navigate(R.id.volume_fragment);
                return true;
            case R.id.menu_recent:
                navController.navigate(R.id.recent_fragment);
                return true;
            case R.id.menu_history:
                navController.navigate(R.id.history_fragment);
                return true;
            default:
                return false;
        }
    }
}