package com.example.musicplayer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TrackViewModel extends AndroidViewModel {
    private TrackRepository trackRepository;
    private LiveData<List<Track>> popTracks, hipHopTracks, countryTracks, classicalTracks, rockTracks, jazzTracks, favTracks, allTracks;

    public TrackViewModel(@NonNull Application application) {
        super(application);
        trackRepository = new TrackRepository(application.getApplicationContext());
        popTracks = trackRepository.getPopTracks();
        hipHopTracks = trackRepository.getHipHopTracks();
        countryTracks = trackRepository.getCountryTracks();
        classicalTracks = trackRepository.getClassicalTracks();
        rockTracks = trackRepository.getRockTracks();
        jazzTracks = trackRepository.getJazzTracks();
        favTracks = trackRepository.getFavTracks();
        allTracks = trackRepository.getAllTracks();
    }

    public void insertTrack(Track track){
        trackRepository.insert(track);
    }

    public void deleteTrack(Track track){
        trackRepository.delete(track);
    }

    public void update(Track track){trackRepository.update(track);}

   /* public LiveData<List<Track>> getAllTracks{
        return allTracks;
    }*/

    public LiveData<List<Track>> getFavTracks(){return favTracks;}

    public LiveData<List<Track>> getPopTracks(){
        return popTracks;
    }

    public LiveData<List<Track>> getHipHopTracks(){
        return hipHopTracks;
    }
    public LiveData<List<Track>> getCountryTracks(){
        return countryTracks;
    }
    public LiveData<List<Track>> getClassicalTracks(){
        return classicalTracks;
    }
    public LiveData<List<Track>> getJazzTracks(){
        return jazzTracks;
    }
    public LiveData<List<Track>> getRockTracks(){
        return rockTracks;
    }
}
