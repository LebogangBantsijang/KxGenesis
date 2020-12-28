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

package com.lebogang.kxgenesis.Room;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.lebogang.kxgenesis.Room.DataAccessObjects.AudioDao;
import com.lebogang.kxgenesis.Room.Model.Audio;

import java.util.List;

public class Repository {
    private final AudioDao audioDao;

    public Repository(Context context) {
        Database database = Database.getDatabase(context);
        this.audioDao = database.audioDao();
    }

    public void addAudio(Audio audio){
        Database.EXECUTOR_SERVICE.execute(()->{
            audioDao.addAudio(audio);
        });
    }

    public void removeAudio(Audio audio){
        Database.EXECUTOR_SERVICE.execute(()->{
            audioDao.removeAudio(audio);
        });
    }

    public void clearAudio(){
        Database.EXECUTOR_SERVICE.execute(audioDao::clear);
    }

    public LiveData<List<Audio>> getAudio(){
        return audioDao.getAudio();
    }

    public LiveData<List<Long>> getAudioIds(){
        return audioDao.getAudioIds();
    }

    public LiveData<List<Long>> getArtistIds(){
        return audioDao.getArtistIds();
    }

    public LiveData<List<Long>> getAlbumIds(){
        return audioDao.getAlbumIds();
    }
}
