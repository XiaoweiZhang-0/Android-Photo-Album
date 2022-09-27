package com.example.photo_album;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import model.Album;
import model.User;

public class MainActivity extends AppCompatActivity {

    public FloatingActionButton fab;

    public ArrayList<String> albumnames = new ArrayList<>();
    public ArrayAdapter<String> adapter;
    public ListView listview;
    public File filename = new File("/data/data/com.example.photo_album/files/data.dat");
    public int pos;
    public static User user = new User();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            user = User.load();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        update();
        //bind with xml file
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //System.out.println("eufurhiwuehguiewhguiewhgiuewhugewhgewiuhweugh"+user.get_albums().size());

        if(!filename.exists()){
            Context context = this;
            File file = new File(context.getFilesDir(), "data.dat");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        //bind listview and button in activity_main
        listview = findViewById(R.id.listview);
        fab = findViewById(R.id.floatingActionButton);

        adapter = new ArrayAdapter<>(this, R.layout.album_name_text, albumnames);
        listview.setAdapter(adapter);
        registerForContextMenu(listview);

        //set click listener for floating button
        fab.setOnClickListener(this::addAlbum);




        //When Clicked should redirect you to another activity with that albums photos
        listview.setOnItemClickListener((adapterView, view, i, l) -> {

            Album currentAlbum = user.get_albums().get(i);
            user.setCurrentAlbum(currentAlbum);
            Intent goToCurrentAlbum = new Intent(MainActivity.this, AlbumActivity.class);
            startActivity(goToCurrentAlbum);

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.albumoptions, menu);
    }

    //rename and delete album
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        pos = info.position;
        int itemId = item.getItemId();
        if (itemId == R.id.renameAlbum) {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Rename");

            final EditText input = new EditText(MainActivity.this);
            alert.setView(input);


            alert.setPositiveButton("OK", (dialog, whichButton) -> {
                String renamed = input.getText().toString();
                if (user.album_exist(renamed)) {
                    Context context = getApplicationContext();
                    CharSequence text = "Album already exists. Try another name.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(context, text, duration).show();
                    return;
                }
                if (renamed.isEmpty()) {
                    Context context = getApplicationContext();
                    CharSequence text = "Field cannot be blank";
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(context, text, duration).show();
                    return;
                }
                user.get_albums().get(pos).SetName(renamed);

                try {
                    User.save(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                update();
                adapter.notifyDataSetChanged();
            });


            alert.setNegativeButton("CANCEL", (dialog, whichButton) -> dialog.cancel());
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
            return true;
        } else if (itemId == R.id.deleteAlbum) {
            user.delete_album(pos);
            try {
                User.save(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
            update();
            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "Album Deleted", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }


    //search activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search_icon) {
            Intent goToSearch = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(goToSearch);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause(){
        super.onPause();

        try {
            User.save(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            user = User.load();
            adapter.notifyDataSetChanged();
            listview.setAdapter(adapter);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        try {
            user = User.load();
            adapter.notifyDataSetChanged();
            listview.setAdapter(adapter);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addAlbum(View view){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Enter the name of the album: ");

        final EditText input = new EditText(this);

        alertDialogBuilder.setView(input);
        alertDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> {

        });
        alertDialogBuilder.setPositiveButton("OK", (dialog, which) -> {
            String albumname = input.getText().toString().trim();
            if(albumname.isEmpty()){
                Context context = getApplicationContext();
                CharSequence text = "Field cannot be blank";
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context, text, duration).show();
                return;
            }

            if(user.album_exist(albumname)){
                Context context = getApplicationContext();
                CharSequence text = "Album already exists!";
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context, text, duration).show();
                return;
            }

            Album album = new Album(albumname);
            user.add_album(album);

            try {
                User.save(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
            update();
            adapter.notifyDataSetChanged();
        });
        alertDialogBuilder.show();



    }

    public void update(){
        albumnames.clear();
        for(int i=0; i <user.get_albums().size(); i++){
            String albumname = user.get_albums().get(i).Getname();
            albumnames.add(albumname);
        }
    }
}