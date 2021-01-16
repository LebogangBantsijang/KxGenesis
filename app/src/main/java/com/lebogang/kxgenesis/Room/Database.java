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

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lebogang.kxgenesis.Room.DataAccessObjects.AlbumHistoryDao;
import com.lebogang.kxgenesis.Room.DataAccessObjects.SongHistoryDao;
import com.lebogang.kxgenesis.Room.DataAccessObjects.PlaylistAudioDao;
import com.lebogang.kxgenesis.Room.DataAccessObjects.PlaylistDao;
import com.lebogang.kxgenesis.Room.Model.AlbumHistory;
import com.lebogang.kxgenesis.Room.Model.SongHistory;
import com.lebogang.kxgenesis.Room.Model.PlaylistAudio;
import com.lebogang.kxgenesis.Room.Model.PlaylistDetails;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@androidx.room.Database(entities = {SongHistory.class, AlbumHistory.class, PlaylistDetails.class, PlaylistAudio.class},
        version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
    public abstract SongHistoryDao songHistoryDao();
    public abstract PlaylistAudioDao playlistAudioDao();
    public abstract PlaylistDao playlistDao();
    public abstract AlbumHistoryDao albumHistoryDao();

    private static volatile Database database;
    public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(4);

     public static Database getDatabase(final Context context){
        if ( database == null){
            synchronized (Database.class){
                database = Room.databaseBuilder(context.getApplicationContext()
                        ,Database.class, "KxGenesisDatabase")
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return database;
    }
}
