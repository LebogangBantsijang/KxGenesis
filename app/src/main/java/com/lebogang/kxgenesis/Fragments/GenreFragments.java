package com.lebogang.kxgenesis.Fragments;

import android.content.Intent;
import android.os.Bundle;
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

import com.lebogang.audiofilemanager.Callbacks.GenreCallBacks;
import com.lebogang.audiofilemanager.Models.GenreMediaItem;
import com.lebogang.audiofilemanager.Models.MediaItem;
import com.lebogang.kxgenesis.Adapters.GeneralItemClick;
import com.lebogang.kxgenesis.Adapters.GenreAdapter;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.ViewModels.GenreViewModel;
import com.lebogang.kxgenesis.databinding.FragmentLayoutBinding;

import java.util.List;

public class GenreFragments extends Fragment implements GeneralItemClick, GenreCallBacks {
    private FragmentLayoutBinding binding;
    private GenreAdapter adapter = new GenreAdapter(this);
    private GenreViewModel viewModel;

    public GenreFragments() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLayoutBinding.inflate(inflater);
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(GenreViewModel.class);
        viewModel.initialize(getContext(),getViewLifecycleOwner());
        viewModel.getObserveItems(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
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

    }

    @Override
    public void onGetAudio(List<GenreMediaItem> mediaItems) {
        adapter.setItems(mediaItems);
        binding.progressBar.setVisibility(View.GONE);
    }
}
