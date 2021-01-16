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
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.lebogang.audiofilemanager.Models.Album;

@Entity(tableName = "AlbumHistory")
public class AlbumHistory {
    @PrimaryKey
    private long albumId;
    @ColumnInfo
    private String albumName;
    @ColumnInfo
    private long date;
    @ColumnInfo
    private String dateModified;
    @Ignore
    private Album album;

    public AlbumHistory(long albumId, String albumName, long date, String dateModified) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.date = date;
        this.dateModified = dateModified;
    }

    public long getAlbumId() {
        return albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public long getDate() {
        return date;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
