package com.hfad.atasteofthefiftystates;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipeListActivity extends AppCompatActivity implements Serializable {

ArrayList<String> statesArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list2);
        statesArrayList = (ArrayList<String>) getIntent().getSerializableExtra("states");

        ArrayAdapter statesArray = new ArrayAdapter<>(this, R.layout.activity_recipe_list2,
                statesArrayList);

        ListView listView = findViewById(R.id.statesList);
        listView.setAdapter(statesArray);
    }
}