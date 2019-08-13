package com.example.inventory_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RecipeLibrary extends AppCompatActivity {
    private Button addRecipe, back;
    private List<Recipe> lstRecipe = new ArrayList<>();
    private FirebaseUser user;
    private RecyclerView recyclerView;
    private RecipeRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_library);
        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerLibrary);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();
        adapter = new RecipeRecyclerAdapter(lstRecipe);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (FirebaseUser) extras.get("user");
        }
        addRecipe = findViewById(R.id.btnAddRecipe);
        back = findViewById(R.id.btnBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
            }
        });

        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddRecipe();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(RecipeLibrary.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openViewRecipe(lstRecipe.get(position));
            }

            @Override
            public void onLongItemClick(View view, int position) {
                openAddRecipe();
            }
        }));

        initRecipes();
    }

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
                                String rid = document.getId();
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

    public void openHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void openAddRecipe() {
        Intent intent = new Intent(this, RecipeAdd.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void openViewRecipe(Recipe recipe) {
        Intent intent = new Intent(this, ViewRecipe.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("recipe", recipe);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
