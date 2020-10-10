package com.lebogang.kxgenesis.Dialogs;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lebogang.audiofilemanager.AudioManagement.AudioFileManger;
import com.lebogang.audiofilemanager.Models.AudioMediaItem;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.LayoutEditAudioBinding;

public class AudioEdit {
    private AudioFileManger audioFileManger;
    private LayoutEditAudioBinding binding;
    private AudioFileManger.ValueBuilder valueBuilder;
    private AlertDialog dialog;
    private String title,artist, album, composer, year, track;

    public AudioEdit(AudioFileManger audioFileManger) {
        this.audioFileManger = audioFileManger;
    }

    public void createDialog(Context context, AudioMediaItem audioItem){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = LayoutEditAudioBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        valueBuilder = new AudioFileManger.ValueBuilder(audioItem);
        setupViews(context, audioItem);
        dialog = builder.create();
        dialog.show();
    }

    private void setupViews(Context context, AudioMediaItem audioItem){
        Glide.with(context).load(audioItem.getAlbumArtUri())
                .error(R.drawable.ic_music_light)
                .into(binding.imageView);
        title = audioItem.getTitle();
        album = audioItem.getAlbumTitle();
        artist = audioItem.getArtistTitle();
        composer = audioItem.getComposer();
        track = audioItem.getTrackNumber();
        year = audioItem.getReleaseYear();
        binding.titleEditText.setText(audioItem.getTitle());
        binding.titleEditText.addTextChangedListener(new Watcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                title = s.toString();
            }
        });
        binding.artistEditText.setText(audioItem.getArtistTitle());
        binding.artistEditText.addTextChangedListener(new Watcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                artist = s.toString();
            }
        });
        binding.albumEditText.setText(audioItem.getAlbumTitle());
        binding.albumEditText.addTextChangedListener(new Watcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                album = s.toString();
            }
        });
        binding.yearEditText.setText(audioItem.getReleaseYear());
        binding.yearEditText.addTextChangedListener(new Watcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                year = s.toString();
            }
        });
        binding.compilationEditText.setText(audioItem.getTrackNumber());
        binding.compilationEditText.addTextChangedListener(new Watcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                track = s.toString();
            }
        });
        binding.composerEditText.setText(audioItem.getComposer());
        binding.composerEditText.addTextChangedListener(new Watcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                composer = s.toString();
            }
        });
        binding.floatingActionButton.setOnClickListener(v->{
            AudioFileManger.ValueBuilder builder = new AudioFileManger.ValueBuilder(audioItem);
            builder.setAlbum(album);
            builder.setArtist(artist);
            builder.setCompilation(track);
            builder.setTitle(title);
            builder.setComposer(composer);
            builder.setYear(year);
            int r = audioFileManger.updateItem(audioItem,builder.build());
            if (r > 0){
                dialog.dismiss();
            }
        });
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
