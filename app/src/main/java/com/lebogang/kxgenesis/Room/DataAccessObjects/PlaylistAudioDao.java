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

package com.lebogang.kxgenesis.Room.DataAccessObjects;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.lebogang.kxgenesis.Room.Model.PlaylistAudio;

import java.util.List;

@Dao
public interface PlaylistAudioDao {

    @Query("SELECT audioId FROM PlaylistsAudioBridge WHERE playlistId =:id")
    LiveData<List<Long>> getPlaylistAudioIds(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAudio(PlaylistAudio playlistAudio);

    @Query("DELETE FROM PlaylistsAudioBridge WHERE playlistId =:id")
    void clearPlaylistAudio(int id);

    @Query("DELETE FROM PlaylistsAudioBridge WHERE playlistId =:playlistId AND audioId =:audioId")
    void deleteAudio(int playlistId, long audioId);

}
