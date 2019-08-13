package com.example.inventory_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeAdd extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser user;
    Button addRecipeIngredient, confirm;
    List<Ingredient> lstRecipeIngredients = new ArrayList<>();
   ListView recipeIngredients;
   EditText recipeName, recipeInstructions;
    String[] arraySpinner = new String[]{
            "Ct", "g", "lb", "kg", "oz"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_add);
        recipeIngredients = findViewById(R.id.listViewRecipeIngredients);
        recipeName = findViewById(R.id.txtViewRecipeName);
        recipeInstructions = findViewById(R.id.txtInstructions);
        addRecipeIngredient = findViewById(R.id.btnAddRecipeIngredient);
        confirm = findViewById(R.id.btnRecipeConfirm);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (FirebaseUser) extras.get("user");
        }


        addRecipeIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientDialogBox(view);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe recipe = new Recipe(recipeName.getText().toString(), recipeInstructions.getText().toString(), lstRecipeIngredients);
                addRecipeText(recipe);
                openHome();
            }
        });

    }

    private void openHome() {
        Intent intent = new Intent(this, Home.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void addRecipeIngredientList(Recipe recipe) {
        for(Ingredient ing : recipe.getLstIngredient()) {
            final Map<String, Object> ingredientMap = new HashMap<>();
            ingredientMap.put("amount", ing.getAmount());
            ingredientMap.put("name", ing.getName());
            ingredientMap.put("unit", ing.getUnit());
            db.collection("Recipe Info")
                    .document(recipe.getRid())
                    .collection("Recipe Ingredients")
                    .add(ingredientMap);
        }
    }

    private void addRecipeText(final Recipe recipe) {
        final Map<String, Object> recipeMap = new HashMap();
        recipeMap.put("name", recipe.getName());
        recipeMap.put("instructions", recipe.getInstructions());
        recipeMap.put("uid", user.getUid());

        db.collection("Recipe Info")
                .add(recipeMap)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        recipe.setRid(task.getResult().getId());
                        addRecipeIngredientList(recipe);
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
