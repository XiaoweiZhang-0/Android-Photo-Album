package com.example.photo_album;


import android.net.Uri;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import model.Photo;

public class SlideshowActivity extends AppCompatActivity {


    public ImageButton previous, next;
    public ImageView imageView;
    public int currentindex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);


        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        imageView = findViewById(R.id.slideshowImage);


        final int end = MainActivity.user.getCurrentAlbum().getPhotos().size();

        currentindex = 0;

        openInitialImage();


        next.setOnClickListener(view -> {
            currentindex++;

            if(currentindex == end){
                currentindex = 0;
            }

            Photo photo = MainActivity.user.getCurrentAlbum().getPhotos().get(currentindex);
            Uri uri = Uri.parse(photo.getFilePath());
            imageView.setImageURI(uri);
        });

        previous.setOnClickListener(view -> {
            currentindex--;
            if(currentindex == -1){
                currentindex = end-1;
            }
            Photo photo = MainActivity.user.getCurrentAlbum().getPhotos().get(currentindex);
            Uri uri = Uri.parse(photo.getFilePath());
            imageView.setImageURI(uri);
        });





    }

    public void openInitialImage(){
        Uri uri = Uri.parse(MainActivity.user.getCurrentAlbum().getPhotos().get(0).getFilePath());
        imageView.setImageURI(uri);
    }
}
