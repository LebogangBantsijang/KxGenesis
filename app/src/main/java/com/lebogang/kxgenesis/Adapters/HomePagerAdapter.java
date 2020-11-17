/*
 * Copyright (c) 2020. Lebogang Bantsijang
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import com.lebogang.kxgenesis.Fragments.SongsFragments;

import java.util.ArrayList;
import java.util.List;

public class HomePagerAdapter extends FragmentStateAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();

    public HomePagerAdapter(@NonNull FragmentManager fm, Lifecycle lifecycle) {
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
