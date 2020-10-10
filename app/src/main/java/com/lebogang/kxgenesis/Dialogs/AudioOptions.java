package com.lebogang.kxgenesis.Dialogs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lebogang.audiofilemanager.AlbumManagement.AlbumFileManager;
import com.lebogang.audiofilemanager.ArtistManagement.ArtistFileManager;
import com.lebogang.audiofilemanager.AudioManagement.AudioFileManger;
import com.lebogang.audiofilemanager.Models.AlbumMediaItem;
import com.lebogang.audiofilemanager.Models.ArtistMediaItem;
import com.lebogang.audiofilemanager.Models.AudioMediaItem;
import com.lebogang.kxgenesis.Utils.TimeUnitConvert;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.LayoutSongOptionsBinding;

public class AudioOptions {
    private AudioFileManger audioFileManger;
    private AlbumFileManager albumFileManager;
    private ArtistFileManager artistFileManager;
    private LayoutSongOptionsBinding binding;
    private AlertDialog dialog;
    private NavController navController;

    public AudioOptions(AudioFileManger audioFileManger, NavController navController) {
        this.audioFileManger = audioFileManger;
        this.navController = navController;
        albumFileManager = new AlbumFileManager();
        artistFileManager = new ArtistFileManager();
    }

    public void createDialog(Context context, AudioMediaItem audioItem){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = LayoutSongOptionsBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        setupView(context,audioItem);
        dialog = builder.create();
        dialog.show();
    }

    private void setupView(Context context,AudioMediaItem audioItem){
        Glide.with(context).load(audioItem.getAlbumArtUri())
                .error(R.drawable.ic_music_light)
                .into(binding.imageView);
        binding.titleTextView.setText(audioItem.getTitle());
        binding.subtitleTextView.setText(audioItem.getSubTitle());
        String dur = TimeUnitConvert.toMinutes(audioItem.getDuration());
        binding.durationTextView.setText(dur);
        binding.floatingActionButton.setOnClickListener(v->{
            dialog.dismiss();
        });
        binding.goToAlbum.setOnClickListener(v->{
            AlbumMediaItem item = albumFileManager.getItem(context, audioItem.getAlbumId());
            Bundle bundle = new Bundle();
            bundle.putParcelable("Item", item);
            dialog.dismiss();
            navController.navigate(R.id.album_artist_view_fragment, bundle);
        });
        binding.goToArtist.setOnClickListener(v->{
            ArtistMediaItem item = artistFileManager.getItem(context, audioItem.getArtistId());
            Bundle bundle = new Bundle();
            bundle.putParcelable("Item", item);
            dialog.dismiss();
            navController.navigate(R.id.album_artist_view_fragment, bundle);
        });
        binding.share.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("audio/*");
            intent.putExtra(Intent.EXTRA_STREAM, audioItem.getContentUri());
            context.startActivity(Intent.createChooser(intent,"Share Song"));
        });
        binding.delete.setOnClickListener(v->{
            int r = audioFileManger.removeItem(audioItem);
            if (r > 0)
                dialog.dismiss();
        });
        binding.editTags.setOnClickListener(v->{
            new AudioEdit(audioFileManger).createDialog(context, audioItem);
        });
        binding.addToPlaylist.setOnClickListener(v->{
            new AudioToPlaylistDialog().createDiaLog(context,audioItem);
        });
        binding.info.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putParcelable("Item", audioItem);
            dialog.dismiss();
            navController.navigate(R.id.audio_info_fragment, bundle);
        });
    }
}
