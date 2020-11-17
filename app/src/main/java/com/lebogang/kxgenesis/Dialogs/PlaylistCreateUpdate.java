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
import android.content.DialogInterface;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lebogang.audiofilemanager.Models.Playlist;
import com.lebogang.audiofilemanager.PlaylistManagement.PlaylistManager;
import com.lebogang.kxgenesis.databinding.LayoutNewPlaylistBinding;

public class PlaylistCreateUpdate {
    private LayoutNewPlaylistBinding binding;
    private String name = "";
    private AlertDialog dialog;
    private PlaylistManager playlistFileManager;
    private Context context;

    public PlaylistCreateUpdate(Context context) {
        this.context = context;
        this.playlistFileManager = new PlaylistManager(context);
    }

    public void setPlaylistFileManager(PlaylistManager playlistFileManager) {
        this.playlistFileManager = playlistFileManager;
    }

    public void createDialog(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = LayoutNewPlaylistBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Save", getCreateOnClickListener());
        setupCreateView();
        dialog = builder.create();
        dialog.show();
    }

    private void setupCreateView(){
        binding.nameEditText.addTextChangedListener(new AudioEdit.Watcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                name = s.toString();
            }
        });
    }

    public void updateDialog(Playlist playlistItem){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = LayoutNewPlaylistBinding.inflate(inflater);
        setupUpdateView(playlistItem);
        builder.setPositiveButton("Save", getUpdateOnClickListener(playlistItem));
        builder.setNegativeButton("Cancel", null);
        builder.setView(binding.getRoot());
        dialog = builder.create();
        dialog.show();
    }

    private void setupUpdateView(Playlist playlistItem){
        binding.textView.setText("Update Playlist");
        binding.nameEditText.setText(playlistItem.getTitle());
        binding.nameEditText.setHint("update playlist name");
        binding.nameEditText.addTextChangedListener(new AudioEdit.Watcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                name = s.toString();
            }
        });
    }

    private DialogInterface.OnClickListener getUpdateOnClickListener(Playlist playlist){
        return (dialog, which)->{
            if (name.length() > 0){
                boolean result =playlistFileManager.updatePlaylist(playlist.getId(), name);
                if (result)
                    dialog.dismiss();
            }
        };
    }

    private DialogInterface.OnClickListener getCreateOnClickListener(){
        return (dialog, which)->{
            if (name.length() > 0){
                boolean result =playlistFileManager.createPlaylist(name);
                if (result)
                    dialog.dismiss();
            }
        };
    }
}
