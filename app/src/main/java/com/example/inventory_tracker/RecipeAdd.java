package com.example.inventory_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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

    //firebase variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;

    //arrays
    private ArrayList<String> arraySpinner = new ArrayList<String>();
    private List<Ingredient> lstRecipeIngredients = new ArrayList<>();

    //gui components
    private Button addRecipeIngredient, confirm;
    private EditText recipeName, recipeInstructions;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    //objects and adapters
    private Recipe recipe;
    private RecyclerAdapter adapterRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_add);

        //add units to array for spinner
        arraySpinner.add("Ct");
        arraySpinner.add("g");
        arraySpinner.add("lb");
        arraySpinner.add("kg");
        arraySpinner.add("oz");

        //set GUI components
        recyclerView = findViewById(R.id.recyclerRecipeIngredients);
        recipeName = findViewById(R.id.txtViewRecipeName);
        recipeInstructions = findViewById(R.id.txtInstructions);
        addRecipeIngredient = findViewById(R.id.btnAddRecipeIngredient);
        confirm = findViewById(R.id.btnRecipeConfirm);

        //set layout and set to recycler
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();

        //check for contents of bundle and act accordingly
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (FirebaseUser) extras.get("user");
            if (extras.getSerializable("recipe") != null) {
                recipe = (Recipe) extras.getSerializable("recipe");
                adapterRecycler = new RecyclerAdapter(recipe.getLstIngredient());
                setFields();

            }
        }

        //brings up dialog box for adding recipe ingredient
        addRecipeIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientDialogBox(view);
            }
        });

        //adds recipe to database
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe recipe = new Recipe(recipeName.getText().toString(), recipeInstructions.getText().toString(), lstRecipeIngredients);
                addRecipeText(recipe);
                openHome();
            }
        });


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(RecipeAdd.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            //brings up the edit dialog box
            @Override
            public void onItemClick(View view, int position) {
                ingredientDialogBox(view, recipe.getLstIngredient().get(position));
            }

            //brings up the delete dialog box
            @Override
            public void onLongItemClick(View view, int position) {
                deleteDialogBox(view, recipe.getLstIngredient().get(position));
            }
        }));

    }

    //dialog box for deleting the ingredient from the list
    private void deleteDialogBox(View view, final Ingredient ingredient) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RecipeAdd.this);

        builder.setTitle("Recipe Delete");

        Context context = view.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        builder.setView(layout);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteIngredient(ingredient);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }

    //deletes ingredient from list and updates the recycler
    private void deleteIngredient(Ingredient ingredient) {

        int index = recipe.getLstIngredient().indexOf(ingredient);
        recipe.getLstIngredient().remove(index);
        recyclerView.setAdapter(adapterRecycler);
    }

    //dialog box for updating an ingredient
    private void ingredientDialogBox(View view, Ingredient ingredient) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RecipeAdd.this);
        builder.setTitle("Input Ingredient Info");

        //set dialog box componenets
        final EditText name = new EditText(RecipeAdd.this);
        name.setHint("Name");
        final EditText amount = new EditText(RecipeAdd.this);
        amount.setHint("Amount");
        final Spinner units = new Spinner(RecipeAdd.this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RecipeAdd.this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        units.setAdapter(adapter);

        //assign type to GUI components
        name.setInputType(InputType.TYPE_CLASS_TEXT);
        amount.setInputType(InputType.TYPE_CLASS_NUMBER);

        //set context and orientation
        Context context = view.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        //set text and selection
        name.setText(ingredient.getName());
        amount.setText(ingredient.getAmount().toString());
        units.setSelection(arraySpinner.indexOf(ingredient.getUnit()));

        //add to layout
        layout.addView(name);
        layout.addView(amount);
        layout.addView(units);

        //set the view of the builder
        builder.setView(layout);

        //updates list
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Ingredient ing = new Ingredient(name.getText().toString(), units.getSelectedItem().toString(), Integer.parseInt(amount.getText().toString()), "0");
                lstRecipeIngredients.add(ing);
                updateList();
            }
        });

        //cancels dialog
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    //opens to home page
    private void openHome() {
        Intent intent = new Intent(this, Home.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    //add the ingredients of the recipe to the database in their own collection
    private void addRecipeIngredientList(Recipe recipe) {
        for (Ingredient ing : recipe.getLstIngredient()) {
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

    //add text componenets of recipe into database
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

                        recipeMap.put("rid", recipe.getRid());
                        db.collection("Recipe Info")
                                .document(recipe.getRid())
                                .update(recipeMap);
                        addRecipeIngredientList(recipe);
                    }
                });

    }

    //updates recipe ingredients
    private void updateList() {
        RecyclerAdapter adapter = new RecyclerAdapter(lstRecipeIngredients);
        recyclerView.setAdapter(adapter);

    }

    //dialog box for entering in new ingredient to the list, same as above
    public void ingredientDialogBox(View view) {
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

    //set field of recipe
    private void setFields() {
        recipeName.setText(recipe.getName());
        recipeInstructions.setText(recipe.getInstructions());
        recyclerView.setAdapter(adapterRecycler);
    }

}
