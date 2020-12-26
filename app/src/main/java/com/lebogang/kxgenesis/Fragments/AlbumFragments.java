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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.lebogang.audiofilemanager.AlbumManagement.AlbumCallbacks;
import com.lebogang.audiofilemanager.AlbumManagement.AlbumManager;
import com.lebogang.audiofilemanager.Models.Album;
import com.lebogang.kxgenesis.Adapters.AlbumAdapter;
import com.lebogang.kxgenesis.AppUtils.AlbumClickListener;
import com.lebogang.kxgenesis.AppUtils.AppSettings;
import com.lebogang.kxgenesis.AppUtils.AudioIndicator;
import com.lebogang.kxgenesis.MainActivity;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.ViewModels.AlbumViewModel;
import com.lebogang.kxgenesis.databinding.FragmentLayoutBinding;

import java.util.List;

public class AlbumFragments extends Fragment implements AlbumClickListener, AlbumCallbacks, PopupMenu.OnMenuItemClickListener {

    private FragmentLayoutBinding binding;
    private final AlbumAdapter adapter = new AlbumAdapter();
    private AlbumViewModel viewModel;

    public AlbumFragments() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLayoutBinding.inflate(inflater);
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(AlbumViewModel.class);
        viewModel.init(getContext());
        viewModel.registerCallbacks(this, getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        initOtherViews();
        observer();
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

    private void initRecyclerView(){
        boolean displayGrid = AppSettings.displayGrid(requireContext());
        adapter.setAlbumClickListener(this);
        adapter.setLayoutGrid(displayGrid);
        if (displayGrid)
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        else
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void observer(){
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

    @Override
    public void onQueryComplete(List<Album> albumList) {
        adapter.setList(AlbumManager.groupByName(albumList));
    }

    @Override
    public void onClick(Album album) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_host);
        Bundle bundle = new Bundle();
        bundle.putParcelable("Album", album);
        navController.navigate(R.id.album_view_fragment, bundle);
    }

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
            default:
                return false;
        }
    }
}
