package com.lebogang.kxgenesis.Fragments;

import android.os.Bundle;
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
import com.lebogang.audiofilemanager.Models.PlaylistMediaItem;
import com.lebogang.audiofilemanager.PlaylistManagement.PlaylistFileManager;
import com.lebogang.kxgenesis.Adapters.AudioFromPlaylistAdapter;
import com.lebogang.kxgenesis.Adapters.GeneralItemClick;
import com.lebogang.kxgenesis.ViewModels.AudioViewModel;
import com.lebogang.kxgenesis.databinding.FragmentPlaylistItemManagerBinding;

import java.util.List;

public class PlaylistItemManagerFragment extends Fragment implements GeneralItemClick, AudioCallBacks {
    private FragmentPlaylistItemManagerBinding binding;
    private PlaylistMediaItem mediaItem;
    private AudioFromPlaylistAdapter adapter = new AudioFromPlaylistAdapter(this);
    private PlaylistFileManager playlistFileManager;
    private AudioViewModel viewModel;

    public PlaylistItemManagerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlaylistItemManagerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mediaItem = getArguments().getParcelable("Item");
        playlistFileManager = new PlaylistFileManager();
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(AudioViewModel.class);
        viewModel.initialize(getContext(),this);
        viewModel.getObserveItems(this, mediaItem.getTitle(),mediaItem.getMediaId(), mediaItem.getMediaItemType());
        setupRecyclerView();
    }

    private void setupRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(MediaItem mediaItem) {

    }

    @Override
    public void onOptionsClick(MediaItem mediaItem) {
        int r = playlistFileManager.removeAudioFromItem(getContext(),this.mediaItem, (AudioMediaItem) mediaItem);
        if (r > 0)
            adapter.remove((AudioMediaItem) mediaItem);
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
