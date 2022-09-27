package com.example.photo_album;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import model.Tag;
import model.User;

public class SingleImageActivity extends AppCompatActivity {

    public ImageView imageView;
    public FloatingActionButton floatingActionButton;
    public ListView listView;

    public ArrayList<String> taglist = new ArrayList<>();
    public ArrayAdapter<String> tagAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image);
        //Load content
        update();
        //End Load content
        imageView = findViewById(R.id.singleImageView);
        floatingActionButton = findViewById(R.id.floatingActionButton_tag);
        listView = findViewById(R.id.taglistview);

        tagAdapter = new ArrayAdapter<>(this, R.layout.album_name_text, taglist);
        listView.setAdapter(tagAdapter);
        registerForContextMenu(listView);

        openImage();


        // Add tag to a photo
        floatingActionButton.setOnClickListener(view -> {

            final PopupMenu popupMenu = new PopupMenu(SingleImageActivity.this, floatingActionButton);
            popupMenu.getMenuInflater().inflate(R.menu.tagkeys, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.personKey) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(SingleImageActivity.this);
                    alert.setTitle("Person Tag");
                    alert.setMessage("Please Enter a value for this tag");

                    final EditText input = new EditText(SingleImageActivity.this);
                    alert.setView(input);


                    alert.setPositiveButton("OK", (dialog, whichButton) -> {
                        String value = input.getText().toString();
                        if (MainActivity.user.getCurrentAlbum().getCurrenPhoto().tagExists("Person", value)) {
                            Context context = getApplicationContext();
                            CharSequence text = "Tag already exists. Try another tag.";
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(context, text, duration).show();
                            return;
                        }
                        if (value.isEmpty()) {
                            Context context = getApplicationContext();
                            CharSequence text = "Field cannot be blank";
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(context, text, duration).show();
                            return;
                        }
                        MainActivity.user.getCurrentAlbum().getCurrenPhoto().add_tag("Person", value);

                        try {
                            User.save(MainActivity.user);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        update();
                        tagAdapter.notifyDataSetChanged();
                    });


                    alert.setNegativeButton("CANCEL", (dialog, whichButton) -> dialog.cancel());
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                    return true;
                } else if (itemId == R.id.locationKey) {
                    AlertDialog.Builder locationAlert = new AlertDialog.Builder(SingleImageActivity.this);
                    locationAlert.setTitle("Location Tag");
                    locationAlert.setMessage("Please Enter a value for this tag");

                    final EditText locationInput = new EditText(SingleImageActivity.this);
                    locationAlert.setView(locationInput);


                    locationAlert.setPositiveButton("OK", (dialog, whichButton) -> {
                        String value = locationInput.getText().toString();
                        if (MainActivity.user.getCurrentAlbum().getCurrenPhoto().tagExists("Location", value)) {
                            Context context = getApplicationContext();
                            CharSequence text = "Tag already exists. Try another tag.";
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(context, text, duration).show();
                            return;
                        }
                        if (value.isEmpty()) {
                            Context context = getApplicationContext();
                            CharSequence text = "Field cannot be blank";
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(context, text, duration).show();
                            return;
                        }
                        MainActivity.user.getCurrentAlbum().getCurrenPhoto().add_tag("Location", value);

                        try {
                            User.save(MainActivity.user);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        update();
                        tagAdapter.notifyDataSetChanged();
                    });


                    locationAlert.setNegativeButton("CANCEL", (dialog, whichButton) -> dialog.cancel());
                    AlertDialog locationDialog = locationAlert.create();
                    locationDialog.show();
                    return true;
                }
                return true;
            });

            popupMenu.show();


        });



    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.tagoptions, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int pos = (int) info.id;
        int itemId = item.getItemId();
        if (itemId == R.id.deleteTag) {
            ArrayList<Tag> tagArrayList = MainActivity.user.getCurrentAlbum().getCurrenPhoto().get_taglist();
            MainActivity.user.getCurrentAlbum().getCurrenPhoto().remove_tag(tagArrayList.get(pos).name(), tagArrayList.get(pos).value());
            try {
                User.save(MainActivity.user);
            } catch (IOException e) {
                e.printStackTrace();
            }
            update();
            tagAdapter.notifyDataSetChanged();

            return true;
        } else if (itemId == R.id.movePhoto) {
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void openImage() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            int index = intent.getIntExtra("index",-1);
            Uri uri = Uri.parse(MainActivity.user.getCurrentAlbum().getPhotos().get(index).getFilePath());
            imageView.setImageURI(uri);
        }
    }

    public void update(){
        taglist.clear();
        for(int i = 0; i < MainActivity.user.getCurrentAlbum().getCurrenPhoto().get_taglist().size(); i++){
            taglist.add(MainActivity.user.getCurrentAlbum().getCurrenPhoto().get_taglist().get(i).name() + " | " + MainActivity.user.getCurrentAlbum().getCurrenPhoto().get_taglist().get(i).value());
        }
    }
}
