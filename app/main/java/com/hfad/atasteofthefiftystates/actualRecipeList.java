package com.hfad.atasteofthefiftystates;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;

public class actualRecipeList extends AppCompatActivity implements Serializable {

    ArrayList<String> ingredientsArrayList = new ArrayList<String>();
    public Integer INDEX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_recipe_list);

        ingredientsArrayList = (ArrayList<String>) getIntent().getSerializableExtra("ingredients");
        INDEX = (Integer) getIntent().getSerializableExtra("number");

        ArrayAdapter ingredientsArray = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                ingredientsArrayList);

        ListView listView = findViewById(R.id.textview);
        listView.setClickable(true);
        listView.setAdapter(ingredientsArray);
    }
}