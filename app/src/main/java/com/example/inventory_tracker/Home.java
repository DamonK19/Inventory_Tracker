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

    //GUI componenets
    private Button viewInventory,viewAvailableRecipes, viewLibrary, signOut;

    //Firebase variables
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //set buttons
        viewInventory = findViewById(R.id.btnViewInventory);
        viewAvailableRecipes = findViewById(R.id.btnViewAvailableRecipes);
        viewLibrary = findViewById(R.id.btnViewLibrary);
        signOut = findViewById(R.id.btnSignOut);

        //assign user from extras
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            user = (FirebaseUser) extras.get("user");
        }

        //opens recipe library
        viewLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecipeLibrary();
            }
        });

        //opens the available recipes page
        viewAvailableRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAvailableRecipe();
            }
        });

        //opens inventory page
        viewInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInventory();
            }
        });

        //signs out user
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOutUser();
            }
        });


    }

    //opens inventory
    public void openInventory() {
        Intent intent = new Intent(this, Inventory.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    //opens recipe library
    public void openRecipeLibrary() {
        Intent intent = new Intent(this, RecipeLibrary.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    //open available recipes
    public void openAvailableRecipe() {
        Intent intent = new Intent(this, AvailableRecipes.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    //signs out user
    public void signOutUser() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
