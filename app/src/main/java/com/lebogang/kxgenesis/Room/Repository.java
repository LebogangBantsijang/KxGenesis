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

import com.lebogang.kxgenesis.Room.DataAccessObjects.AlbumHistoryDao;
import com.lebogang.kxgenesis.Room.DataAccessObjects.SongHistoryDao;
import com.lebogang.kxgenesis.Room.DataAccessObjects.PlaylistAudioDao;
import com.lebogang.kxgenesis.Room.DataAccessObjects.PlaylistDao;
import com.lebogang.kxgenesis.Room.Model.AlbumHistory;
import com.lebogang.kxgenesis.Room.Model.SongHistory;
import com.lebogang.kxgenesis.Room.Model.PlaylistAudio;
import com.lebogang.kxgenesis.Room.Model.PlaylistDetails;

import java.util.List;

public class Repository {
    private final SongHistoryDao songHistoryDao;
    private AlbumHistoryDao albumHistoryDao;
    private final PlaylistDao playlistDao;
    private final PlaylistAudioDao playlistAudioDao;

    public Repository(Context context) {
        Database database = Database.getDatabase(context);
        this.songHistoryDao = database.songHistoryDao();
        this.playlistAudioDao = database.playlistAudioDao();
        this.playlistDao = database.playlistDao();
        this.albumHistoryDao = database.albumHistoryDao();
    }

    /*Song History*/
    public void addSongHistory(SongHistory songHistory){
        Database.EXECUTOR_SERVICE.execute(()->{
            songHistoryDao.addAudio(songHistory);
        });
    }

    public void removeSongHistory(SongHistory songHistory){
        Database.EXECUTOR_SERVICE.execute(()->{
            songHistoryDao.removeAudio(songHistory);
        });
    }

    public void clearSongHistory(){
        Database.EXECUTOR_SERVICE.execute(songHistoryDao::clear);
    }

    public LiveData<List<SongHistory>> getSongHistory(){
        return songHistoryDao.getAudio();
    }

    public LiveData<List<Long>> getSongHistoryAudioIds(){
        return songHistoryDao.getAudioIds();
    }

    /*Album History*/
    public void addAlbumHistory(AlbumHistory albumHistory){
        Database.EXECUTOR_SERVICE.execute(()->{
            albumHistoryDao.insertAlbumHistory(albumHistory);
        });
    }

    public void removeAlbumHistory(AlbumHistory albumHistory){
        Database.EXECUTOR_SERVICE.execute(()->{
            albumHistoryDao.deleteAlbumHistory(albumHistory);
        });
    }

    public void clearAlbumHistory(){
        Database.EXECUTOR_SERVICE.execute(albumHistoryDao::clear);
    }

    public LiveData<List<AlbumHistory>> getAlbumHistory(){
        return albumHistoryDao.getAlbumHistory();
    }

    public LiveData<List<Long>> getAlbumHistoryAudioIds(){
        return albumHistoryDao.getAlbumIds();
    }

    /*Playlists*/
    public LiveData<List<PlaylistDetails>> getPlaylists(){
        return playlistDao.getPlaylist();
    }

    public void updatePlaylist(PlaylistDetails details){
        Database.EXECUTOR_SERVICE.execute(()->{
            playlistDao.updatePlaylist(details);
        });
    }

    public void insertPlaylist(PlaylistDetails details){
        Database.EXECUTOR_SERVICE.execute(()->{
            playlistDao.insertPlaylist(details);
        });
    }

    public void deletePlaylist(PlaylistDetails details){
        Database.EXECUTOR_SERVICE.execute(()->{
            playlistDao.deletePlaylist(details);
        });
    }

    public void clearPlaylists(){
        Database.EXECUTOR_SERVICE.execute(playlistDao::clearPlaylists);
    }

    /*Playlist Audio*/
    public LiveData<List<Long>> getPlaylistAudioIds(int playlistId){
        return playlistAudioDao.getPlaylistAudioIds(playlistId);
    }

    public void addPlaylistAudio(PlaylistAudio playlistAudio){
        Database.EXECUTOR_SERVICE.submit(()->{
            playlistAudioDao.addAudio(playlistAudio);
        });
    }

    public void clearPlaylistAudio(int playlistId){
        Database.EXECUTOR_SERVICE.execute(()->{
            playlistAudioDao.clearPlaylistAudio(playlistId);
        });
    }

    public void deletePlaylistAudio(int playlistId, long audioId){
        Database.EXECUTOR_SERVICE.execute(()->{
            playlistAudioDao.deleteAudio(playlistId, audioId);
        });
    }
}
