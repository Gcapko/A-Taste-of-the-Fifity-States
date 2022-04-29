package com.hfad.atasteofthefiftystates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class actualRecipeList extends AppCompatActivity implements Serializable {

    ArrayList<String> chosenFood = new ArrayList<String>();
    ArrayList<String> chosenIngredient = new ArrayList<String>();

    public Integer INDEX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_recipe_list);

        chosenFood = (ArrayList<String>) getIntent().getSerializableExtra("food");
        chosenIngredient = (ArrayList<String>) getIntent().getSerializableExtra("ingredients");
        INDEX = (Integer) getIntent().getSerializableExtra("number");



      //  ListView listView = findViewById(R.id.textview);
        TextView food = findViewById(R.id.textView3);
        TextView ingredients = findViewById(R.id.textView4);
        food.setText(chosenFood.get(0));
        ingredients.setText(chosenIngredient.get(0));
    //    listView.setClickable(true);
        //listView.setAdapter(ingredientsArray);
      //  listView.setSelection(INDEX);
    }
}