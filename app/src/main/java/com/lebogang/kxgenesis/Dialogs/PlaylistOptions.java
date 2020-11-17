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
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lebogang.audiofilemanager.Models.Playlist;
import com.lebogang.audiofilemanager.PlaylistManagement.PlaylistManager;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.LayoutPlaylistOptionsBinding;

public class PlaylistOptions {

    private LayoutPlaylistOptionsBinding binding;
    private PlaylistManager playlistFileManager;
    private AlertDialog dialog;
    private NavController navController;
    private Context context;

    public PlaylistOptions(Context context, NavController navController) {
        this.context = context;
        this.playlistFileManager = new PlaylistManager(context);
        this.navController = navController;
    }

    public void setPlaylistFileManager(PlaylistManager playlistFileManager) {
        this.playlistFileManager = playlistFileManager;
    }

    public void createDialog(Playlist playlistItem){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = LayoutPlaylistOptionsBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        builder.setNegativeButton("Cancel", null);
        setupViews(playlistItem);
        dialog =builder.create();
        dialog.show();
    }

    private void setupViews(Playlist playlistItem){
        binding.editPlaylist.setOnClickListener(c->{
            PlaylistCreateUpdate playlistCreateUpdate = new PlaylistCreateUpdate(context);
            playlistCreateUpdate.setPlaylistFileManager(playlistFileManager);
            playlistCreateUpdate.updateDialog(playlistItem);
        });
        binding.deletePlaylist.setOnClickListener(v->{
            boolean result = playlistFileManager.deletePlaylist(playlistItem.getId());
            if (result)
                dialog.dismiss();
        });
        binding.removeItems.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putParcelable("Playlist", playlistItem);
            dialog.dismiss();
            navController.navigate(R.id.playlist_audio_manager_fragment, bundle);
        });
        binding.titleTextView.setText(playlistItem.getTitle());
    }
}
