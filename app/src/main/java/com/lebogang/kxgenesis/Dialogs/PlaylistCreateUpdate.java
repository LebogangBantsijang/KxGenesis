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
import android.icu.util.Calendar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lebogang.kxgenesis.Room.Model.PlaylistDetails;
import com.lebogang.kxgenesis.ViewModels.PlaylistViewModel;
import com.lebogang.kxgenesis.databinding.LayoutNewPlaylistBinding;

import java.util.Date;
import java.util.List;

public class PlaylistCreateUpdate {
    private LayoutNewPlaylistBinding binding;
    private String name = null;
    private AlertDialog dialog;
    private final Context context;
    private final PlaylistViewModel viewModel;

    public PlaylistCreateUpdate(Context context,PlaylistViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
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
        binding.nameEditText.addTextChangedListener(new Watcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                name = s.toString();
            }
        });
    }

    public void updateDialog(PlaylistDetails playlistItem){
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

    private void setupUpdateView(PlaylistDetails playlistItem){
        binding.textView.setText("Update Playlist");
        binding.nameEditText.setText(playlistItem.getPlaylistName());
        binding.nameEditText.setHint("update playlist name");
        binding.nameEditText.addTextChangedListener(new Watcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                name = s.toString();
            }
        });
    }

    private DialogInterface.OnClickListener getUpdateOnClickListener(PlaylistDetails playlist){
        return (dialog, which)->{
            if (name != null){
                Date date = Calendar.getInstance().getTime();
                playlist.setPlaylistName(name);
                playlist.setDateCreated("Updated on: " + date.toString());
                viewModel.updatePlaylist(playlist);
                Toast.makeText(context.getApplicationContext(), "Playlist Updated", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(context, "Playlist name empty", Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        };
    }

    private DialogInterface.OnClickListener getCreateOnClickListener(){
        return (dialog, which)->{
            if (name != null){
                Date date = Calendar.getInstance().getTime();
                PlaylistDetails playlist = new PlaylistDetails(0, name, date.toString(), "no uri");
                viewModel.insertPlaylist(playlist);
                Toast.makeText(context.getApplicationContext(), "Playlist Added", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(context.getApplicationContext(), "Playlist name empty", Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        };
    }

    public abstract static class Watcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
