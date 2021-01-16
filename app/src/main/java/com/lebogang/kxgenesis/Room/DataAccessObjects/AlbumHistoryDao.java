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
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.lebogang.kxgenesis.Room.Model.AlbumHistory;

import java.util.List;

@Dao
public interface AlbumHistoryDao {

    @Query("SELECT * FROM AlbumHistory ORDER BY date DESC")
    LiveData<List<AlbumHistory>> getAlbumHistory();

    @Query("SELECT albumId FROM AlbumHistory ORDER BY date DESC")
    LiveData<List<Long>> getAlbumIds();

    @Query("SELECT albumName FROM AlbumHistory ORDER BY date DESC")
    LiveData<List<String>> getAlbumNames();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbumHistory(AlbumHistory albumHistory);

    @Update
    void updateAlbumHistory(AlbumHistory albumHistory);

    @Delete
    void deleteAlbumHistory(AlbumHistory albumHistory);

    @Query("DELETE FROM AlbumHistory")
    void clear();
}
