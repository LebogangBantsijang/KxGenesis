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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.lebogang.audiofilemanager.Models.Media;
import com.lebogang.audiofilemanager.Models.Playlist;
import com.lebogang.audiofilemanager.PlaylistManagement.PlaylistCallbacks;
import com.lebogang.kxgenesis.Adapters.OnClickInterface;
import com.lebogang.kxgenesis.Adapters.OnClickOptionsInterface;
import com.lebogang.kxgenesis.Adapters.PlaylistAdapter;
import com.lebogang.kxgenesis.Dialogs.PlaylistCreateUpdate;
import com.lebogang.kxgenesis.Dialogs.PlaylistOptions;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.ViewModels.PlaylistViewModel;
import com.lebogang.kxgenesis.databinding.FragmentPlaylistsBinding;

import java.util.List;

public class PlaylistFragments extends Fragment implements OnClickInterface, OnClickOptionsInterface, PlaylistCallbacks {
    private FragmentPlaylistsBinding binding;
    private PlaylistAdapter adapter = new PlaylistAdapter(this, this);
    private PlaylistViewModel viewModel;

    public PlaylistFragments() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlaylistsBinding.inflate(inflater);
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(PlaylistViewModel.class);
        viewModel.init(getContext());
        viewModel.registerCallbacks(this, this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        initOtherViews();
    }

    private void initRecyclerView(){
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        binding.recyclerView.setAdapter(adapter);
    }

    private void initOtherViews(){
        binding.newPlaylistButton.setOnClickListener(v->{
            PlaylistCreateUpdate playlistCreateUpdate = new PlaylistCreateUpdate(getContext());
            playlistCreateUpdate.setPlaylistFileManager(viewModel.getPlaylistManager());
            playlistCreateUpdate.createDialog();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onQueryComplete(List<Playlist> playlists) {
        adapter.setList(playlists);
    }

    @Override
    public void onClick(Media media) {
        Playlist playlist = (Playlist) media;
        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_host);
        Bundle bundle = new Bundle();
        bundle.putParcelable("Playlist", playlist);
        navController.navigate(R.id.playlist_view_fragment, bundle);
    }

    @Override
    public void onOptionsClick(Media media) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_host);
        PlaylistOptions playlistOptions = new PlaylistOptions(getContext(), navController);
        playlistOptions.setPlaylistFileManager(viewModel.getPlaylistManager());
        playlistOptions.createDialog((Playlist) media);
    }
}
