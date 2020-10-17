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

import com.lebogang.audiofilemanager.GenreManagement.GenreCallbacks;
import com.lebogang.audiofilemanager.Models.Genre;
import com.lebogang.audiofilemanager.Models.Media;
import com.lebogang.kxgenesis.Adapters.GenreAdapter;
import com.lebogang.kxgenesis.Adapters.OnClickInterface;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.ViewModels.GenreViewModel;
import com.lebogang.kxgenesis.databinding.FragmentLayoutBinding;

import java.util.List;

public class GenreFragments extends Fragment implements OnClickInterface, GenreCallbacks {
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
        boolean type = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getBoolean("GridLayout", true);
        adapter.setLayoutGrid(type);
        if (type)
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        else
            binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(Media media) {
        Genre genre = (Genre) media;
        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_host);
        Bundle bundle = new Bundle();
        bundle.putParcelable("Genre", genre);
        navController.navigate(R.id.genre_view_fragment, bundle);
    }

    @Override
    public void onQueryComplete(List<Genre> genreList) {
        adapter.setList(genreList);
    }
}
