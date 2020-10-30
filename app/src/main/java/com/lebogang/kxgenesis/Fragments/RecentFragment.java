package com.lebogang.kxgenesis.Fragments;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.media.session.MediaControllerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lebogang.audiofilemanager.AudioManagement.AudioCallbacks;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.audiofilemanager.Models.Media;
import com.lebogang.kxgenesis.ActivityMain;
import com.lebogang.kxgenesis.Adapters.AudioAdapter;
import com.lebogang.kxgenesis.Adapters.OnClickInterface;
import com.lebogang.kxgenesis.Adapters.OnClickOptionsInterface;
import com.lebogang.kxgenesis.Adapters.RecentPagerAdapter;
import com.lebogang.kxgenesis.CallBacksAndAnimations.PagerTransformer;
import com.lebogang.kxgenesis.Dialogs.AudioOptions;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.Utils.AudioIndicator;
import com.lebogang.kxgenesis.Utils.ExtractColor;
import com.lebogang.kxgenesis.ViewModels.AudioViewModel;
import com.lebogang.kxgenesis.databinding.FragmentLayoutBinding;
import com.lebogang.kxgenesis.databinding.FragmentRecentBinding;

import java.util.List;

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
