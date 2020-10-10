package com.lebogang.kxgenesis.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayoutMediator;
import com.lebogang.audiofilemanager.Models.MediaItem;
import com.lebogang.kxgenesis.Adapters.GeneralItemClick;
import com.lebogang.kxgenesis.Adapters.MenuPagerAdapter;
import com.lebogang.kxgenesis.CallBacksAndAnimations.PagerTransformer;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements GeneralItemClick {

    private FragmentHomeBinding binding;
    private MenuPagerAdapter menuPagerAdapter;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding= FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewPager();
    }

    private void setupViewPager(){
        menuPagerAdapter = new MenuPagerAdapter(getChildFragmentManager(), getLifecycle());
        binding.viewpager.setAdapter(menuPagerAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewpager,
                (tab, position) -> {
                    switch (position){
                        case 0: tab.setIcon(R.drawable.ic_musical_notes); break;
                        case 1: tab.setIcon(R.drawable.ic_music_record); break;
                        case 2: tab.setIcon(R.drawable.ic_microphone); break;
                        case 3: tab.setIcon(R.drawable.ic_playlist); break;
                        case 4: tab.setIcon(R.drawable.ic_guitar); break;
                    }
                }).attach();
        binding.viewpager.setPageTransformer(new PagerTransformer(getContext()));
        binding.viewpager.setOffscreenPageLimit(5);
    }

    @Override
    public void onClick(MediaItem mediaItem) {

    }

    @Override
    public void onOptionsClick(MediaItem mediaItem) {

    }
}
