package com.example.musicplayer;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder>{
    private List<Track> tracks;


    public TrackAdapter(List<Track> tracks){
       this.tracks = tracks;
    }

    public void setData(List<Track> tracks){
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public TrackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull TrackAdapter.ViewHolder holder, int position) {
        Track currentTrack = tracks.get(position);
        holder.trackName.setText(currentTrack.trackName);
        holder.trackImg.setImageResource(currentTrack.coverImage);

    }

    public Track trackAt(int position){
        return tracks.get(position);
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView trackName;
        ImageView trackImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trackName = itemView.findViewById(R.id.track_name);
            trackImg = itemView.findViewById(R.id.track_img);
        }
    }
}
