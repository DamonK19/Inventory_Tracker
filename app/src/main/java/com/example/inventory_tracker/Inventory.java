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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Inventory extends AppCompatActivity {

    private Button addIngredient;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser user;
    List<Ingredient> lstIngredient = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        addIngredient = findViewById(R.id.btnAddIngredient);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recyclerInventory);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(lstIngredient);
        recyclerView.setHasFixedSize(true);



        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            user = (FirebaseUser) extras.get("user");
        }

        initData();

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openIngredient();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(Inventory.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ingredientDialogBox(view, lstIngredient.get(position));
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }

    private void ingredientDialogBox(View view, final Ingredient ingredient) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Inventory.this);

        builder.setTitle("Ingredient");

        Context context = view.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        builder.setView(layout);

        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openIngredient(ingredient);
            }
        });

        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteIngredient(ingredient);
            }
        });

        builder.show();
    }

    private void openIngredient(Ingredient ingredient) {
        Intent intent = new Intent(this, IngredientAdd.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ingredient", ingredient);
        intent.putExtras(bundle);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void deleteIngredient(Ingredient ingredient) {
        db.collection("Ingredient")
                .document(ingredient.getId())
                .delete();

        lstIngredient.clear();
        initData();
    }

    public void initData() {
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
                                ing.setId(document.getId());
                                lstIngredient.add(ing);
                            }

                            recyclerView.setAdapter(adapter);

                        }
                    }
                });
    }



    public void openIngredient() {
        Intent intent = new Intent(this, IngredientAdd.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}
