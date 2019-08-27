package com.example.inventory_tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

public class ViewRecipe extends AppCompatActivity {

    //GUI componenets
    private TextView name, instructions;

    //global recipe object
    private Recipe recipe;

    //recycler view variables
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        //assign recipe
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            recipe = (Recipe)extras.get("recipe");
        }
        
        //assign GUI componenets
        name = findViewById(R.id.txtViewRecipeName);
        instructions = findViewById(R.id.txtViewInstructions);

        //set recycler view
        recyclerView = findViewById(R.id.recyclerViewIngredients);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();

        //set adapter
        adapter = new RecyclerAdapter(recipe.getLstIngredient());

        setFields(recipe);

    }

    //function to set fields of the recipe
    private void setFields(Recipe recipe) {
        name.setText(recipe.getName());
        instructions.setText(recipe.getInstructions());
        recyclerView.setAdapter(adapter);
    }

}
