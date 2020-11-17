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

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

import com.lebogang.audiofilemanager.GenreManagement.GenreCallbacks;
import com.lebogang.audiofilemanager.GenreManagement.GenreManager;
import com.lebogang.audiofilemanager.Models.Genre;

import java.util.ArrayList;
import java.util.List;

public class GenreViewModel extends ViewModel implements GenreCallbacks{
    private GenreManager genreFileManager;
    private GenreCallbacks callbacks;
    private List<Genre> list = new ArrayList<>();
    private boolean update = false;

    public void init(Context context){
        genreFileManager = new GenreManager(context);
    }

    public void registerCallbacks(GenreCallbacks genreCallBacks, LifecycleOwner owner){
        this.callbacks = genreCallBacks;
        genreFileManager.registerCallbacks(owner, this);
    }

    @Override
    public void onQueryComplete(List<Genre> genreList) {
        if (list.size() != genreList.size()){
            list = genreList;
            callbacks.onQueryComplete(genreList);
        } else {
            for (Genre genre: genreList)
                update = !list.contains(genre);
            if (update){
                list = genreList;
                callbacks.onQueryComplete(list);
            }
        }
    }
}
