package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class AddTracks extends AppCompatActivity {
    EditText trackNameField, trackTypeField;
    Map<String, Integer> songImagesCorrespondence = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tracks);
        trackNameField = findViewById(R.id.track_name_field);
        trackTypeField = findViewById(R.id.track_type_field);
        songImagesCorrespondence.put("Thunder", R.drawable.thunder);
        songImagesCorrespondence.put("Alone", R.drawable.alone);
        songImagesCorrespondence.put("Despacito", R.drawable.despacito);
        songImagesCorrespondence.put("Lai Lai Lai", R.drawable.lailailai);
        songImagesCorrespondence.put("See You Again", R.drawable.seeyouagain);
        songImagesCorrespondence.put("Get You to the Moon", R.drawable.getyoutothemoon);
        songImagesCorrespondence.put("Taki Taki Taki", R.drawable.takitakitaki);
        songImagesCorrespondence.put("Faded", R.drawable.faded);
    }


    public void addTrackToDatabase(View view) {
        String trackNameFromEditText = trackNameField.getText().toString();
        String trackTypeFromEditText = trackTypeField.getText().toString();
        TrackViewModel addTrackViewModel= new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(TrackViewModel.class);
        Track track = new Track();
        track.trackName = trackNameFromEditText;
        track.coverImage = getTrackImage(track);
        track.trackType = trackTypeFromEditText;
        track.trackFavorite = true;
        addTrackViewModel.insertTrack(track);
        trackNameField.getText().clear();
        trackTypeField.getText().clear();
        Toast.makeText(this,"Track Added",Toast.LENGTH_SHORT).show();
    }

    private int getTrackImage(Track track) {
        String trackName = track.trackName;
        int image = 0;
        if(songImagesCorrespondence.containsKey(trackName)){
            image = songImagesCorrespondence.get(trackName);
        }
        return image;
    }
}