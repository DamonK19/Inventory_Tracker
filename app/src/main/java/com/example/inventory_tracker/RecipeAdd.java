package com.example.inventory_tracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.Spinner;

import java.util.List;

public class RecipeAdd extends AppCompatActivity {

    Button addRecipeIngredient;
    List<Ingredient> lstRecipeIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_add);

        addRecipeIngredient = findViewById(R.id.btnAddRecipeIngredient);

        addRecipeIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecipeAdd.this);
                builder.setTitle("Title");

                final EditText name = new EditText(RecipeAdd.this);
                final EditText amount = new EditText(RecipeAdd.this);
                final Spinner units = new Spinner(RecipeAdd.this);
                name.setInputType(InputType.TYPE_CLASS_TEXT);
                amount.setInputType(InputType.TYPE_CLASS_NUMBER);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Ingredient ing = new Ingredient(name.getText().toString(), units.getSelectedItem().toString(), Integer.parseInt(amount.getText().toString()), "0");
                        lstRecipeIngredients.add(ing);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.show();

            }
        });

    }
}
