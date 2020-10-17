package com.lebogang.kxgenesis.Fragments;

import android.os.Bundle;
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

import com.lebogang.audiofilemanager.AudioManagement.AudioCallbacks;
import com.lebogang.audiofilemanager.Models.Audio;
import com.lebogang.audiofilemanager.Models.Media;
import com.lebogang.kxgenesis.ActivityMain;
import com.lebogang.kxgenesis.Adapters.AudioAdapter;
import com.lebogang.kxgenesis.Adapters.OnClickInterface;
import com.lebogang.kxgenesis.Adapters.OnClickOptionsInterface;
import com.lebogang.kxgenesis.Dialogs.AudioOptions;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.Utils.AudioIndicator;
import com.lebogang.kxgenesis.Utils.ExtractColor;
import com.lebogang.kxgenesis.ViewModels.AudioViewModel;
import com.lebogang.kxgenesis.databinding.FragmentLayoutBinding;

import java.util.List;

public class SongsFragments extends Fragment implements OnClickInterface, OnClickOptionsInterface, AudioCallbacks {

    private FragmentLayoutBinding binding;
    private AudioAdapter adapter = new AudioAdapter(this, this);
    private AudioViewModel viewModel;

    public SongsFragments() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLayoutBinding.inflate(inflater);
        ViewModelProvider provider = new ViewModelProvider(this);
        viewModel = provider.get(AudioViewModel.class);
        viewModel.init(getContext());
        viewModel.registerCallback(this,this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        observer();
    }

    private void initRecyclerView(){
        adapter.setContext(getContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void observer(){
        AudioIndicator.getCurrentItem().observe(getViewLifecycleOwner(),mediaItem -> {
            int color = AudioIndicator.Colors.getDefaultColor();
            adapter.setCurrentID(mediaItem.getId(), color);
            int position = adapter.getItemPosition(mediaItem);
            binding.recyclerView.scrollToPosition(position);
        });
    }

    @Override
    public void onClick(Media media) {
        Audio audio = (Audio) media;
        MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(getActivity());
        if (mediaControllerCompat != null){
            Bundle bundle = new Bundle();
            bundle.putParcelable("Item", audio);
            mediaControllerCompat.getTransportControls().playFromUri(audio.getUri(), bundle);
            bundle.putParcelableArrayList("Items",adapter.getList());
            mediaControllerCompat.getTransportControls().sendCustomAction("Act", bundle);
            ((ActivityMain)getActivity()).setPagerData(adapter.getList());
        }
    }

    @Override
    public void onOptionsClick(Media media) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_host);
        new AudioOptions(getContext(), navController).createDialog((Audio) media);
    }

    @Override
    public void onQueryComplete(List<Audio> audioList) {
        adapter.setList(audioList);
        //Sync music service playlist data
        MediaControllerCompat mediaControllerCompat = MediaControllerCompat.getMediaController(getActivity());
        if (mediaControllerCompat != null){
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("Items",adapter.getList());
            mediaControllerCompat.getTransportControls().sendCustomAction("Act", bundle);
        }
    }
}
