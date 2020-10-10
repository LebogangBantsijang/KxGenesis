package com.lebogang.kxgenesis.Fragments;

import android.media.audiofx.AudioEffect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CompoundButton;

import com.lebogang.kxgenesis.Equalizer.EffectsManager;
import com.lebogang.kxgenesis.R;
import com.lebogang.kxgenesis.databinding.FragmentVolumeBinding;
import com.sdsmdg.harjot.crollerTest.Croller;
import com.sdsmdg.harjot.crollerTest.OnCrollerChangeListener;

public class VolumeControlFragment extends Fragment implements AudioEffect.OnControlStatusChangeListener {

    private FragmentVolumeBinding binding;
    private EffectsManager effectsManager;

    public VolumeControlFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVolumeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        effectsManager = new EffectsManager();
        effectsManager.setControlStatusListener(this);
        setupView();
    }

    private void setupView(){
        binding.switchEff.setChecked(effectsManager.isEnabled());
        binding.switchEff.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                effectsManager.enable();
                binding.volumeCroller.setProgress(effectsManager.getLoud());
                if (effectsManager.getLoud() >= 500){
                    binding.view.setBackgroundColor(getResources().getColor(R.color.colorRed));
                }
            }
            else {
                effectsManager.disable();
                binding.volumeCroller.setProgress(0);
                binding.view.setBackgroundColor(getResources().getColor(R.color.colorTranslucent));
            }
        });

        binding.volumeCroller.setProgress(effectsManager.getLoud());
        binding.volumeCroller.setOnCrollerChangeListener(new OnCrollerChangeListener() {
            @Override
            public void onProgressChanged(Croller croller, int progress) {
                if (effectsManager.isEnabled()){
                    effectsManager.setLoudness(progress);
                    if (progress >= 500){
                        binding.view.setBackgroundColor(getResources().getColor(R.color.colorRed));
                    }
                    else
                        binding.view.setBackgroundColor(getResources().getColor(R.color.colorTranslucent));
                }
            }
            @Override
            public void onStartTrackingTouch(Croller croller) {
            }
            @Override
            public void onStopTrackingTouch(Croller croller) {
                if (!effectsManager.isEnabled())
                    croller.setProgress(0);
            }
        });
    }

    @Override
    public void onControlStatusChange(AudioEffect effect, boolean controlGranted) {
        //if control Granted == true then set switch material to checked
        binding.switchEff.setChecked(controlGranted);
    }
}