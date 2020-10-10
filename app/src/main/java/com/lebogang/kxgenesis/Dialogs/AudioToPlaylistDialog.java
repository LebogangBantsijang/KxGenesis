package com.lebogang.kxgenesis.Dialogs;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lebogang.audiofilemanager.Models.AudioMediaItem;
import com.lebogang.audiofilemanager.Models.PlaylistMediaItem;
import com.lebogang.audiofilemanager.PlaylistManagement.PlaylistFileManager;
import com.lebogang.kxgenesis.databinding.LayoutSeletectPlaylistBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AudioToPlaylistDialog {

    private LayoutSeletectPlaylistBinding binding;
    private AlertDialog dialog;
    private List<PlaylistMediaItem> playlistItems;
    private PlaylistFileManager playlistFileManager;

    public AudioToPlaylistDialog() {
        playlistFileManager = new PlaylistFileManager();
    }

    public void createDiaLog(Context context, AudioMediaItem audioItem){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = LayoutSeletectPlaylistBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        setupViews(context, audioItem);
        dialog = builder.create();
        dialog.show();
    }

    private void setupViews(Context context, AudioMediaItem audioItem){
        ArrayAdapter adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, getData(context));
        binding.floatingActionButton.setOnClickListener(v->{
            dialog.dismiss();
        });
        binding.listView.setAdapter(adapter);
        binding.listView.setOnItemClickListener((parent, view, position, id) -> {
            PlaylistMediaItem item = playlistItems.get(position);
            Uri r =playlistFileManager.addAudioToItem(context,item,audioItem);
            if (r != null)
                dialog.dismiss();
        });
    }

    private List<String> getData(Context context){
        List<String> strings = new ArrayList<>();
        playlistItems = playlistFileManager.getItems(context);
        for (PlaylistMediaItem item:playlistItems){
            strings.add(item.getTitle() + " (" + TimeUnit.MILLISECONDS.toMinutes(item.getDuration()) + "min)");
        }
        return strings;
    }
}
