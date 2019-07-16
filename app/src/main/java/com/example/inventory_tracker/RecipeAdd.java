package com.example.inventory_tracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.Spinner;


import java.util.ArrayList;
import java.util.List;

public class RecipeAdd extends AppCompatActivity {

    Button addRecipeIngredient;
    List<Ingredient> lstRecipeIngredients = new ArrayList<>();
   ListView recipeIngredients;
    String[] arraySpinner = new String[]{
            "Ct", "g", "lb", "kg", "oz"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_add);
        recipeIngredients = findViewById(R.id.listViewRecipeIngredients);
        addRecipeIngredient = findViewById(R.id.btnAddRecipeIngredient);

        addRecipeIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientDialogBox(view);
            }
        });

    }

    private void updateList() {
        ArrayAdapter<Ingredient> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lstRecipeIngredients);
        recipeIngredients.setAdapter(adapter);

    }

    public void ingredientDialogBox(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(RecipeAdd.this);
        builder.setTitle("Input Ingredient Info");

        final EditText name = new EditText(RecipeAdd.this);
        name.setHint("Name");
        final EditText amount = new EditText(RecipeAdd.this);
        amount.setHint("Amount");
        final Spinner units = new Spinner(RecipeAdd.this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RecipeAdd.this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        units.setAdapter(adapter);
        name.setInputType(InputType.TYPE_CLASS_TEXT);
        amount.setInputType(InputType.TYPE_CLASS_NUMBER);

        Context context = view.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(name);
        layout.addView(amount);
        layout.addView(units);

        builder.setView(layout);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Ingredient ing = new Ingredient(name.getText().toString(), units.getSelectedItem().toString(), Integer.parseInt(amount.getText().toString()), "0");
                lstRecipeIngredients.add(ing);
                updateList();
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

}
