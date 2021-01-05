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

package com.lebogang.kxgenesis.Dialogs;

import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.lebogang.kxgenesis.Adapters.SelectPlayerAdapter;
import com.lebogang.kxgenesis.AppUtils.AppSettings;
import com.lebogang.kxgenesis.AppUtils.SelectPlayerOnClick;
import com.lebogang.kxgenesis.MainActivity;
import com.lebogang.kxgenesis.databinding.DialogPlayerSelectBinding;

public class PlayerDialog implements SelectPlayerOnClick {
    private final MainActivity activity;
    private AlertDialog dialog;
    private DialogPlayerSelectBinding binding;
    private final SelectPlayerAdapter adapter = new SelectPlayerAdapter(this);

    public PlayerDialog(AppCompatActivity activity) {
        this.activity = (MainActivity) activity;
    }

    public void createDialog(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
        LayoutInflater inflater = LayoutInflater.from(activity);
        binding = DialogPlayerSelectBinding.inflate(inflater);
        initViews();
        builder.setView(binding.getRoot());
        builder.setNegativeButton("Close", null);
        dialog = builder.create();
        dialog.show();
    }

    private void initViews(){
        binding.viewPger.setAdapter(adapter);
        int index = AppSettings.getSelectedPlayerIndex(activity);
        binding.viewPger.setCurrentItem(index);
    }

    @Override
    public void onClick(int position) {
        AppSettings.setPlayer(activity,position);
        activity.setBottomSheetBehavior(true);
        dialog.dismiss();
    }
}
