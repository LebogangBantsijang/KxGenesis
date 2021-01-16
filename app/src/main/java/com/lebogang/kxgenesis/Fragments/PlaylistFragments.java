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
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.lebogang.kxgenesis.Adapters.PlaylistAdapter;
import com.lebogang.kxgenesis.AppUtils.AudioIndicator;
import com.lebogang.kxgenesis.AppUtils.PlaylistClickListener;
import com.lebogang.kxgenesis.Dialogs.PlaylistCreateUpdate;
import com.lebogang.kxgenesis.Dialogs.PlaylistOptionsDialog;
import com.lebogang.kxgenesis.MainActivity;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.Room.Model.PlaylistDetails;
import com.lebogang.kxgenesis.ViewModels.PlaylistViewModel;
import com.lebogang.kxgenesis.databinding.FragmentPlaylistsBinding;

import java.util.ArrayList;
import java.util.List;

public class PlaylistFragments extends Fragment implements PlaylistClickListener, PopupMenu.OnMenuItemClickListener {
    private FragmentPlaylistsBinding binding;
    private final PlaylistAdapter adapter = new PlaylistAdapter();

    private PlaylistViewModel viewModel;

    public PlaylistFragments() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlaylistsBinding.inflate(inflater);
        viewModel = new PlaylistViewModel.PlaylistViewModelFactory(getContext()).create(PlaylistViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        getPlaylists();
        initOtherViews();
        addObserver();
    }

    private void initRecyclerView(){
        adapter.setPlaylistClickListener(this);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        binding.recyclerView.setAdapter(adapter);
    }

    private void getPlaylists(){
        viewModel.getPlaylist().observe(getViewLifecycleOwner(), playlistDetails -> {
            adapter.setList(playlistDetails);
        });
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
        binding.newPlaylistButton.setOnClickListener(v->{
            PlaylistCreateUpdate playlistCreateUpdate = new PlaylistCreateUpdate(getContext(), viewModel);
            playlistCreateUpdate.createDialog();
        });
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

    @Override
    public void onClick(PlaylistDetails playlist) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_host);
        Bundle bundle = new Bundle();
        bundle.putParcelable("Playlist", playlist);
        navController.navigate(R.id.playlist_view_fragment, bundle);
    }

    @Override
    public void onClickOptions(PlaylistDetails playlist) {
        PlaylistOptionsDialog playlistOptionsDialog = new PlaylistOptionsDialog(getContext(), viewModel);
        playlistOptionsDialog.createDialog(playlist);
    }
}
