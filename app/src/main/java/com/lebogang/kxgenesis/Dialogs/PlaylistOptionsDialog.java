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
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.Room.Model.PlaylistDetails;
import com.lebogang.kxgenesis.ViewModels.PlaylistViewModel;
import com.lebogang.kxgenesis.databinding.LayoutPlaylistOptionsBinding;

public class PlaylistOptionsDialog {

    private LayoutPlaylistOptionsBinding binding;
    private final PlaylistViewModel viewModel;
    private AlertDialog dialog;
    private final Context context;

    public PlaylistOptionsDialog(Context context, PlaylistViewModel viewModel) {
        this.viewModel = viewModel;
        this.context = context;
    }

    public void createDialog(PlaylistDetails playlistItem){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = LayoutPlaylistOptionsBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        builder.setNegativeButton("Cancel", null);
        setupViews(playlistItem);
        dialog =builder.create();
        dialog.show();
    }

    private void setupViews(PlaylistDetails playlistItem){
        binding.editPlaylist.setOnClickListener(c->{
            PlaylistCreateUpdate playlistCreateUpdate = new PlaylistCreateUpdate(context, viewModel);
            dialog.dismiss();
            playlistCreateUpdate.updateDialog(playlistItem);
        });
        binding.deletePlaylist.setOnClickListener(v->{
            viewModel.deletePlaylist(playlistItem);
            Toast.makeText(context, "PLaylist removed", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        });
        binding.titleTextView.setText(playlistItem.getPlaylistName());
        Uri uri = Uri.parse(playlistItem.getArtUri());
        Glide.with(binding.imageView)
                .load(uri)
                .error(R.drawable.ic_playlist)
                .dontAnimate()
                .into(binding.imageView)
                .waitForLayout();
    }
}
