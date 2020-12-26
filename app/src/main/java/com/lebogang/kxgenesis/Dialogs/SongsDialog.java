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

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lebogang.audiofilemanager.AlbumManagement.AlbumManager;
import com.lebogang.audiofilemanager.ArtistManagement.ArtistManager;
import com.lebogang.audiofilemanager.AudioManagement.AudioManager;
import com.lebogang.audiofilemanager.Models.Album;
import com.lebogang.audiofilemanager.Models.Artist;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.AppUtils.SongDeleteListener;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.AppUtils.TimeUnitConvert;
import com.lebogang.kxgenesis.databinding.LayoutSongOptionsBinding;

public class SongsDialog{
    private LayoutSongOptionsBinding binding;
    private AlertDialog dialog;
    private final AlbumManager albumManager;
    private final ArtistManager artistManager;
    private final AppCompatActivity activity;
    private final AudioManager audioManager;
    private SongDeleteListener songDeleteListener;

    public SongsDialog(AppCompatActivity activity, AudioManager audioManager) {
        this.activity = activity;
        this.audioManager = audioManager;
        albumManager = new AlbumManager(activity);
        artistManager = new ArtistManager(activity);
    }

    public void setSongDeleteListener(SongDeleteListener songDeleteListener) {
        this.songDeleteListener = songDeleteListener;
    }

    public void createDialog(Audio audioItem){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        binding = LayoutSongOptionsBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        builder.setNegativeButton("Cancel", null);
        setupView(audioItem);
        dialog = builder.create();
        dialog.show();
    }

    private void setupView(Audio audioItem){
        Glide.with(binding.imageView).load(audioItem.getAlbumArtUri())
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
            Album item = albumManager.getAlbumItemWithName(audioItem.getAlbumTitle());
            Bundle bundle = new Bundle();
            bundle.putParcelable("Album", item);
            dialog.dismiss();
            NavController navController = Navigation.findNavController(activity, R.id.fragment_host);
            navController.navigate(R.id.album_view_fragment, bundle);
        });
        binding.goToArtist.setOnClickListener(v->{
            Artist item = artistManager.getArtistItemWithName(audioItem.getArtistTitle());
            Bundle bundle = new Bundle();
            bundle.putParcelable("Artist", item);
            dialog.dismiss();
            NavController navController = Navigation.findNavController(activity, R.id.fragment_host);
            navController.navigate(R.id.artist_view_fragment, bundle);
        });
        binding.share.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("audio/*");
            intent.putExtra(Intent.EXTRA_STREAM, audioItem.getUri());
            activity.startActivity(Intent.createChooser(intent,"Share Song"));
        });
        binding.delete.setOnClickListener(v->{
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED){
                boolean result = audioManager.deleteAudio(audioItem.getId());
                if (result)
                    dialog.dismiss();
                songDeleteListener.onDelete(audioItem);
            }else {
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
            }
        });
        binding.editTags.setOnClickListener(v->{
            SongsEditDialog songsEditDialog = new SongsEditDialog(activity, audioManager);
            songsEditDialog.createDialog(audioItem);
        });
        binding.addToPlaylist.setOnClickListener(v->{
            SongPlaylistDialog songPlaylistDialog = new SongPlaylistDialog(activity);
            songPlaylistDialog.createDiaLog(audioItem);
        });
        binding.info.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putParcelable("Item", audioItem);
            dialog.dismiss();
            NavController navController = Navigation.findNavController(activity, R.id.fragment_host);
            navController.navigate(R.id.audio_info_fragment, bundle);
        });
    }
}
