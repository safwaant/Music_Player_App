package com.example.musicplayer;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "track_table")
public class Track {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo
    public String trackType;

    @ColumnInfo
    public String artistName;

    @ColumnInfo
    public boolean trackFavorite;

    @ColumnInfo
    public String trackName;

    @ColumnInfo
    public int coverImage;

}
