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
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lebogang.audiofilemanager.AudioManagement.AudioManager;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.LayoutEditAudioBinding;

public class SongsEditDialog {
    private final AppCompatActivity activity;
    private final AudioManager audioManager;
    private String title,artist, album, composer, year, track;
    private AlertDialog dialog;
    private LayoutEditAudioBinding binding;

    public SongsEditDialog(AppCompatActivity activity, AudioManager audioManager) {
        this.activity = activity;
        this.audioManager = audioManager;
    }

    public void createDialog(Audio audioItem){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        binding = LayoutEditAudioBinding.inflate(inflater);
        builder.setView(binding.getRoot());
        builder.setPositiveButton("Save", getOnClickListener(audioItem));
        builder.setNegativeButton("Cancel", null);
        setupViews(audioItem);
        dialog = builder.create();
        dialog.show();
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
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED){
                audioManager.updateAudio(audio.getId(), values);
                dialog.dismiss();
            }else {
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
            }
        };
    }

    private void setupViews(Audio audioItem){
        Glide.with(binding.imageView).load(audioItem.getAlbumArtUri())
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
