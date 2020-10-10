package com.lebogang.kxgenesis.Fragments;

import android.os.Bundle;
import android.support.v4.media.session.MediaControllerCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lebogang.audiofilemanager.Callbacks.AudioCallBacks;
import com.lebogang.audiofilemanager.Models.AudioMediaItem;
import com.lebogang.audiofilemanager.Models.MediaItem;
import com.lebogang.kxgenesis.Adapters.AudioSearchAdapter;
import com.lebogang.kxgenesis.Adapters.GeneralItemClick;
import com.lebogang.kxgenesis.Utils.AudioIndicator;
import com.lebogang.kxgenesis.Utils.ExtractColor;
import com.lebogang.kxgenesis.ViewModels.AudioViewModel;
import com.lebogang.kxgenesis.databinding.FragmentSearchBinding;

import java.util.List;

public class SearchFragment extends Fragment implements GeneralItemClick, AudioCallBacks {
    private FragmentSearchBinding binding;
    private AudioSearchAdapter adapter = new AudioSearchAdapter(this);
    private AudioViewModel viewModel;

    public SearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(AudioViewModel.class);
        viewModel.initialize(getContext(),this);
        viewModel.getObserveItems(this);
        setupEditText();
        setupRecyclerView();
    }

    private void setupEditText(){
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        AudioIndicator.getCurrentItem().observe(getViewLifecycleOwner(), mediaItem -> {
            int color = ExtractColor.getColor(getContext(), mediaItem.getAlbumArtUri());
            adapter.setCurrentID(mediaItem.getMediaId(), color);
        });
    }

    @Override
    public void onClick(MediaItem mediaItem) {
        MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(getActivity());
        if (mediaControllerCompat != null){
            Bundle bundle = new Bundle();
            bundle.putParcelable("Item", mediaItem);
            mediaControllerCompat.getTransportControls().playFromUri(mediaItem.getContentUri(), bundle);
        }
    }

    @Override
    public void onOptionsClick(MediaItem mediaItem) {

    }

    @Override
    public void onGetAudio(List<AudioMediaItem> mediaItems) {
        adapter.setItems(mediaItems);
    }

    @Override
    public void onUpdate(AudioMediaItem mediaItem) {

    }

    @Override
    public void onRemove(AudioMediaItem mediaItem) {

    }
}
