package com.lebogang.kxgenesis.Dialogs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lebogang.audiofilemanager.Models.PlaylistMediaItem;
import com.lebogang.audiofilemanager.PlaylistManagement.PlaylistFileManager;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.LayoutPlaylistOptionsBinding;

public class PlaylistOptions {

    private LayoutPlaylistOptionsBinding binding;
    private PlaylistFileManager playlistFileManager;
    private AlertDialog dialog;
    private NavController navController;

    public PlaylistOptions(NavController navController, PlaylistFileManager playlistFileManager) {
        this.playlistFileManager = playlistFileManager;
        this.navController = navController;
    }

    public void createDialog(Context context, PlaylistMediaItem playlistItem){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = LayoutPlaylistOptionsBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        setupViews(context,playlistItem);
        dialog =builder.create();
        dialog.show();
    }

    private void setupViews(Context context, PlaylistMediaItem playlistItem){
        binding.editPlaylist.setOnClickListener(c->{
            new PlaylistCreateUpdate(playlistFileManager).updateDialog(context,playlistItem);
        });
        binding.deletePlaylist.setOnClickListener(v->{
            int r = playlistFileManager.removeItem(playlistItem);
            if (r>0)
                dialog.dismiss();
        });
        binding.removeItems.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putParcelable("Item", playlistItem);
            dialog.dismiss();
            navController.navigate(R.id.playlist_item_manager_fragment, bundle);
        });
        binding.titleTextView.setText(playlistItem.getTitle());
        binding.subtitleTextView.setText(playlistItem.getSubTitle());
        binding.floatingActionButton.setOnClickListener(v->{
            dialog.dismiss();
        });
    }
}
