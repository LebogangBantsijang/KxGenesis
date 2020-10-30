package com.lebogang.kxgenesis.Fragments;

import android.os.Bundle;
import android.provider.MediaStore;
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

import com.lebogang.audiofilemanager.AlbumManagement.AlbumCallbacks;
import com.lebogang.audiofilemanager.AlbumManagement.AlbumManager;
import com.lebogang.audiofilemanager.Models.Album;
import com.lebogang.audiofilemanager.Models.Media;
import com.lebogang.kxgenesis.Adapters.AlbumAdapter;
import com.lebogang.kxgenesis.Adapters.OnClickInterface;
import com.lebogang.kxgenesis.Preferences;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.ViewModels.AlbumViewModel;
import com.lebogang.kxgenesis.databinding.FragmentLayoutBinding;

import java.util.List;

public class RecentAlbumFragments extends Fragment implements OnClickInterface, AlbumCallbacks {

    private FragmentLayoutBinding binding;
    private AlbumAdapter adapter = new AlbumAdapter(this);
    private AlbumViewModel viewModel;
    private Preferences preferences;

    public RecentAlbumFragments() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLayoutBinding.inflate(inflater);
        preferences = new Preferences(getContext());
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(AlbumViewModel.class);
        viewModel.init(getContext());
        viewModel.getAlbumManager().setSortOrder(MediaStore.Audio.Albums.LAST_YEAR + " DESC");
        viewModel.registerCallbacks(this, getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    private void initRecyclerView(){
        adapter.setContext(getContext());
        adapter.setLayoutGrid(preferences.isDisplayGrid());
        if (preferences.isDisplayGrid())
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        else
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onQueryComplete(List<Album> albumList) {
        adapter.setList(AlbumManager.groupByName(albumList));
    }

    @Override
    public void onClick(Media media) {
        Album album = (Album) media;
        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_host);
        Bundle bundle = new Bundle();
        bundle.putParcelable("Album", album);
        navController.navigate(R.id.album_view_fragment, bundle);
    }
}
