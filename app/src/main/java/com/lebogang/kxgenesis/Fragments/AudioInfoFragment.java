package com.lebogang.kxgenesis.Fragments;

import android.os.Bundle;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.lebogang.audiofilemanager.Models.AudioMediaItem;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.Utils.TimeUnitConvert;
import com.lebogang.kxgenesis.databinding.FragmentAudioInfoBinding;

public class AudioInfoFragment extends Fragment {

    private AudioMediaItem audioMediaItem;
    private FragmentAudioInfoBinding binding;

    public AudioInfoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAudioInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        audioMediaItem = getArguments().getParcelable("Item");
        setupAllViews();
    }

    private void setupAllViews(){
        Glide.with(this).load(audioMediaItem.getAlbumArtUri())
                .error(R.drawable.ic_music_light)
                .into(binding.imageView);
        binding.titleTextText.setText(audioMediaItem.getTitle());
        binding.artistTextText.setText(audioMediaItem.getArtistTitle());
        binding.albumTextText.setText(audioMediaItem.getAlbumTitle());
        binding.durationTextText.setText(TimeUnitConvert.toMinutes(audioMediaItem.getDuration()));
        binding.sizeTextText.setText(Formatter.formatFileSize(getContext(),audioMediaItem.getAudioSize()));
        binding.composerTextText.setText(audioMediaItem.getComposer());
        binding.yearTextText.setText(audioMediaItem.getReleaseYear());
        binding.tackTextText.setText(audioMediaItem.getTrackNumber());
    }

}