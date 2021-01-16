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
import android.support.v4.media.session.MediaControllerCompat;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.lebogang.audiofilemanager.AudioManagement.AudioCallbacks;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.Adapters.SongsAdapter;
import com.lebogang.kxgenesis.AppUtils.SongClickListener;
import com.lebogang.kxgenesis.AppUtils.SongDeleteListener;
import com.lebogang.kxgenesis.Dialogs.SongsOptionsDialog;
import com.lebogang.kxgenesis.MainActivity;
import com.lebogang.kxgenesis.AppUtils.AudioIndicator;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.ViewModels.AudioViewModel;
import com.lebogang.kxgenesis.databinding.FragmentLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class SongsFragments extends Fragment implements SongClickListener, AudioCallbacks, SongDeleteListener,
        PopupMenu.OnMenuItemClickListener, DefaultLifecycleObserver {

    private FragmentLayoutBinding binding;
    private AudioViewModel viewModel;
    private final SongsAdapter songsAdapter = new SongsAdapter();

    public SongsFragments() {
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLayoutBinding.inflate(inflater);
        initRecyclerView();
        initOtherViews();
        viewModel = new AudioViewModel.AudioViewModelFactory(getContext()).create(AudioViewModel.class);
        viewModel.registerCallback(this,this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addObserver();
    }

    private void initOtherViews(){
        binding.customToolbarTextView1.setText("All");
        binding.customToolbarTextView2.setText("Songs");
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
        songsAdapter.setSongClickListener(this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.getItemAnimator().setAddDuration(500);
        binding.recyclerView.getItemAnimator().setMoveDuration(500);
        binding.recyclerView.getItemAnimator().setRemoveDuration(500);
        binding.recyclerView.getItemAnimator().setChangeDuration(500);
        binding.recyclerView.setAdapter(songsAdapter);
    }

    private void addObserver(){
        AudioIndicator.getCurrentItem().observe(getViewLifecycleOwner(),mediaItem -> {
            int color = AudioIndicator.Colors.getPrimaryColor();
            songsAdapter.setAudioIdHighlightingColor(mediaItem.getId(), color);
            int position = songsAdapter.getAudioPosition(mediaItem);
            binding.recyclerView.scrollToPosition(position);
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
    public void onQueryComplete(List<Audio> audioList) {
        songsAdapter.setList(new ArrayList<>(audioList));
    }

    @Override
    public void onClick(Audio audio) {
        MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(getActivity());
        if (mediaControllerCompat != null){
            Bundle bundle = new Bundle();
            bundle.putParcelable("Item", audio);
            bundle.putParcelableArrayList("List", songsAdapter.getList());
            mediaControllerCompat.getTransportControls().playFromUri(audio.getUri(), bundle);
            ((MainActivity)getActivity()).setPagerData(songsAdapter.getList());
        }
    }

    @Override
    public void onClickOptions(Audio audio) {
        SongsOptionsDialog songsOptionsDialog = new SongsOptionsDialog((MainActivity)getActivity(), viewModel.getAudioManager());
        songsOptionsDialog.setSongDeleteListener(this);
        songsOptionsDialog.createDialog(audio);
    }

    @Override
    public void onDelete(Audio audio) {
        songsAdapter.deleteAudio(audio);
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

}
