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

package com.lebogang.kxgenesis.Dialogs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lebogang.audiofilemanager.AlbumManagement.AlbumManager;
import com.lebogang.audiofilemanager.ArtistManagement.ArtistManager;
import com.lebogang.audiofilemanager.AudioManagement.AudioManager;
import com.lebogang.audiofilemanager.Models.Album;
import com.lebogang.audiofilemanager.Models.Artist;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.Utils.TimeUnitConvert;
import com.lebogang.kxgenesis.databinding.LayoutSongOptionsBinding;

public class AudioOptions {
    private AudioManager audioFileManger;
    private AlbumManager albumFileManager;
    private ArtistManager artistFileManager;
    private LayoutSongOptionsBinding binding;
    private AlertDialog dialog;
    private NavController navController;
    private final Context context;

    public AudioOptions(Context context, NavController navController) {
        this.context = context;
        this.audioFileManger = new AudioManager(context);
        this.navController = navController;
        albumFileManager = new AlbumManager(context);
        artistFileManager = new ArtistManager(context);
    }

    public void createDialog(Audio audioItem){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = LayoutSongOptionsBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        builder.setNegativeButton("Cancel", null);
        setupView(context,audioItem);
        dialog = builder.create();
        dialog.show();
    }

    private void setupView(Context context,Audio audioItem){
        Glide.with(context).load(audioItem.getAlbumArtUri())
                .error(R.drawable.ic_music_light)
                .into(binding.imageView);
        binding.titleTextView.setText(audioItem.getTitle());
        binding.subtitleTextView.setText(audioItem.getArtistTitle() + " - " + audioItem.getAlbumTitle());
        String dur = TimeUnitConvert.toMinutes(audioItem.getAudioDuration());
        binding.durationTextView.setText(dur);
        initOptions(audioItem);
    }

    private void initOptions(Audio audioItem){
        binding.goToAlbum.setOnClickListener(v->{
            Album item = albumFileManager.getAlbumItemWithName(audioItem.getAlbumTitle());
            Bundle bundle = new Bundle();
            bundle.putParcelable("Album", item);
            dialog.dismiss();
            navController.navigate(R.id.album_view_fragment, bundle);
        });
        binding.goToArtist.setOnClickListener(v->{
            Artist item = artistFileManager.getArtistItemWithName(audioItem.getArtistTitle());
            Bundle bundle = new Bundle();
            bundle.putParcelable("Artist", item);
            dialog.dismiss();
            navController.navigate(R.id.artist_view_fragment, bundle);
        });
        binding.share.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("audio/*");
            intent.putExtra(Intent.EXTRA_STREAM, audioItem.getUri());
            context.startActivity(Intent.createChooser(intent,"Share Song"));
        });
        binding.delete.setOnClickListener(v->{
            boolean result = audioFileManger.deleteAudio(audioItem.getId());
            if (result)
                dialog.dismiss();
        });
        binding.editTags.setOnClickListener(v->{
            AudioEdit audioEdit = new AudioEdit(context);
            audioEdit.setAudioFileManger(audioFileManger);
            audioEdit.createDialog(audioItem);

        });
        binding.addToPlaylist.setOnClickListener(v->{
            new AudioToPlaylistDialog(context).createDiaLog(audioItem);
        });
        binding.info.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putParcelable("Item", audioItem);
            dialog.dismiss();
            navController.navigate(R.id.audio_info_fragment, bundle);
        });
    }
}
