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
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.AppUtils.TimeUnitConvert;
import com.lebogang.kxgenesis.databinding.FragmentAudioInfoBinding;

public class AudioInfoFragment extends Fragment {

    private Audio audioMediaItem;
    private FragmentAudioInfoBinding binding;

    public AudioInfoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAudioInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        audioMediaItem = getArguments().getParcelable("Item");
        setupAllViews();
        initOtherViews();
    }

    private void initOtherViews(){
        binding.backButton.setOnClickListener(v->{
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_host);
            navController.navigateUp();
        });
    }

    private void setupAllViews(){
        Glide.with(this).load(audioMediaItem.getAlbumArtUri())
                .error(R.drawable.ic_music_light)
                .into(binding.imageView);
        binding.titleTextText.setText(audioMediaItem.getTitle());
        binding.artistTextText.setText(audioMediaItem.getArtistTitle());
        binding.albumTextText.setText(audioMediaItem.getAlbumTitle());
        binding.durationTextText.setText(TimeUnitConvert.toMinutes(audioMediaItem.getAudioDuration()));
        binding.sizeTextText.setText(Formatter.formatFileSize(getContext(),audioMediaItem.getAudioSize()));
        binding.composerTextText.setText(audioMediaItem.getComposer());
        binding.yearTextText.setText(audioMediaItem.getReleaseYear());
        binding.tackTextText.setText(audioMediaItem.getTrackNumber());
    }

}