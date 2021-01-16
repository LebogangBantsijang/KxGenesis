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

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Playlists")
public class PlaylistDetails implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private final int id;
    @ColumnInfo
    private String playlistName;
    @ColumnInfo
    private String dateCreated;
    @ColumnInfo(defaultValue = "no art")
    private String artUri;


    public PlaylistDetails(int id, String playlistName, String dateCreated, @Nullable String artUri) {
        this.id = id;
        this.playlistName = playlistName;
        this.dateCreated = dateCreated;
        this.artUri = artUri;
    }

    protected PlaylistDetails(Parcel in) {
        id = in.readInt();
        playlistName = in.readString();
        dateCreated = in.readString();
        artUri = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(playlistName);
        dest.writeString(dateCreated);
        dest.writeString(artUri);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlaylistDetails> CREATOR = new Creator<PlaylistDetails>() {
        @Override
        public PlaylistDetails createFromParcel(Parcel in) {
            return new PlaylistDetails(in);
        }

        @Override
        public PlaylistDetails[] newArray(int size) {
            return new PlaylistDetails[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getArtUri() {
        return artUri;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setArtUri(String artUri) {
        this.artUri = artUri;
    }
}
