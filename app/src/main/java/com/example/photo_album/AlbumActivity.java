package com.example.photo_album;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import model.AlbumImageAdapter;
import model.Photo;
import model.User;

public class AlbumActivity extends AppCompatActivity {
    public final int REQUEST_CODE = 0;
    public ArrayList<Photo> photoList = new ArrayList<>();
    public FloatingActionButton fab;
    public GridView gridView;
    public AlbumImageAdapter albumImageAdapter;
    public int pos;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);


        gridView = findViewById(R.id.gridview_album);
        fab = findViewById(R.id.floatingActionButton_album);
        albumImageAdapter = new AlbumImageAdapter(AlbumActivity.this, photoList);
        gridView.setAdapter(albumImageAdapter);
        registerForContextMenu(gridView);

        update();

        // Add a photo
        fab.setOnClickListener(view -> {
            Intent addPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            addPhoto.addCategory(Intent.CATEGORY_OPENABLE);
            addPhoto.setType("image/*");
            AlbumActivity.this.startActivityForResult(addPhoto, REQUEST_CODE);

        });

        //entry point to single image
        gridView.setOnItemClickListener((adapterView, view, index, l) -> {
            Photo photo = MainActivity.user.getCurrentAlbum().getPhotos().get(index);
            MainActivity.user.getCurrentAlbum().setCurrentPhoto(photo);
            Intent viewFullImage = new Intent(AlbumActivity.this, SingleImageActivity.class);
            viewFullImage.putExtra("index", index);
            startActivity(viewFullImage);
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.photooptions, menu);
    }


    //move or delete photo
    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        pos = info.position;

        int itemId = item.getItemId();
        if (itemId == R.id.deletePhoto) {
            MainActivity.user.getCurrentAlbum().remove_photo(pos);
            try {
                User.save(MainActivity.user);
            } catch (IOException e) {
                e.printStackTrace();
            }
            gridView = findViewById(R.id.gridview_album);
            update();
            albumImageAdapter.notifyDataSetChanged();
            gridView.invalidateViews();
            gridView.setAdapter(albumImageAdapter);
            Toast.makeText(getApplicationContext(), "Photo Deleted", Toast.LENGTH_SHORT).show();

            return true;
        } else if (itemId == R.id.movePhoto) {
            final PopupMenu popupMenu = new PopupMenu(AlbumActivity.this, gridView);
            for (int i = 0; i < MainActivity.user.get_albums().size(); i++) {
                popupMenu.getMenu().add(Menu.NONE, i, Menu.NONE, MainActivity.user.get_albums().get(i).Getname());
            }


            popupMenu.setOnMenuItemClickListener(menuItem -> {
                Photo photo = MainActivity.user.getCurrentAlbum().getPhotos().get(pos);
                MainActivity.user.get_albums().get(menuItem.getItemId()).add_photo(photo);
                MainActivity.user.getCurrentAlbum().remove_photo(pos);

                try {
                    User.save(MainActivity.user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                gridView = findViewById(R.id.gridview_album);
                update();
                albumImageAdapter.notifyDataSetChanged();
                gridView.invalidateViews();
                gridView.setAdapter(albumImageAdapter);

                return true;
            });
            popupMenu.show();

            return true;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            Uri uri;
            uri = data.getData();
            String photopath = uri.toString();
            Photo photo = new Photo(photopath);
            MainActivity.user.getCurrentAlbum().add_photo(photo);

            try {
                User.save(MainActivity.user);
            } catch (IOException e) {
                e.printStackTrace();
            }
            gridView = findViewById(R.id.gridview_album);
            update();

            albumImageAdapter.notifyDataSetChanged();
            gridView.setAdapter(albumImageAdapter);
        }
    }

    public void update(){
        photoList.clear();
        photoList.addAll(MainActivity.user.getCurrentAlbum().getPhotos());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.slideshow) {
            if(MainActivity.user.getCurrentAlbum().getPhotos().size() == 0){
                AlertDialog alertDialog = new AlertDialog.Builder(AlbumActivity.this).create();
                alertDialog.setTitle("Empty Album");
                alertDialog.setMessage("Add a photo to an album to view a slideshow");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();
            }
            else {
                Intent goToSlideshow = new Intent(AlbumActivity.this, SlideshowActivity.class);
                startActivity(goToSlideshow);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}