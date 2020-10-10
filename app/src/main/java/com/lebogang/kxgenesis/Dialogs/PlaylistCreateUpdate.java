package com.lebogang.kxgenesis.Dialogs;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lebogang.audiofilemanager.Models.PlaylistMediaItem;
import com.lebogang.audiofilemanager.PlaylistManagement.PlaylistFileManager;
import com.lebogang.kxgenesis.databinding.LayoutNewPlaylistBinding;

public class PlaylistCreateUpdate {
    private LayoutNewPlaylistBinding binding;
    private String name = "";
    private AlertDialog dialog;
    private PlaylistFileManager playlistFileManager;

    public PlaylistCreateUpdate(PlaylistFileManager playlistFileManager) {
        this.playlistFileManager = playlistFileManager;
    }

    public void createDialog(Context context){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = LayoutNewPlaylistBinding.inflate(inflater);
        builder.setView(binding.getRoot());
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
        binding.floatingActionButton.setOnClickListener(v->{
            if (name.length() > 0){
                Uri r = playlistFileManager.createNewItem(name);
                if (r != null)
                    dialog.dismiss();
            }
        });
    }

    public void updateDialog(Context context, PlaylistMediaItem playlistItem){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = LayoutNewPlaylistBinding.inflate(inflater);
        setupUpdateView(playlistItem);
        builder.setView(binding.getRoot());
        dialog = builder.create();
        dialog.show();
    }

    private void setupUpdateView(PlaylistMediaItem playlistItem){
        binding.textView.setText("Edit Playlist");
        binding.nameEditText.setText(playlistItem.getTitle());
        binding.nameEditText.setHint("edit playlist");
        binding.nameEditText.addTextChangedListener(new AudioEdit.Watcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                name = s.toString();
            }
        });
        binding.floatingActionButton.setOnClickListener(v->{
            if (name.length() > 0){
                int r=playlistFileManager.updateItem(playlistItem, name);
                if (r >0)
                    dialog.dismiss();
            }
        });
    }

}
