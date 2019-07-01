package com.example.inventory_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewRecipe extends AppCompatActivity {
    private Button recipeConfirm, recipeCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        recipeConfirm = findViewById(R.id.btnRecipeConfirm);
        recipeCancel = findViewById(R.id.btnRecipeCancel);

        recipeConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecipeLibrary();
            }
        });

        recipeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecipeLibrary();
            }
        });
    }

    public void openRecipeLibrary() {
        Intent intent = new Intent(this, RecipeLibrary.class);
        startActivity(intent);
    }

}
