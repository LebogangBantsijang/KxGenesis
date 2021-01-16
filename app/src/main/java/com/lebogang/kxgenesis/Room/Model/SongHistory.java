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
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.lebogang.audiofilemanager.Models.Audio;

@Entity(tableName = "SongHistory")
public class SongHistory {
    @PrimaryKey
    private long audioId;
    @ColumnInfo
    private String dateString;
    @ColumnInfo
    private long date;
    @Ignore
    private Audio audio;

    public SongHistory(long audioId, String dateString, long date) {
        this.audioId = audioId;
        this.dateString = dateString;
        this.date = date;
    }

    public long getAudioId() {
        return audioId;
    }

    public String getDateString() {
        return dateString;
    }

    public long getDate() {
        return date;
    }

    public void setAudioId(long audioId) {
        this.audioId = audioId;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public Audio getAudio() {
        return audio;
    }
}
