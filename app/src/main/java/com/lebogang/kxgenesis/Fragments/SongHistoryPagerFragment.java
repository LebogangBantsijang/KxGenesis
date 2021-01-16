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
import android.support.v4.media.session.MediaControllerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.Adapters.SongHistoryAdapter;
import com.lebogang.kxgenesis.AppUtils.AudioIndicator;
import com.lebogang.kxgenesis.AppUtils.OnGetSongHistory;
import com.lebogang.kxgenesis.AppUtils.SongClickListener;
import com.lebogang.kxgenesis.MainActivity;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.Room.Model.SongHistory;
import com.lebogang.kxgenesis.ViewModels.HistoryViewModel;
import com.lebogang.kxgenesis.databinding.LayoutRecyclerviewBinding;

public class SongHistoryPagerFragment extends Fragment implements OnGetSongHistory, SongClickListener {
    private LayoutRecyclerviewBinding binding;
    private final SongHistoryAdapter adapter = new SongHistoryAdapter();
    private HistoryViewModel viewModel;

    public SongHistoryPagerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LayoutRecyclerviewBinding.inflate(inflater, container, false);
        viewModel = new HistoryViewModel.HistoryFactory(getContext()).create(HistoryViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        getSongHistory();
        addObserver();
    }

    private void initRecyclerView(){
        adapter.setSongClickListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void getSongHistory(){
        viewModel.getSongHistory(getViewLifecycleOwner(), this);
    }

    private void addObserver(){
        AudioIndicator.getCurrentItem().observe(getViewLifecycleOwner(), mediaItem -> {
            int color = AudioIndicator.Colors.getPrimaryColor();
            adapter.setAudioIdHighlightingColor(mediaItem.getId(), color);
            int position = adapter.getAudioPosition(mediaItem);
            binding.recyclerView.scrollToPosition(position);
        });
    }

    @Override
    public void onGetSongHistory(SongHistory songHistory) {
        adapter.addSongHistory(songHistory);
    }

    @Override
    public void resetSongData() {
        adapter.clear();
    }

    @Override
    public void onClick(Audio audio) {
        MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(getActivity());
        if (mediaControllerCompat != null){
            Bundle bundle = new Bundle();
            bundle.putParcelable("Item", audio);
            bundle.putParcelableArrayList("List", adapter.getList());
            mediaControllerCompat.getTransportControls().playFromUri(audio.getUri(), bundle);
            ((MainActivity)getActivity()).setPagerData(adapter.getList());
        }
    }

    @Override
    public void onClickOptions(Audio audio) {

    }
}
