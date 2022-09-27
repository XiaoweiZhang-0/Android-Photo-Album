package com.example.photo_album;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import model.AlbumImageAdapter;
import model.CustomSpinner;
import model.Photo;

public class SearchActivity extends AppCompatActivity {

    public CustomSpinner customSpinner;
    public GridView gridView;
    public SearchView searchView;

    public ArrayList<Photo> photoList = new ArrayList<>();
    public AlbumImageAdapter albumImageAdapter;

    public String[] spinneroptions = {"Person", "Location", "Search All"};
    public int option;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // System.out.println("------------------------------------------------------------------------------------NOT");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        customSpinner = findViewById(R.id.tagspinner);
        gridView = findViewById(R.id.gridview_search);
        searchView = findViewById(R.id.searchField);
        albumImageAdapter = new AlbumImageAdapter(SearchActivity.this, photoList);
        gridView.setAdapter(albumImageAdapter);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, spinneroptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customSpinner.setAdapter(spinnerAdapter);

        customSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                option = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                photoList.clear();
                if (option == 0) {
                    photoList.addAll(MainActivity.user.searchCertainTags(spinneroptions[option], s));
                   // System.out.println("ewfhgwefwgufgweiyufgewuifgewifgewiugfiewf"+photoList.size());
                    gridView = findViewById(R.id.gridview_search);
                    albumImageAdapter = new AlbumImageAdapter(SearchActivity.this, photoList);
                    gridView.setAdapter(albumImageAdapter);
                } else if (option == 1) {
                    photoList.addAll(MainActivity.user.searchCertainTags(spinneroptions[option], s));
                    gridView = findViewById(R.id.gridview_search);
                    albumImageAdapter = new AlbumImageAdapter(SearchActivity.this, photoList);
                    gridView.setAdapter(albumImageAdapter);
                } else {
                    photoList.addAll(MainActivity.user.searchAllTags(s));
                    gridView = findViewById(R.id.gridview_search);
                    albumImageAdapter = new AlbumImageAdapter(SearchActivity.this, photoList);
                    gridView.setAdapter(albumImageAdapter);
                }
                return true;
            }
        });


        gridView = findViewById(R.id.gridview_search);
        albumImageAdapter = new AlbumImageAdapter(SearchActivity.this, photoList);
        gridView.setAdapter(albumImageAdapter);
    }
}