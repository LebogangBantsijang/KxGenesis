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
import com.lebogang.audiofilemanager.ArtistManagement.ArtistManager;
import com.lebogang.audiofilemanager.AudioManagement.AudioCallbacks;
import com.lebogang.audiofilemanager.Models.Album;
import com.lebogang.audiofilemanager.Models.Artist;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.Adapters.ContributingArtistAdapter;
import com.lebogang.kxgenesis.Adapters.SongsAdapter;
import com.lebogang.kxgenesis.AppUtils.SongClickListener;
import com.lebogang.kxgenesis.AppUtils.SongDeleteListener;
import com.lebogang.kxgenesis.Dialogs.SongsDialog;
import com.lebogang.kxgenesis.MainActivity;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.AppUtils.AudioIndicator;
import com.lebogang.kxgenesis.ViewModels.AudioViewModel;
import com.lebogang.kxgenesis.databinding.FragmentAlbumViewBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AlbumViewFragment extends Fragment implements SongClickListener, SongDeleteListener, AudioCallbacks {
    private FragmentAlbumViewBinding binding;
    private final SongsAdapter songsAdapter = new SongsAdapter();
    private final ContributingArtistAdapter contributingArtistAdapter = new ContributingArtistAdapter();

    private Album album;
    private AudioViewModel viewModel;

    public AlbumViewFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAlbumViewBinding.inflate(inflater, container, false);
        album = getArguments().getParcelable("Album");
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(AudioViewModel.class);
        viewModel.init(getContext());
        viewModel.registerCallbacksForAlbumAudio(this, this, album.getTitle());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupMediaItemDetails();
        initSongsRecyclerView();
        initBottomRecyclerView();
        initOtherViews();
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
        binding.titleTextView.setText(album.getTitle());
        binding.subtitleTextView.setText(album.getArtist());
        Glide.with(this).load(album.getAlbumArtUri())
                .error(R.drawable.ic_music_record_light)
                .into(binding.imageView);
    }

    private void initSongsRecyclerView(){
        songsAdapter.setSongClickListener(this);
        songsAdapter.setHideOptionsButton(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(songsAdapter);
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

    private void initBottomRecyclerView(){
        binding.recyclerViewArtists.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL
                ,false));
        binding.recyclerViewArtists.setAdapter(contributingArtistAdapter);
        contributingArtistAdapter.setSongClickListener(new SongClickListener() {
            @Override
            public void onClick(Audio audio) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_host);
                ArtistManager artistManager = new ArtistManager(getContext());
                Artist artist = artistManager.getArtistItemWithName(audio.getArtistTitle());
                Bundle bundle = new Bundle();
                bundle.putParcelable("Artist", artist);
                navController.navigate(R.id.artist_view_fragment, bundle);
            }
            @Override
            public void onClickOptions(Audio audio) {
            }
        });
    }

    @Override
    public void onQueryComplete(List<Audio> audioList) {
        songsAdapter.setList(new ArrayList<>(audioList));
        initAlbumDetails(audioList);
    }

    private void initAlbumDetails(List<Audio> audioList){
        binding.numSongsTextView.setText("song count: " + audioList.size());
        int duration = 0;
        for (Audio audio:audioList){
            duration += audio.getAudioDuration();
        }
        binding.durationTextView.setText("duration: " + TimeUnit.MILLISECONDS.toMinutes(duration) + "min");
        contributingArtistAdapter.setList(audioList);
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
