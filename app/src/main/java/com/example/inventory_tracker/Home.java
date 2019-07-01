package com.example.inventory_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {
    private Button add, edit, use, viewLibrary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        add = findViewById(R.id.btn_add);
        edit = findViewById(R.id.btnEdit);
        use = findViewById(R.id.btnUse);
        viewLibrary = findViewById(R.id.btnViewLibrary);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openIngredient();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openIngredient();
            }
        });

        viewLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecipeLibrary();
            }
        });
    }

    public void openIngredient() {
        Intent intent = new Intent(this, Ingredient.class);
        startActivity(intent);
    }
    public void openRecipeLibrary() {
        Intent intent = new Intent(this, RecipeLibrary.class);
        startActivity(intent);
    }
}
