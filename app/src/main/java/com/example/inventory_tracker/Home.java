package com.example.inventory_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private Button viewInventory,viewAvailableRecipes, viewLibrary;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        viewInventory = findViewById(R.id.btnViewInventory);
        viewAvailableRecipes = findViewById(R.id.btnViewAvailableRecipes);
        viewLibrary = findViewById(R.id.btnViewLibrary);


        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            user = (FirebaseUser) extras.get("user");
        }


        viewLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecipeLibrary();
            }
        });

        viewAvailableRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAvailableRecipe();
            }
        });

        viewInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInventory();
            }
        });


    }

    public void openInventory() {
        Intent intent = new Intent(this, Inventory.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
    public void openRecipeLibrary() {
        Intent intent = new Intent(this, RecipeLibrary.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void openAvailableRecipe() {
        Intent intent = new Intent(this, AvailableRecipes.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }



}
