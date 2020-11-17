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

package com.lebogang.kxgenesis.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayoutMediator;
import com.lebogang.kxgenesis.Adapters.RecentPagerAdapter;
import com.lebogang.kxgenesis.Animations.PagerTransformer;
import com.lebogang.kxgenesis.databinding.FragmentRecentBinding;

public class RecentFragment extends Fragment {
    private FragmentRecentBinding binding;
    private RecentPagerAdapter pagerAdapter;

    public RecentFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRecentBinding.inflate(inflater,container, false);
        pagerAdapter = new RecentPagerAdapter(getChildFragmentManager(), getLifecycle());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupPager();
    }

    private void setupPager(){
        binding.pager.setAdapter(pagerAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.pager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Recent Songs");
                    break;
                case 1:
                    tab.setText("Recent Albums");
                    break;
            }
        }).attach();
        binding.pager.setPageTransformer(new PagerTransformer(getContext()));
        binding.pager.setOffscreenPageLimit(2);
    }

}
