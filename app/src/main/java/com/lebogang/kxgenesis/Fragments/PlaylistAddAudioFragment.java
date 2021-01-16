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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lebogang.audiofilemanager.AudioManagement.AudioCallbacks;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.Adapters.PlaylistAddSongsAdapter;
import com.lebogang.kxgenesis.AppUtils.SongClickListener;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.Room.Model.PlaylistAudio;
import com.lebogang.kxgenesis.Room.Model.PlaylistDetails;
import com.lebogang.kxgenesis.ViewModels.PlaylistViewModel;
import com.lebogang.kxgenesis.databinding.FragmentPlaylistAddAudioBinding;

import java.util.List;

public class PlaylistAddAudioFragment extends Fragment implements AudioCallbacks, SongClickListener {
    private FragmentPlaylistAddAudioBinding binding;
    private final PlaylistAddSongsAdapter adapter = new PlaylistAddSongsAdapter();
    private PlaylistDetails playlist;
    private PlaylistViewModel viewModel;

    public PlaylistAddAudioFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlaylistAddAudioBinding.inflate(inflater, container, false);
        playlist = getArguments().getParcelable("Playlist");
        viewModel = new PlaylistViewModel.PlaylistViewModelFactory(getContext()).create(PlaylistViewModel.class);
        viewModel.getAudio(getViewLifecycleOwner(), this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        initOtherViews();
        initSearchViews();
    }

    private void initOtherViews(){
        binding.playlistNameTextView.setText(playlist.getPlaylistName());
        binding.backButton.setOnClickListener(v->{
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_host);
            navController.navigateUp();
        });
    }

    private void setupRecyclerView(){
        adapter.setHideOptionsButton(true);
        adapter.setSongClickListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void initSearchViews(){
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onQueryComplete(List<Audio> audioList) {
        adapter.setList(audioList);
    }

    @Override
    public void onClick(Audio audio) {
        PlaylistAudio playlistAudio = new PlaylistAudio(0, playlist.getId(), audio.getId());
        viewModel.addPlaylistAudio(playlistAudio);
        Toast.makeText(getContext(), "Added", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClickOptions(Audio audio) {
    }
}
