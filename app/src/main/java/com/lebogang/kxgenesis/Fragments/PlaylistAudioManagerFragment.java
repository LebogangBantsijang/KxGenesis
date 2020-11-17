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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lebogang.audiofilemanager.AudioManagement.AudioCallbacks;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.audiofilemanager.Models.Playlist;
import com.lebogang.audiofilemanager.PlaylistManagement.PlaylistManager;
import com.lebogang.kxgenesis.Adapters.PlaylistAudioManagerAdapter;
import com.lebogang.kxgenesis.ViewModels.AudioViewModel;
import com.lebogang.kxgenesis.databinding.FragmentPlaylistItemManagerBinding;

import java.util.HashMap;
import java.util.List;

public class PlaylistAudioManagerFragment extends Fragment implements AudioCallbacks {
    private FragmentPlaylistItemManagerBinding binding;
    private Playlist playlist;
    private PlaylistAudioManagerAdapter adapter = new PlaylistAudioManagerAdapter();
    private AudioViewModel viewModel;

    public PlaylistAudioManagerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlaylistItemManagerBinding.inflate(inflater, container, false);
        playlist = getArguments().getParcelable("Playlist");
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(AudioViewModel.class);
        viewModel.init(getContext());
        viewModel.registerCallbacksForAudioIds(this,this,playlist.getAudioIds());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        setupDeleteBtn();
    }

    private void setupRecyclerView(){
        binding.titleTextView.setText(playlist.getTitle());
        adapter.setContext(getContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void setupDeleteBtn(){
        binding.saveButton.setOnClickListener(v->{
            HashMap<Long, Audio> map = adapter.getCheckedItems();
            PlaylistManager playlistManager = new PlaylistManager(getContext());
            map.forEach((aLong, audio) -> {
                boolean b = playlistManager.deleteAudioFromPlaylist(playlist.getId(), audio.getId());
                if (b)
                    adapter.removeItem(audio);
            });
        });
    }

    @Override
    public void onQueryComplete(List<Audio> audioList) {
        adapter.setList(audioList);
    }
}
