package com.example.inventory_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AvailableRecipes extends AppCompatActivity {

    //Arraylists of user recipe library, user inventory and available recipes
    private List<Recipe> lstLibrary = new ArrayList<>();
    private List<Ingredient> lstInventory = new ArrayList<>();
    private List<Recipe> lstAvailable = new ArrayList<>();

    //FirebaseVariable
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;

    //Recyclerview variables
    private RecyclerView recyclerView;
    private RecipeRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_recipes);

        //set the recycler to the available lists
        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerAvailableRecipes);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecipeRecyclerAdapter(lstAvailable);

        //assign user
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (FirebaseUser) extras.get("user");
        }

        //initialize list of available ingredients
        initLibrary();

    }

    //function to find recipes that are available
    private void findAvailable() {
        for(Recipe rec : lstLibrary){
            int count = 0;

            //check all recipes ingredients against inventory
            for(Ingredient ing : rec.getLstIngredient()){
                for(Ingredient ingInv : lstInventory) {
                    if(ing.getName().equals(ingInv.getName()) && ing.getAmount() <= ingInv.getAmount()) {
                        count++;
                    }
                }
                if(count == rec.getLstIngredient().size()) {
                    lstAvailable.add(rec);
                }
            }
        }

    }

    //function to assign all user recipes to library list
    private void initLibrary() {
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
                                                        lstLibrary.add(rec);

                                                    }
                                                }
                                                initInventory();
                                            }
                                        });


                            }

                        }
                    }
                });
    }

    //function to initialize user inventory
    private void initInventory() {
        db.collection("Ingredient")
                .whereEqualTo("uid", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String name = document.get("name").toString();
                                String uid = document.get("uid").toString();
                                Integer amount = Integer.parseInt(document.get("amount").toString());
                                String unit = document.get("unit").toString();
                                Ingredient ing = new Ingredient(name, unit, amount, uid);
                                lstInventory.add(ing);
                            }
                            findAvailable();
                            recyclerView.setAdapter(adapter);

                        }

                    }
                });
    }

}
