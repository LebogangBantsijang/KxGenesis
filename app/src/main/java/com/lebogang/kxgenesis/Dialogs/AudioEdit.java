package com.lebogang.kxgenesis.Dialogs;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lebogang.audiofilemanager.AudioManagement.AudioManager;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.LayoutEditAudioBinding;

public class AudioEdit {
    private AudioManager audioFileManger;
    private LayoutEditAudioBinding binding;
    private AlertDialog dialog;
    private Context context;
    private String title,artist, album, composer, year, track;

    public AudioEdit(Context context) {
        this.context = context;
        this.audioFileManger = new AudioManager(context);
    }

    public void createDialog(Audio audioItem){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = LayoutEditAudioBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        builder.setPositiveButton("Save", getOnClickListener(audioItem));
        builder.setNegativeButton("Cancel", null);
        setupViews(audioItem);
        dialog = builder.create();
        dialog.show();
    }

    private void setupViews(Audio audioItem){
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
    }

    private DialogInterface.OnClickListener getOnClickListener(Audio audio){
        return (dialog, which) -> {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Audio.Media.TITLE, title);
            values.put(MediaStore.Audio.Media.ALBUM, album);
            values.put(MediaStore.Audio.Media.ARTIST, artist);
            values.put(MediaStore.Audio.Media.COMPOSER, composer);
            values.put(MediaStore.Audio.Media.TRACK, track);
            values.put(MediaStore.Audio.Media.YEAR, year);
            boolean results = audioFileManger.updateAudio(audio.getId(), values);
            if (results)
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
