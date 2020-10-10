package com.lebogang.kxgenesis.Fragments;

import android.os.Bundle;
import android.support.v4.media.session.MediaControllerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lebogang.audiofilemanager.Callbacks.AudioCallBacks;
import com.lebogang.audiofilemanager.Models.AudioMediaItem;
import com.lebogang.audiofilemanager.Models.MediaItem;
import com.lebogang.kxgenesis.Adapters.AudioAdapter;
import com.lebogang.kxgenesis.Adapters.GeneralItemClick;
import com.lebogang.kxgenesis.Dialogs.AudioOptions;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.Utils.AudioIndicator;
import com.lebogang.kxgenesis.Utils.ExtractColor;
import com.lebogang.kxgenesis.ViewModels.AudioViewModel;
import com.lebogang.kxgenesis.databinding.FragmentLayoutBinding;

import java.util.List;

public class SongsFragments extends Fragment implements GeneralItemClick, AudioCallBacks {

    private FragmentLayoutBinding binding;
    private AudioAdapter adapter = new AudioAdapter(this);
    private AudioViewModel viewModel;

    public SongsFragments() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLayoutBinding.inflate(inflater);
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(AudioViewModel.class);
        viewModel.initialize(getContext(),getViewLifecycleOwner());
        viewModel.getObserveItems(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        AudioIndicator.getCurrentItem().observe(getViewLifecycleOwner(),mediaItem -> {
            int color = ExtractColor.getColor(getContext(), mediaItem.getAlbumArtUri());
            adapter.setCurrentID(mediaItem.getMediaId(), color);
            int position = adapter.getItemPosition(mediaItem);
            binding.recyclerView.scrollToPosition(position);
        });
    }

    @Override
    public void onGetAudio(List<AudioMediaItem> mediaItems) {
        adapter.setItems(mediaItems);
        binding.progressBar.setVisibility(View.GONE);
        MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(getActivity());
        if (mediaControllerCompat != null){
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("Items",adapter.getItems());
            mediaControllerCompat.getTransportControls().sendCustomAction("Act", bundle);
        }
    }

    @Override
    public void onUpdate(AudioMediaItem mediaItem) {
        adapter.update(mediaItem);
    }

    @Override
    public void onRemove(AudioMediaItem mediaItem) {
        adapter.remove(mediaItem);
    }

    @Override
    public void onClick(MediaItem mediaItem) {
        MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(getActivity());
        if (mediaControllerCompat != null){
            Bundle bundle = new Bundle();
            bundle.putParcelable("Item", mediaItem);
            mediaControllerCompat.getTransportControls().playFromUri(mediaItem.getContentUri(), bundle);
            bundle.putParcelableArrayList("Items",adapter.getItems());
            mediaControllerCompat.getTransportControls().sendCustomAction("Act", bundle);
        }
    }

    @Override
    public void onOptionsClick(MediaItem mediaItem) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_host);
        new AudioOptions(viewModel.getAudioFileManger(), navController).createDialog(getContext(), (AudioMediaItem) mediaItem);
    }

}
