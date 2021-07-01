package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    GridView categories;
    //arrays containing categories and category names
    String[] categoryNames = {"Pop", "Hip Hop", "Classical", "Country", "Jazz", "Rock"};
    int[] images = {R.drawable.pop, R.drawable.hiphop, R.drawable.classical, R.drawable.country,
    R.drawable.jazz, R.drawable.rock};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        categories = findViewById(R.id.categories_grid_view);
        GridViewAdapter gridViewAdapter = new GridViewAdapter(categoryNames, images, this);
        categories.setAdapter(gridViewAdapter);
        categories.setOnItemClickListener((parent, view, position, id) -> {
            String categoryNameSelected = categoryNames[position];
            int selectedCategory = images[position];
            startActivity(new Intent(MainActivity.this, Playlist.class).putExtra("categoryName",categoryNameSelected).putExtra("categoryImage", selectedCategory));

        });

    }
    public void goToAddScreen(View view) {
        startActivity(new Intent(MainActivity.this, AddTracks.class));
    }

    public class GridViewAdapter extends BaseAdapter{
        private String[] imagenames;
        private int[] imagesPhoto;
        private Context context;
        private LayoutInflater layoutInflater;

        public GridViewAdapter(String[] imagenames, int[] imagesPhoto, Context context) {
            this.imagenames = imagenames;
            this.imagesPhoto = imagesPhoto;
            this.context = context;
            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return imagesPhoto.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if(view == null){
                view = layoutInflater.inflate(R.layout.item_row, viewGroup, false);
            }

            TextView categoryName = view.findViewById(R.id.category_name);
            ImageView categoryImage = view.findViewById(R.id.category_image);
            categoryName.setText(imagenames[position]);
            categoryImage.setImageResource(imagesPhoto[position]);


            return view;
        }
    }
}