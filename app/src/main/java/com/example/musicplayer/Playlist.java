package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.Services.MediaPlayerBackgroundService;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Playlist extends AppCompatActivity{

    TextView categoryText, timePosition, timeDuration;
    SlidingUpPanelLayout slidingUpPanel;
    MediaPlayerBackgroundService mediaPlayer;
    SeekBar seekBar;
    ImageView categoryImage, songImage, playButton, skip5, replay5, nextTrack, prevTrack;
    Handler handler = new Handler();
    Runnable runnable;
    boolean serviceBound = false;
    SharedPreferences sharedPreferences;
    public final String STORAGE_KEY = "com.example.musicplayer.STORAGE";
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.example.musicplayer.PlayNewAudio";
    public ArrayList<Track> tracksListForTesting = new ArrayList<>();
    TrackViewModel playlistViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        getMusicPlayerSlidingPanelReferences();

        runnable = new Runnable() {
            @Override
            public void run() {
              //  seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        };

        sharedPreferences = getApplicationContext().getSharedPreferences(STORAGE_KEY, Context.MODE_PRIVATE);


        configurePlayerAndPanel(runnable);


        //set up recyclerview and create ViewModel
        categoryImage = findViewById(R.id.category_image);
        categoryText = findViewById(R.id.category_name);
        RecyclerView recyclerView = findViewById(R.id.trackRecyclerView);
        List<Track> tracksList = new ArrayList<>();
        TrackAdapter trackAdapter = new TrackAdapter(tracksList);
        recyclerView.setAdapter(trackAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        playlistViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(TrackViewModel.class);
        initializePlaylist();


        Intent intent = getIntent();
        if(intent.getExtras() != null){
            String categoryNameSelected = intent.getStringExtra("categoryName");
            int categoryImageSelected = intent.getIntExtra("categoryImage",0);
            Log.d("CATEGORY", categoryNameSelected);
            createPlaylist(categoryImageSelected, categoryNameSelected, playlistViewModel, trackAdapter);
            categoryText.setText(categoryNameSelected);
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("ServiceState",serviceBound);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceBound) {
            unbindService(serviceConnection);
            mediaPlayer.stopSelf();
        }
    }

    public int getIndex(){
        Log.d("Current Index",""+ sharedPreferences.getInt("currentIndex",-1));
       return sharedPreferences.getInt("currentIndex",-1);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //Binding to Local Service through binder class
            MediaPlayerBackgroundService.LocalBinder binder = (MediaPlayerBackgroundService.LocalBinder) service;
            mediaPlayer = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    private void playAudio(int audioIndex) {
        //Check is service is active
        if (!serviceBound) {
            //Store audioList index to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("currentIndex",audioIndex);
            editor.apply();

            Intent playerIntent = new Intent(this, MediaPlayerBackgroundService.class);

            //Create ArrayLists for each track field to be stored in service
            ArrayList<Track> playlist = new ArrayList<>();
            playlistViewModel.getFavTracks().observe(this, playlist::addAll);
            ArrayList<String> currentPlaylistTrackNames = new ArrayList<>(), currentPlaylistArtistNames = new ArrayList<>();
            ArrayList<Integer> currentPlaylistImages = new ArrayList<>();
            String log = "";
            for(int i = 0; i < playlist.size(); i++){
                currentPlaylistArtistNames.add(playlist.get(i).artistName);
                currentPlaylistImages.add(playlist.get(i).coverImage);
                currentPlaylistTrackNames.add(playlist.get(i).trackName);
                log = playlist.get(i).trackName + " ";
            }
            Log.d("LIST BEFORE BIND",log);
            //Pass ArrayLists to service
            playerIntent.putStringArrayListExtra("names",currentPlaylistTrackNames).putIntegerArrayListExtra("images",currentPlaylistImages)
                    .putStringArrayListExtra("artistNames",currentPlaylistArtistNames);

            //Pass playlist to service
           // passCurrentPlaylistToService(playlistViewModel, playerIntent);

            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            startService(playerIntent);
        } else {
            //Store the new audioIndex to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("currentIndex",audioIndex);
            editor.apply();

            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            sendBroadcast(broadcastIntent);
        }
    }

    private void passCurrentPlaylistToService(TrackViewModel playlistViewModel, Intent playerIntent) {
        //Create ArrayLists for each track field to be stored in service
        ArrayList<String> currentPlaylistNames= new ArrayList<>();
        ArrayList<Integer> currentPlaylistImages = new ArrayList<>();
        ArrayList<String> currentPlaylistArtistNames = new ArrayList<>();
        playlistViewModel.getFavTracks().observe(this, tracks -> {
            String tracksLog= "";
            for(Track track : tracks){
                tracksLog += track.trackName + " " + track.coverImage + " " +track.artistName;
                currentPlaylistNames.add(track.trackName);
                currentPlaylistImages.add(track.coverImage);
                currentPlaylistArtistNames.add(track.artistName);
            }
            Log.d("Tracks before Binder", tracksLog);
        });
        //Pass ArrayLists to service
        playerIntent.putStringArrayListExtra("names",currentPlaylistArtistNames).putIntegerArrayListExtra("images",currentPlaylistImages)
                .putStringArrayListExtra("artistNames",currentPlaylistArtistNames);
    }



    @SuppressLint("DefaultLocale")
    private String convertTime(int duration) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

    private void getMusicPlayerSlidingPanelReferences(){
          playButton = findViewById(R.id.play_button);
          skip5 = findViewById(R.id.forward5Seconds);
          replay5 = findViewById(R.id.replayLast5Seconds);
          nextTrack = findViewById(R.id.skipToNext);
          prevTrack = findViewById(R.id.skipToPrev);
          songImage = findViewById(R.id.songImage);
          seekBar = findViewById(R.id.seekBar);
          timePosition = findViewById(R.id.timePosition);
          timeDuration = findViewById(R.id.timeDuration);
      }

      public void createPlaylist(int categoryImageSelected, String categoryName, TrackViewModel playlistViewModel, TrackAdapter trackAdapter){
          switch(categoryName){
              case("Pop"):
                  playlistViewModel.getPopTracks().observe(this, new Observer<List<Track>>() {
                      @Override
                      public void onChanged(List<Track> tracks) {
                          Playlist.this.setCoverImage(tracks, categoryImageSelected);
                          trackAdapter.setData(tracks);
                          trackAdapter.notifyDataSetChanged();
                      }
                  });
                  break;
              case("Rock"):
                  playlistViewModel.getRockTracks().observe(this, tracks -> {
                      setCoverImage(tracks,categoryImageSelected);
                      trackAdapter.setData(tracks);
                      trackAdapter.notifyDataSetChanged();
                  });
                  break;
              case("Classical"):
                  playlistViewModel.getClassicalTracks().observe(this, tracks -> {
                      setCoverImage(tracks,categoryImageSelected);
                      trackAdapter.setData(tracks);
                      trackAdapter.notifyDataSetChanged();
                  });
                  break;
              case("Country"):
                  playlistViewModel.getCountryTracks().observe(this, tracks -> {
                      setCoverImage(tracks,categoryImageSelected);
                      trackAdapter.setData(tracks);
                      trackAdapter.notifyDataSetChanged();
                  });
                  break;
              case("Jazz"):
                  playlistViewModel.getJazzTracks().observe(this, tracks -> {
                      setCoverImage(tracks,categoryImageSelected);
                      trackAdapter.setData(tracks);
                      trackAdapter.notifyDataSetChanged();
                  });
                  break;
              case("Hip Hop"):
                  playlistViewModel.getHipHopTracks().observe(this, tracks -> {
                      setCoverImage(tracks,categoryImageSelected);
                      trackAdapter.setData(tracks);
                      trackAdapter.notifyDataSetChanged();
                  });
                  break;
          }
      }

      private void setCoverImage(List<Track> tracks, int categoryImageSelected){
          if(tracks.size() > 1){
              Track coverTrack = tracks.get(0);
              categoryImage.setImageResource(coverTrack.coverImage);
          }else{
              categoryImage.setImageResource(categoryImageSelected);
          }
      }


      private void configurePlayerAndPanel(Runnable runnable){
        new Thread(() -> {
            Playlist.this.configureSlidingPanel();
            Playlist.this.configurePlayButton();
            //configureForwardReplayButtons();
            //configureSeekBar();
        }).start();
    }

      private void configureSlidingPanel(){
          slidingUpPanel = findViewById(R.id.slidingUpPanelPlayListScreen);
          slidingUpPanel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
              @Override
              public void onPanelSlide(View panel, float slideOffset) {
                  songImage.setAlpha(slideOffset);
                  playButton.setAlpha(slideOffset);
                  skip5.setAlpha(slideOffset);
                  replay5.setAlpha(slideOffset);
                  nextTrack.setAlpha(slideOffset);
                  prevTrack.setAlpha(slideOffset);
                  seekBar.setAlpha(slideOffset);
                  timeDuration.setAlpha(slideOffset);
                  timePosition.setAlpha(slideOffset);
              }
              @Override
              public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
              }
          });
      }

      private void initializePlaylist(){
        playlistViewModel.getFavTracks().observe(this, new Observer<List<Track>>() {
            @Override
            public void onChanged(List<Track> tracks) {
                for(Track track : tracks){
                    if(!tracksListForTesting.contains(track)){
                        tracksListForTesting.add(track);
                    }
                }
            }
        });
      }

      private void configurePlayButton(){
          playButton.setOnClickListener(v -> {
            playAudio(getIndex());
            if(serviceBound){
                playButton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
            }else{
                playButton.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
            }
          });
      }

      private void configureForwardReplayButtons(){
        skip5.setOnClickListener(v -> {
            //int currentPosition = mediaPlayer.getCurrentPosition();
            //int duration = mediaPlayer.getDuration();
            /*if(mediaPlayer.isPlaying() && duration != currentPosition){
                currentPosition = currentPosition + 5000;
                timePosition.setText(convertTime(currentPosition));
                mediaPlayer.seekTo(currentPosition);
            }*/
        });
         replay5.setOnClickListener(v -> {
             //int currentPosition = mediaPlayer.getCurrentPosition();
             //int duration = mediaPlayer.getDuration();
            /* if(mediaPlayer.isPlaying() && duration > 5000){
                 currentPosition = currentPosition - 5000;
                 timePosition.setText(convertTime(currentPosition));
                 mediaPlayer.seekTo(currentPosition);
             }*/
         });
      }

      public void configureSeekBar(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
             //       mediaPlayer.seekTo(progress);
                }
           //     timePosition.setText(convertTime(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
      }
}