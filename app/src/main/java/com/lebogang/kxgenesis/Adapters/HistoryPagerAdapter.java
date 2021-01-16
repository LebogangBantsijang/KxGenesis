/*
 * Copyright (c) 2021. Lebogang Bantsijang
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
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.lebogang.kxgenesis.Fragments.AlbumHistoryPagerFragment;
import com.lebogang.kxgenesis.Fragments.SongHistoryPagerFragment;

import java.util.ArrayList;
import java.util.List;

public class HistoryPagerAdapter extends FragmentStateAdapter {
    private final List<Fragment> list = new ArrayList<>();

    public HistoryPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
        list.add(new SongHistoryPagerFragment());
        list.add(new AlbumHistoryPagerFragment());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
