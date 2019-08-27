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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RecipeLibrary extends AppCompatActivity {

    //GUI components
    private Button addRecipe, back;

    //Firebase viariables
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;

    //list of user recipes
    private List<Recipe> lstRecipe = new ArrayList<>();

    //recycler view variables
    private RecyclerView recyclerView;
    private RecipeRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_library);

        //set recyclerview
        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerLibrary);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        adapter = new RecipeRecyclerAdapter(lstRecipe);

        //assign user
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (FirebaseUser) extras.get("user");
        }

        //assign buttons
        addRecipe = findViewById(R.id.btnAddRecipe);
        back = findViewById(R.id.btnBack);

        //go back home
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
            }
        });

        //open add recipe page
        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddRecipe();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(RecipeLibrary.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

            //opens view recipe page
            @Override
            public void onItemClick(View view, int position) {
                openViewRecipe(lstRecipe.get(position));
            }

            //opens recipe dialog
            @Override
            public void onLongItemClick(View view, int position) {
                recipeDialogBox(view, lstRecipe.get(position));
            }
        }));

        initRecipes();
    }

    //initializes user recipes
    private void initRecipes() {
        db.collection("Recipe Info")
                .whereEqualTo("uid", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                //recipe parameters
                                final String recName = document.get("name").toString();
                                final String recInstructions = document.get("instructions").toString();
                                final String rid = document.getId();
                                //get ingredients list
                                db.collection("Recipe Info")
                                        .document(rid)
                                        .collection("Recipe Ingredients")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                List<Ingredient> lstIngredients = new ArrayList<Ingredient>();
                                                if (task.isSuccessful()) {
                                                    for(QueryDocumentSnapshot document1 : task.getResult()) {
                                                        String name = document1.get("name").toString();
                                                        Integer amount = Integer.parseInt(document1.get("amount").toString());
                                                        String unit = document1.get("unit").toString();
                                                        Ingredient ing = new Ingredient(name, unit, amount, "0");
                                                        lstIngredients.add(ing);
                                                        Recipe rec = new Recipe(recName,recInstructions,lstIngredients);
                                                        rec.setRid(rid);
                                                        lstRecipe.add(rec);

                                                    }
                                                    recyclerView.setAdapter(adapter);
                                                }
                                            }
                                        });


                            }

                        }
                    }
                });
    }

    //open home page
    public void openHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    //open the page to add a recipe
    public void openAddRecipe() {
        Intent intent = new Intent(this, RecipeAdd.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    //open page to view the recipe
    public void openViewRecipe(Recipe recipe) {
        Intent intent = new Intent(this, ViewRecipe.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("recipe", recipe);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //dialog box where user chooses to edit or delete the recipe
    public void recipeDialogBox(View view, final Recipe recipe) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RecipeLibrary.this);

        builder.setTitle("Recipe");

        Context context = view.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        builder.setView(layout);

        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openAddRecipe(recipe);
            }
        });

        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteRecipe(recipe);
            }
        });

        builder.show();

    }

    //function to delete recipe from library
    private void deleteRecipe(Recipe recipe) {
        db.collection("Recipe Info")
                .document(recipe.getRid())
                .delete();

        lstRecipe.clear();
        initRecipes();

    }

    //open the add recipe page with a selected recipe
    private void openAddRecipe(Recipe recipe) {
        Intent intent = new Intent(this, RecipeAdd.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("recipe", recipe);
        intent.putExtras(bundle);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}
