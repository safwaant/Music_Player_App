package com.example.musicplayer;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Track.class, version = 2)
public abstract class TrackDatabase extends RoomDatabase {
    private static TrackDatabase instance;

    public abstract TrackDAO trackDAO();

    public static synchronized TrackDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TrackDatabase.class, "track_database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }

}
