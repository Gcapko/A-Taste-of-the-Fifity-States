package com.hfad.atasteofthefiftystates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipeListActivity extends AppCompatActivity implements Serializable {
    public static int INDEX = 0;

    ArrayList<String> statesArrayList = new ArrayList<String>();
   ArrayList<String> ingredientsArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list2);
        ingredientsArrayList = (ArrayList<String>) getIntent().getSerializableExtra("ingredients");
        statesArrayList = (ArrayList<String>) getIntent().getSerializableExtra("states");

        ArrayAdapter statesArray = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                statesArrayList);

        ListView listView = findViewById(R.id.textview);
        listView.setClickable(true);
        listView.setAdapter(statesArray);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
/*            String item = adapterView.getItemAtPosition(position).toString();
                Toast.makeText(RecipeListActivity.this, item, Toast.LENGTH_SHORT).show();*/
                Integer stateIndex = position;
                Intent intent = new Intent(view.getContext(), actualRecipeList.class);
                intent.putExtra("ingredients", ingredientsArrayList);
                intent.putExtra("number", position);
                view.getContext().startActivity(intent);


            }
        });

        {

/*
            @Override
            public void onClick(View view) {
                    Integer stateIndex = listView.getSelectedItemPosition();
*/
/*                    Toast.makeText(RecipeListActivity.this, stateIndex, Toast.LENGTH_SHORT).show();
                    Integer  newIndex = stateIndex + INDEX;
                    Intent intent = new Intent(view.getContext(), actualRecipeList.class);
                    intent.putExtra("states", ingredientsArrayList);
                    intent.putExtra("ingredientsIndex", newIndex);
                    view.getContext().startActivity(intent);*//*


            }
        });
*/

        }
    }
}