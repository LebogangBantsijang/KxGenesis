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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.lebogang.audiofilemanager.GenreManagement.GenreCallbacks;
import com.lebogang.audiofilemanager.Models.Genre;
import com.lebogang.kxgenesis.Adapters.GenreAdapter;
import com.lebogang.kxgenesis.AppUtils.AppSettings;
import com.lebogang.kxgenesis.AppUtils.AudioIndicator;
import com.lebogang.kxgenesis.AppUtils.GenreClickListener;
import com.lebogang.kxgenesis.MainActivity;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.ViewModels.GenreViewModel;
import com.lebogang.kxgenesis.databinding.FragmentLayoutBinding;
import com.lebogang.kxgenesis.databinding.FragmentLayoutOtherBinding;

import java.util.List;

public class GenreFragments extends Fragment implements GenreClickListener, GenreCallbacks {
    private FragmentLayoutOtherBinding binding;
    private final GenreAdapter adapter = new GenreAdapter();
    private GenreViewModel viewModel;

    public GenreFragments() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLayoutOtherBinding.inflate(inflater, container, false);
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(GenreViewModel.class);
        viewModel.init(getContext());
        viewModel.registerCallbacks(this,this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        initOtherViews();
        addObserver();
    }

    private void initOtherViews(){
        binding.backButton.setOnClickListener(v->{
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_host);
            navController.navigateUp();
        });
        binding.expandView.setOnClickListener(v->{
            ((MainActivity) requireActivity()).expandBottomSheets();
        });
    }

    private void initRecyclerView(){
        adapter.setGenreClickListener(this);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.recyclerView.setAdapter(adapter);
    }

    private void addObserver(){
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

    private void removeObserver(){
        AudioIndicator.getCurrentItem().removeObservers(getViewLifecycleOwner());
    }

    @Override
    public void onQueryComplete(List<Genre> genreList) {
        adapter.setList(genreList);
    }

    @Override
    public void onClick(Genre genre) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_host);
        Bundle bundle = new Bundle();
        bundle.putParcelable("Genre", genre);
        navController.navigate(R.id.genre_view_fragment, bundle);
    }
}
