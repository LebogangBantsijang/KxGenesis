package com.lebogang.kxgenesis.Fragments;

import android.content.Context;
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
import androidx.recyclerview.widget.GridLayoutManager;

import com.lebogang.audiofilemanager.Callbacks.AlbumCallBacks;
import com.lebogang.audiofilemanager.Models.AlbumMediaItem;
import com.lebogang.audiofilemanager.Models.MediaItem;
import com.lebogang.kxgenesis.Adapters.AlbumAdapterMultiColumn;
import com.lebogang.kxgenesis.Adapters.AlbumAdapterSingleColumn;
import com.lebogang.kxgenesis.Adapters.GeneralItemClick;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.ViewModels.AlbumViewModel;
import com.lebogang.kxgenesis.databinding.FragmentLayoutBinding;

import java.util.List;

public class AlbumFragments extends Fragment implements GeneralItemClick, AlbumCallBacks {

    private FragmentLayoutBinding binding;
    private AlbumAdapterMultiColumn adapterMultiColumn = new AlbumAdapterMultiColumn(this);
    private AlbumAdapterSingleColumn adapterSingleColumn = new AlbumAdapterSingleColumn(this);

    private AlbumViewModel viewModel;

    public AlbumFragments() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLayoutBinding.inflate(inflater);
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(AlbumViewModel.class);
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
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
            binding.recyclerView.setAdapter(adapterMultiColumn);
        }
    }

    @Override
    public void onGetAudio(List<AlbumMediaItem> mediaItems) {
        adapterMultiColumn.setItems(mediaItems);
        adapterSingleColumn.setItems(mediaItems);
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(MediaItem mediaItem) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_host);
        Bundle bundle = new Bundle();
        bundle.putParcelable("Item", mediaItem);
        navController.navigate(R.id.album_artist_view_fragment, bundle);
    }

    @Override
    public void onOptionsClick(MediaItem mediaItem) {

    }
}
