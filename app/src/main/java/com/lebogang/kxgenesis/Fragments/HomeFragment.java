package com.lebogang.kxgenesis.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayoutMediator;
import com.lebogang.kxgenesis.Adapters.HomePagerAdapter;
import com.lebogang.kxgenesis.CallBacksAndAnimations.PagerTransformer;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomePagerAdapter homePagerAdapter;

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
        homePagerAdapter = new HomePagerAdapter(getChildFragmentManager(), getLifecycle());
        binding.viewpager.setAdapter(homePagerAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewpager,
                (tab, position) -> {
                    switch (position){
                        case 0: tab.setIcon(R.drawable.ic_musical_notes);
                            tab.setText("Songs");
                        break;
                        case 1: tab.setIcon(R.drawable.ic_music_record);
                            tab.setText("Albums");
                        break;
                        case 2: tab.setIcon(R.drawable.ic_microphone);
                            tab.setText("Artists");
                        break;
                        case 3: tab.setIcon(R.drawable.ic_playlist);
                            tab.setText("Playlists");
                        break;
                        case 4: tab.setIcon(R.drawable.ic_guitar);
                            tab.setText("Genres");
                        break;
                    }
                }).attach();
        binding.viewpager.setPageTransformer(new PagerTransformer(getContext()));
        binding.viewpager.setOffscreenPageLimit(5);
    }

}
