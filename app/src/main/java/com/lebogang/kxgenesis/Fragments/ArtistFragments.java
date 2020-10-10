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

import com.lebogang.audiofilemanager.Callbacks.ArtistCallBacks;
import com.lebogang.audiofilemanager.Models.ArtistMediaItem;
import com.lebogang.audiofilemanager.Models.MediaItem;
import com.lebogang.kxgenesis.Adapters.ArtistAdapterMultiColumn;
import com.lebogang.kxgenesis.Adapters.ArtistAdapterSingleColumn;
import com.lebogang.kxgenesis.Adapters.GeneralItemClick;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.ViewModels.ArtistViewModel;
import com.lebogang.kxgenesis.databinding.FragmentLayoutBinding;

import java.util.List;

public class ArtistFragments extends Fragment implements GeneralItemClick, ArtistCallBacks {
    private FragmentLayoutBinding binding;
    private ArtistAdapterMultiColumn adapterMultiColumn = new ArtistAdapterMultiColumn(this);
    private ArtistAdapterSingleColumn adapterSingleColumn = new ArtistAdapterSingleColumn(this);
    private ArtistViewModel viewModel;

    public ArtistFragments() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLayoutBinding.inflate(inflater);
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(ArtistViewModel.class);
        viewModel.initialize(getContext(),getViewLifecycleOwner());
        viewModel.getObserveItems(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int type = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE)
                .getInt("Item_Display", SettingsFragment.ITEM_DISPLAY_TILES);
        if (type == SettingsFragment.ITEM_DISPLAY_SINGLE_LINE){
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
            binding.recyclerView.setAdapter(adapterSingleColumn);
        }else {
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
            binding.recyclerView.setAdapter(adapterMultiColumn);
        }
    }

    @Override
    public void onGetAudio(List<ArtistMediaItem> mediaItems) {
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
