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
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.audiofilemanager.Models.Playlist;
import com.lebogang.audiofilemanager.PlaylistManagement.PlaylistManager;
import com.lebogang.kxgenesis.databinding.LayoutSeletectPlaylistBinding;

import java.util.ArrayList;
import java.util.List;

public class SongPlaylistDialog {
    private LayoutSeletectPlaylistBinding binding;
    private AlertDialog dialog;
    private List<Playlist> playlistItems;
    private final PlaylistManager playlistFileManager;
    private final AppCompatActivity activity;

    public SongPlaylistDialog(AppCompatActivity activity) {
        this.activity = activity;
        playlistFileManager = new PlaylistManager(activity);
    }

    public void createDiaLog(Audio audioItem) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        binding = LayoutSeletectPlaylistBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        builder.setNegativeButton("Cancel", null);
        setupViews(audioItem);
        dialog = builder.create();
        dialog.show();
    }

    private void setupViews(Audio audioItem){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, getData());
        binding.listView.setAdapter(adapter);
        binding.listView.setOnItemClickListener((parent, view, position, id) -> {
            Playlist item = playlistItems.get(position);
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED){
                playlistFileManager.addSingleItemsToPlaylist(item.getId(),Long.toString(audioItem.getId()));
                dialog.dismiss();
            }else {
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
            }
        });
    }

    private List<String> getData(){
        List<String> strings = new ArrayList<>();
        playlistItems = playlistFileManager.getPlaylists();
        for (Playlist item:playlistItems){
            strings.add(item.getTitle() + " - " + item.getAudioIds().length + " songs");
        }
        return strings;
    }
}
