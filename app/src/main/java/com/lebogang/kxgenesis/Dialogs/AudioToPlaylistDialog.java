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
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.audiofilemanager.Models.Playlist;
import com.lebogang.audiofilemanager.PlaylistManagement.PlaylistManager;
import com.lebogang.kxgenesis.databinding.LayoutSeletectPlaylistBinding;

import java.util.ArrayList;
import java.util.List;

public class AudioToPlaylistDialog {

    private LayoutSeletectPlaylistBinding binding;
    private AlertDialog dialog;
    private List<Playlist> playlistItems;
    private PlaylistManager playlistFileManager;
    private Context context;

    public AudioToPlaylistDialog(Context context) {
        this.context = context;
        playlistFileManager = new PlaylistManager(context);
    }

    public void createDiaLog(Audio audioItem){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = LayoutSeletectPlaylistBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        builder.setNegativeButton("Cancel", null);
        setupViews(audioItem);
        dialog = builder.create();
        dialog.show();
    }

    private void setupViews(Audio audioItem){
        ArrayAdapter adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, getData());
        binding.listView.setAdapter(adapter);
        binding.listView.setOnItemClickListener((parent, view, position, id) -> {
            Playlist item = playlistItems.get(position);
            boolean result =playlistFileManager.addSingleItemsToPlaylist(item.getId(),Long.toString(audioItem.getId()));
            if (result)
                dialog.dismiss();
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
