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

import com.lebogang.audiofilemanager.AudioManagement.AudioCallbacks;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.audiofilemanager.Models.Genre;
import com.lebogang.audiofilemanager.Models.Media;
import com.lebogang.audiofilemanager.Models.Playlist;
import com.lebogang.kxgenesis.Adapters.AudioAdapter;
import com.lebogang.kxgenesis.Adapters.OnClickInterface;
import com.lebogang.kxgenesis.Adapters.OnClickOptionsInterface;
import com.lebogang.kxgenesis.Dialogs.AudioOptions;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.Utils.AudioIndicator;
import com.lebogang.kxgenesis.Utils.ExtractColor;
import com.lebogang.kxgenesis.ViewModels.AudioViewModel;
import com.lebogang.kxgenesis.databinding.FragmentGenreViewBinding;
import com.lebogang.kxgenesis.databinding.FragmentPlaylistViewBinding;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlaylistViewFragment extends Fragment implements OnClickInterface, OnClickOptionsInterface, AudioCallbacks {
    private FragmentPlaylistViewBinding binding;
    private AudioAdapter adapter = new AudioAdapter(this, this);
    private Playlist playlist;
    private AudioViewModel viewModel;

    public PlaylistViewFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlaylistViewBinding.inflate(inflater, container, false);
        playlist = getArguments().getParcelable("Playlist");
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(AudioViewModel.class);
        viewModel.init(getContext());
        viewModel.registerCallbacksForAudioIds(this, this, playlist.getAudioIds());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupMediaItemDetails();
        setupRecyclerView();
    }

    private void setupMediaItemDetails(){
        binding.titleTextView.setText(playlist.getTitle());
    }

    private void setupRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        AudioIndicator.getCurrentItem().observe(getViewLifecycleOwner(), mediaItem -> {
            int color = ExtractColor.getColor(getContext(), mediaItem.getAlbumArtUri());
            adapter.setCurrentID(mediaItem.getId(), color);
            int position = adapter.getItemPosition(mediaItem);
            binding.recyclerView.scrollToPosition(position);
        });
    }

    @Override
    public void onQueryComplete(List<Audio> audioList) {
        adapter.setList(audioList);
        binding.numSongsTextView.setText("song count: " + audioList.size());
        int duration = 0;
        for (Audio audio:audioList){
            duration += audio.getAudioDuration();
        }
        binding.durationTextView.setText("duration: " + TimeUnit.MILLISECONDS.toMinutes(duration) + "min");
    }

    @Override
    public void onClick(Media media) {
        Audio audio = (Audio) media;
        MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(getActivity());
        if (mediaControllerCompat != null){
            Bundle bundle = new Bundle();
            bundle.putParcelable("Item", audio);
            mediaControllerCompat.getTransportControls().playFromUri(audio.getUri(), bundle);
            bundle.putParcelableArrayList("Items",adapter.getList());
            mediaControllerCompat.getTransportControls().sendCustomAction("Act", bundle);
        }
    }

    @Override
    public void onOptionsClick(Media media) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_host);
        new AudioOptions(getContext(), navController).createDialog((Audio) media);
    }
}
