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

package com.lebogang.kxgenesis.Room.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Audio")
public class Audio {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private long audioId;
    @ColumnInfo
    private long albumId;
    @ColumnInfo
    private long artistId;
    @ColumnInfo
    private String audioName;
    @ColumnInfo
    private String albumName;
    @ColumnInfo
    private String artistName;
    @ColumnInfo
    private String artUri;
    @ColumnInfo
    private long date;
    @ColumnInfo
    private String dateString;

    public Audio(int id, long audioId, long albumId, long artistId, String audioName, String albumName, String artistName, String artUri, long date, String dateString) {
        this.id = id;
        this.audioId = audioId;
        this.albumId = albumId;
        this.artistId = artistId;
        this.audioName = audioName;
        this.albumName = albumName;
        this.artistName = artistName;
        this.artUri = artUri;
        this.date = date;
        this.dateString = dateString;
    }

    public int getId() {
        return id;
    }

    public long getAudioId() {
        return audioId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public long getArtistId() {
        return artistId;
    }

    public String getAudioName() {
        return audioName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtUri() {
        return artUri;
    }

    public long getDate() {
        return date;
    }

    public String getDateString() {
        return dateString;
    }
}
