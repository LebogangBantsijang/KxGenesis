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

import com.lebogang.audiofilemanager.ArtistManagement.ArtistCallbacks;
import com.lebogang.audiofilemanager.ArtistManagement.ArtistManager;
import com.lebogang.audiofilemanager.Models.Album;
import com.lebogang.audiofilemanager.Models.Artist;
import com.lebogang.audiofilemanager.Models.Media;
import com.lebogang.kxgenesis.Adapters.ArtistAdapter;
import com.lebogang.kxgenesis.Adapters.OnClickInterface;
import com.lebogang.kxgenesis.Preferences;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.ViewModels.ArtistViewModel;
import com.lebogang.kxgenesis.databinding.FragmentLayoutBinding;

import java.util.List;

public class ArtistFragments extends Fragment implements OnClickInterface, ArtistCallbacks {
    private FragmentLayoutBinding binding;
    private ArtistAdapter adapter = new ArtistAdapter(this);
    private ArtistViewModel viewModel;
    private Preferences preferences;

    public ArtistFragments() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLayoutBinding.inflate(inflater);
        preferences = new Preferences(getContext());
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(ArtistViewModel.class);
        viewModel.init(getContext());
        viewModel.registerCallbacks(this,this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    private void initRecyclerView(){
        adapter.setLayoutGrid(preferences.isDisplayGrid());
        if (preferences.isDisplayGrid())
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        else
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onQueryComplete(List<Artist> artistList) {
        adapter.setList(ArtistManager.groupByName(artistList));
    }

    @Override
    public void onClick(Media media) {
        Artist artist = (Artist) media;
        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_host);
        Bundle bundle = new Bundle();
        bundle.putParcelable("Artist", artist);
        navController.navigate(R.id.artist_view_fragment, bundle);
    }
}
