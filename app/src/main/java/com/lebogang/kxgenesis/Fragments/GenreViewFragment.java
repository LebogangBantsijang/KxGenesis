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
import android.support.v4.media.session.MediaControllerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.lebogang.audiofilemanager.AudioManagement.AudioCallbacks;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.audiofilemanager.Models.Genre;
import com.lebogang.kxgenesis.Adapters.SongsAdapter;
import com.lebogang.kxgenesis.AppUtils.SongClickListener;
import com.lebogang.kxgenesis.AppUtils.SongDeleteListener;
import com.lebogang.kxgenesis.Dialogs.SongsDialog;
import com.lebogang.kxgenesis.MainActivity;
import com.lebogang.kxgenesis.AppUtils.AudioIndicator;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.ViewModels.AudioViewModel;
import com.lebogang.kxgenesis.databinding.FragmentGenreViewBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GenreViewFragment extends Fragment implements SongClickListener, SongDeleteListener, AudioCallbacks {
    private FragmentGenreViewBinding binding;
    private final SongsAdapter songsAdapter = new SongsAdapter();
    private Genre genre;
    private AudioViewModel viewModel;

    public GenreViewFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGenreViewBinding.inflate(inflater, container, false);
        genre = getArguments().getParcelable("Genre");
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(AudioViewModel.class);
        viewModel.init(getContext());
        viewModel.registerCallbacksForAudioIds(this, this, genre.getAudioIds());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupMediaItemDetails();
        setupRecyclerView();
        initOtherViews();
        observer();
    }

    private void initOtherViews(){
        binding.backButton.setOnClickListener(v->{
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_host);
            navController.navigateUp();
        });
        binding.expandView.setOnClickListener(v->{
            ((MainActivity)requireActivity()).expandBottomSheets();
        });
    }

    private void setupMediaItemDetails(){
        binding.titleTextView.setText(genre.getTitle());
    }

    private void setupRecyclerView(){
        songsAdapter.setSongClickListener(this);
        songsAdapter.setHideOptionsButton(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(songsAdapter);
    }

    private void observer(){
        AudioIndicator.getCurrentItem().observe(getViewLifecycleOwner(), mediaItem -> {
            int color = AudioIndicator.Colors.getDefaultColor();
            songsAdapter.setAudioIdHighlightingColor(mediaItem.getId(), color);
            int position = songsAdapter.getAudioPosition(mediaItem);
            binding.recyclerView.scrollToPosition(position);
            binding.expandView.setVisibility(View.VISIBLE);
            binding.titleTextTextSheet.setText(mediaItem.getTitle());
            binding.subTitleTextViewSheet.setText(mediaItem.getAlbumTitle());
            Glide.with(this)
                    .load(mediaItem.getAlbumArtUri())
                    .error(R.drawable.ic_music_light)
                    .dontAnimate()
                    .into(binding.coverImageView)
                    .waitForLayout();
        });
    }

    @Override
    public void onQueryComplete(List<Audio> audioList) {
        songsAdapter.setList(new ArrayList<>(audioList));
        binding.numSongsTextView.setText("song count: " + audioList.size());
        int duration = 0;
        for (Audio audio:audioList){
            duration += audio.getAudioDuration();
        }
        binding.durationTextView.setText("duration: " + TimeUnit.MILLISECONDS.toMinutes(duration) + "min");
    }

    @Override
    public void onClick(Audio audio) {
        MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(getActivity());
        if (mediaControllerCompat != null){
            Bundle bundle = new Bundle();
            bundle.putParcelable("Item", audio);
            bundle.putParcelableArrayList("List",songsAdapter.getList());
            mediaControllerCompat.getTransportControls().playFromUri(audio.getUri(), bundle);
            ((MainActivity)getActivity()).setPagerData(songsAdapter.getList());
        }
    }

    @Override
    public void onClickOptions(Audio audio) {
        SongsDialog songsDialog = new SongsDialog((MainActivity)getActivity(), viewModel.getAudioManager());
        songsDialog.setSongDeleteListener(this);
        songsDialog.createDialog(audio);
    }

    @Override
    public void onDelete(Audio audio) {
        songsAdapter.deleteAudio(audio);
    }
}
