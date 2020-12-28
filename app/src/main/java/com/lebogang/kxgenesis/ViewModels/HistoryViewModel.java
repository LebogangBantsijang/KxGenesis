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

package com.lebogang.kxgenesis.ViewModels;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lebogang.kxgenesis.Room.Model.Audio;
import com.lebogang.kxgenesis.Room.Repository;

import java.util.List;

public class HistoryViewModel extends ViewModel {

    private final Repository repository;
    private final LiveData<List<Audio>> liveData;

    public HistoryViewModel(Repository repository) {
        this.repository = repository;
        liveData = repository.getAudio();
    }

    public LiveData<List<Audio>> getAudio(){
        return liveData;
    }

    public void clear(){
        repository.clearAudio();
    }


    public static class HistoryFactory implements ViewModelProvider.Factory {
        private final Context context;
        public HistoryFactory(Context context) {
            this.context = context;
        }
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(HistoryViewModel.class)){
                return (T) new HistoryViewModel(new Repository(context));
            }
            throw new IllegalArgumentException();
        }
    }
}
