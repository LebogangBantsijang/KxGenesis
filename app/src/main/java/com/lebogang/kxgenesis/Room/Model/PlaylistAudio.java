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

package com.lebogang.kxgenesis.Room.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PlaylistsAudioBridge")
public class PlaylistAudio {
    @PrimaryKey(autoGenerate = true)
    private final int id;
    @ColumnInfo
    private final int playlistId;
    @ColumnInfo
    private final long audioId;

    public PlaylistAudio(int id, int playlistId, long audioId) {
        this.id = id;
        this.playlistId = playlistId;
        this.audioId = audioId;
    }

    public int getId() {
        return id;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public long getAudioId() {
        return audioId;
    }
}
