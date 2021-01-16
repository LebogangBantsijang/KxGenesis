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

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaControllerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.lebogang.audiofilemanager.AudioManagement.AudioCallbacks;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.Adapters.PlaylistSongsAdapter;
import com.lebogang.kxgenesis.AppUtils.SongClickListener;
import com.lebogang.kxgenesis.AppUtils.SongDeleteListener;
import com.lebogang.kxgenesis.MainActivity;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.AppUtils.AudioIndicator;
import com.lebogang.kxgenesis.Room.Model.PlaylistDetails;
import com.lebogang.kxgenesis.ViewModels.PlaylistViewModel;
import com.lebogang.kxgenesis.databinding.FragmentPlaylistViewBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlaylistAudioViewFragment extends Fragment implements SongClickListener, SongDeleteListener, AudioCallbacks {
    private FragmentPlaylistViewBinding binding;
    private final PlaylistSongsAdapter playlistSongsAdapter = new PlaylistSongsAdapter();
    private PlaylistDetails playlist;
    private PlaylistViewModel viewModel;
    private ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null){
                    playlist.setArtUri(uri.toString());
                    viewModel.updatePlaylist(playlist);
                    setupMediaItemDetails();
                }
            });

    public PlaylistAudioViewFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlaylistViewBinding.inflate(inflater, container, false);
        playlist = getArguments().getParcelable("Playlist");
        viewModel = new PlaylistViewModel.PlaylistViewModelFactory(getContext()).create(PlaylistViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupMediaItemDetails();
        setupRecyclerView();
        initOtherViews();
        getAudio();
        addObserver();
        initAddImageView();
        initAddAudioViews();
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
        binding.titleTextView.setText(playlist.getPlaylistName());
        binding.subtitleTextView.setText("Last Updated :" + playlist.getDateCreated());
        Uri uri = Uri.parse(playlist.getArtUri());
        Glide.with(this)
                .load(uri)
                .error(R.drawable.ic_playlist)
                .dontAnimate()
                .into(binding.playlistImageView)
                .waitForLayout();
    }

    private void setupRecyclerView(){
        playlistSongsAdapter.setSongClickListener(this);
        playlistSongsAdapter.setSongDeleteListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(playlistSongsAdapter);
    }
    
    private void initAddImageView(){
        binding.addImageButton.setOnClickListener(v->{
            mGetContent.launch("image/*");
        });   
    }

    private void initAddAudioViews(){
        binding.addAudioButton.setOnClickListener(v->{
            NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_host);
            Bundle bundle = new Bundle();
            bundle.putParcelable("Playlist", playlist);
            navController.navigate(R.id.playlist_add_audio_fragment, bundle);
        });
    }

    private void getAudio(){
        viewModel.getPlaylistAudio(getViewLifecycleOwner(), this, playlist.getId());
    }

    private void addObserver(){
        AudioIndicator.getCurrentItem().observe(getViewLifecycleOwner(), mediaItem -> {
            int color = AudioIndicator.Colors.getPrimaryColor();
            playlistSongsAdapter.setAudioIdHighlightingColor(mediaItem.getId(), color);
            int position = playlistSongsAdapter.getAudioPosition(mediaItem);
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
        playlistSongsAdapter.setList(new ArrayList<>(audioList));
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
            bundle.putParcelableArrayList("List",playlistSongsAdapter.getList());
            mediaControllerCompat.getTransportControls().playFromUri(audio.getUri(), bundle);
            ((MainActivity)getActivity()).setPagerData(playlistSongsAdapter.getList());
        }
    }

    @Override
    public void onClickOptions(Audio audio) {
    }

    @Override
    public void onDelete(Audio audio) {
        playlistSongsAdapter.deleteAudio(audio);
    }
}
