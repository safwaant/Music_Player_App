package com.example.musicplayer;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TrackDAO {

    @Insert
    void insert (Track track);

    @Update
    void update(Track track);

    @Delete
    void delete(Track track);

    @Query("SELECT * FROM track_table")
    LiveData<List<Track>> getAllTracks();

    @Query("SELECT * FROM track_table WHERE trackFavorite = 1")
    LiveData<List<Track>> getFavTracks();

    @Query("SELECT * FROM track_table WHERE trackType = 'Pop'")
    LiveData<List<Track>> getPopTracks();

    @Query("SELECT * FROM track_table WHERE trackType = 'Hip Hop'")
    LiveData<List<Track>> getHipHopTracks();

    @Query("SELECT * FROM track_table WHERE trackType = 'Country'")
    LiveData<List<Track>> getCountryTracks();

    @Query("SELECT * FROM track_table WHERE trackType = 'Jazz'")
    LiveData<List<Track>> getJazzTracks();

    @Query("SELECT * FROM track_table WHERE trackType = 'Classical'")
    LiveData<List<Track>> getClassicalTracks();

    @Query("SELECT * FROM track_table WHERE trackType = 'Rock'")
    LiveData<List<Track>> getRockTracks();

    @Query("SELECT * FROM track_table WHERE trackFavorite = 1")
    LiveData<List<Track>> getFavoriteTracks();
}
