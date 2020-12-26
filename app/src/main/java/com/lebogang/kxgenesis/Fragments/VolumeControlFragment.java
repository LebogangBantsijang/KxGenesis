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

import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
        initOtherViews();
        effectsManager = new EffectsManager();
        try {
            effectsManager.setControlStatusListener(this);
            setupView();
        } catch (Exception e) {
            endThisShit();
        }
    }

    private void initOtherViews(){
        binding.backButton.setOnClickListener(v->{
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_host);
            navController.navigateUp();
        });
    }

    private void endThisShit(){
        Toast.makeText(requireActivity(),"Failed to start effect engine", Toast.LENGTH_LONG).show();
        requireActivity().onBackPressed();
    }

    private void setupView() throws Exception {
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
                try {
                    if (effectsManager.isEnabled()){
                        effectsManager.setLoudness(progress);
                        if (progress >= 500){
                            binding.view.setBackgroundColor(getResources().getColor(R.color.colorRed));
                        }
                        else
                            binding.view.setBackgroundColor(getResources().getColor(R.color.colorTranslucent));
                    }
                } catch (Exception e) {
                    endThisShit();
                }
            }
            @Override
            public void onStartTrackingTouch(Croller croller) {
            }
            @Override
            public void onStopTrackingTouch(Croller croller) {
                try {
                    if (!effectsManager.isEnabled())
                        croller.setProgress(0);
                } catch (Exception e) {
                    endThisShit();
                }
            }
        });
    }

    @Override
    public void onControlStatusChange(AudioEffect effect, boolean controlGranted) {
        //if control Granted == true then set switch material to checked
        binding.switchEff.setChecked(controlGranted);
    }
}