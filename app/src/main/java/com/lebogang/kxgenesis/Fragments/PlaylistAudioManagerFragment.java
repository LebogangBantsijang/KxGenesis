package com.lebogang.kxgenesis.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lebogang.audiofilemanager.AudioManagement.AudioCallbacks;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.audiofilemanager.Models.Playlist;
import com.lebogang.audiofilemanager.PlaylistManagement.PlaylistManager;
import com.lebogang.kxgenesis.Adapters.PlaylistAudioManagerAdapter;
import com.lebogang.kxgenesis.ViewModels.AudioViewModel;
import com.lebogang.kxgenesis.databinding.FragmentPlaylistItemManagerBinding;

import java.util.List;

public class PlaylistAudioManagerFragment extends Fragment implements AudioCallbacks {
    private FragmentPlaylistItemManagerBinding binding;
    private Playlist playlist;
    private PlaylistAudioManagerAdapter adapter = new PlaylistAudioManagerAdapter();
    private PlaylistManager playlistFileManager;
    private AudioViewModel viewModel;

    public PlaylistAudioManagerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlaylistItemManagerBinding.inflate(inflater, container, false);
        playlistFileManager = new PlaylistManager(getContext());
        playlist = getArguments().getParcelable("Playlist");
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(AudioViewModel.class);
        viewModel.init(getContext());
        viewModel.registerCallbacksForAudioIds(this,this,playlist.getAudioIds());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        initDeleteBTN();
    }

    private void setupRecyclerView(){
        binding.titleTextView.setText(playlist.getTitle());
        adapter.setContext(getContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void initDeleteBTN(){
        binding.saveButton.setOnClickListener(v->{
            String[] audioIds = new String[adapter.getCheckedItems().size()];
            for (int x = 0; x < adapter.getCheckedItems().size(); x++){
                long id = adapter.getCheckedItems().get(x).getId();
                audioIds[x] = Long.toString(id);
            }
            if(audioIds.length > 0 ){
                playlistFileManager.deleteAudioFromPlaylist(playlist.getId(), audioIds);
                Toast.makeText(getContext(), "Items Removed", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(getContext(), "Operation Failed", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onQueryComplete(List<Audio> audioList) {
        adapter.setList(audioList);
    }
}
