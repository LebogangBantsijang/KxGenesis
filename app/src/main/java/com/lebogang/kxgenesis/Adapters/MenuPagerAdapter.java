package com.lebogang.kxgenesis.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.lebogang.kxgenesis.Fragments.AlbumFragments;
import com.lebogang.kxgenesis.Fragments.ArtistFragments;
import com.lebogang.kxgenesis.Fragments.GenreFragments;
import com.lebogang.kxgenesis.Fragments.PlaylistFragments;
import com.lebogang.kxgenesis.Fragments.SongsFragments;

import java.util.ArrayList;
import java.util.List;

public class MenuPagerAdapter extends FragmentStateAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();

    public MenuPagerAdapter(@NonNull FragmentManager fm, Lifecycle lifecycle) {
        super(fm, lifecycle);
        for (int x = 0; x < 5; x++){
            if (x==0)
                fragmentList.add(new SongsFragments());
            else if (x==1)
                fragmentList.add(new AlbumFragments());
            else if (x==2)
                fragmentList.add(new ArtistFragments());
            else if (x==3)
                fragmentList.add(new PlaylistFragments());
            else
                fragmentList.add(new GenreFragments());
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
