package com.lebogang.kxgenesis.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.lebogang.audiofilemanager.Callbacks.PlaylistCallBacks;
import com.lebogang.audiofilemanager.Models.MediaItem;
import com.lebogang.audiofilemanager.Models.PlaylistMediaItem;
import com.lebogang.kxgenesis.Adapters.GeneralItemClick;
import com.lebogang.kxgenesis.Adapters.PlaylistAdapterMultiColumn;
import com.lebogang.kxgenesis.Adapters.PlaylistAdapterSingleColumn;
import com.lebogang.kxgenesis.Dialogs.PlaylistCreateUpdate;
import com.lebogang.kxgenesis.Dialogs.PlaylistOptions;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.ViewModels.PlaylistViewModel;
import com.lebogang.kxgenesis.databinding.FragmentPlaylistsBinding;

import java.util.List;

public class PlaylistFragments extends Fragment implements GeneralItemClick, PlaylistCallBacks {
    private FragmentPlaylistsBinding binding;
    private PlaylistAdapterMultiColumn adapterMultiColumn = new PlaylistAdapterMultiColumn(this);
    private PlaylistAdapterSingleColumn adapterSingleColumn = new PlaylistAdapterSingleColumn(this);
    private PlaylistViewModel viewModel;

    public PlaylistFragments() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlaylistsBinding.inflate(inflater);
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(PlaylistViewModel.class);
        viewModel.initialize(getContext(),getViewLifecycleOwner());
        viewModel.getObserveItems(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int type = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getInt("Item_Display", SettingsFragment.ITEM_DISPLAY_TILES);
        if (type == SettingsFragment.ITEM_DISPLAY_SINGLE_LINE){
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
            binding.recyclerView.setAdapter(adapterSingleColumn);
        }else {
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
            binding.recyclerView.setAdapter(adapterMultiColumn);
        }
        binding.newPlaylistButton.setOnClickListener(v->{
            new PlaylistCreateUpdate(viewModel.getPlaylistFileManager()).createDialog(getContext());
        });
    }

    @Override
    public void onClick(MediaItem mediaItem) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_host);
        Bundle bundle = new Bundle();
        bundle.putParcelable("Item", mediaItem);
        navController.navigate(R.id.genre_playlist_view_fragment, bundle);
    }

    @Override
    public void onOptionsClick(MediaItem mediaItem) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_host);
        new PlaylistOptions(navController,viewModel.getPlaylistFileManager())
                .createDialog(getContext(), (PlaylistMediaItem) mediaItem);
    }

    @Override
    public void onGetAudio(List<PlaylistMediaItem> mediaItems) {
        adapterMultiColumn.setItems(mediaItems);
        adapterSingleColumn.setItems(mediaItems);
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(PlaylistMediaItem mediaItem) {
        adapterMultiColumn.create(mediaItem);
        adapterSingleColumn.create(mediaItem);
    }

    @Override
    public void onUpdate(PlaylistMediaItem mediaItem) {
        adapterMultiColumn.update(mediaItem);
        adapterSingleColumn.update(mediaItem);
    }

    @Override
    public void onRemove(PlaylistMediaItem mediaItem) {
        adapterMultiColumn.remove(mediaItem);
        adapterSingleColumn.remove(mediaItem);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
