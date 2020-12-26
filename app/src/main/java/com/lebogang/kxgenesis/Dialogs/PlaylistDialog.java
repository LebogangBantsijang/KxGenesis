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
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lebogang.audiofilemanager.Models.Playlist;
import com.lebogang.audiofilemanager.PlaylistManagement.PlaylistManager;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.LayoutPlaylistOptionsBinding;

public class PlaylistDialog {

    private LayoutPlaylistOptionsBinding binding;
    private final PlaylistManager playlistFileManager;
    private AlertDialog dialog;
    private final AppCompatActivity activity;

    public PlaylistDialog(PlaylistManager playlistFileManager, AppCompatActivity activity) {
        this.playlistFileManager = playlistFileManager;
        this.activity = activity;
    }

    public void createDialog(Playlist playlistItem){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        binding = LayoutPlaylistOptionsBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        builder.setNegativeButton("Cancel", null);
        setupViews(playlistItem);
        dialog =builder.create();
        dialog.show();
    }

    private void setupViews(Playlist playlistItem){
        binding.editPlaylist.setOnClickListener(c->{
            PlaylistCreateUpdate playlistCreateUpdate = new PlaylistCreateUpdate(activity);
            playlistCreateUpdate.setPlaylistFileManager(playlistFileManager);
            playlistCreateUpdate.updateDialog(playlistItem);
        });
        binding.deletePlaylist.setOnClickListener(v->{
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED){
                playlistFileManager.deletePlaylist(playlistItem.getId());
                dialog.dismiss();
            }else
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
        });
        binding.removeItems.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putParcelable("Playlist", playlistItem);
            dialog.dismiss();
            NavController navController = Navigation.findNavController(activity, R.id.fragment_host);
            navController.navigate(R.id.playlist_audio_manager_fragment, bundle);
        });
        binding.titleTextView.setText(playlistItem.getTitle());
    }
}
