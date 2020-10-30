package com.lebogang.kxgenesis.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.lebogang.kxgenesis.Fragments.AlbumFragments;
import com.lebogang.kxgenesis.Fragments.ArtistFragments;
import com.lebogang.kxgenesis.Fragments.GenreFragments;
import com.lebogang.kxgenesis.Fragments.PlaylistFragments;
import com.lebogang.kxgenesis.Fragments.RecentAlbumFragments;
import com.lebogang.kxgenesis.Fragments.RecentSongsFragments;
import com.lebogang.kxgenesis.Fragments.SongsFragments;

import java.util.ArrayList;
import java.util.List;

public class RecentPagerAdapter extends FragmentStateAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();

    public RecentPagerAdapter(@NonNull FragmentManager fm, Lifecycle lifecycle) {
        super(fm, lifecycle);
        for (int x = 0; x < 2; x++){
            if (x==0)
                fragmentList.add(new RecentSongsFragments());
            else
                fragmentList.add(new RecentAlbumFragments());
        }
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

}
