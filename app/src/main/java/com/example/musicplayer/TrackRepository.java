package com.example.musicplayer;

import android.app.Application;
import android.app.AsyncNotedAppOp;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.ListIterator;

public class TrackRepository {
    private LiveData<List<Track>> allTracks;
    Context context;

    public TrackRepository(Context context){
        this.context = context;
    }

    public void insert(Track track){
        AsyncTask.execute(() -> TrackDatabase.getInstance(context).trackDAO().insert(track));
    }

    public void update(Track track){
        AsyncTask.execute(() -> TrackDatabase.getInstance(context).trackDAO().update(track));
    }

    public void delete(Track track){
        AsyncTask.execute(() -> TrackDatabase.getInstance(context).trackDAO().delete(track));
    }

    public LiveData<List<Track>> getAllTracks(){
        return TrackDatabase.getInstance(context).trackDAO().getAllTracks();
    }

    public LiveData<List<Track>> getFavTracks(){
        return TrackDatabase.getInstance(context).trackDAO().getFavTracks();
    }

    public LiveData<List<Track>> getPopTracks(){
       return TrackDatabase.getInstance(context).trackDAO().getPopTracks();
    }

    public LiveData<List<Track>> getHipHopTracks(){
        return TrackDatabase.getInstance(context).trackDAO().getHipHopTracks();
    }

    public LiveData<List<Track>> getCountryTracks(){
        return TrackDatabase.getInstance(context).trackDAO().getCountryTracks();
    }

    public LiveData<List<Track>> getClassicalTracks(){
        return TrackDatabase.getInstance(context).trackDAO().getClassicalTracks();
    }

    public LiveData<List<Track>> getJazzTracks(){
        return TrackDatabase.getInstance(context).trackDAO().getJazzTracks();
    }

    public LiveData<List<Track>> getRockTracks(){
        return TrackDatabase.getInstance(context).trackDAO().getRockTracks();
    }

}
