package com.example.inventory_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

public class IngredientAdd extends AppCompatActivity {

    //GUI componenets
    private Button confirm, remove, cancel;
    EditText txtName, txtAmount;
    String uid;
    Spinner s;

    //Firebase Variables
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user;

    //Hashmap for writing new ingredients to Inventory
    Map<String, Object> ingredient = new HashMap<>();

    //Global Ingredient
    Ingredient ing;

    //Array and adapter used for the spinner
    ArrayAdapter<String> adapter;
    String[] arraySpinner = new String[]{
            "Ct", "g", "lb", "kg", "oz"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);


        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        //db.setFirestoreSettings(settings);

        //setting editTexts
        txtName = findViewById(R.id.txtName);
        txtAmount = findViewById(R.id.txtAmount);

        //setting buttons
        confirm = findViewById(R.id.btnIngredientConfirm);
        remove = findViewById(R.id.btnIngredientRemove);
        cancel = findViewById(R.id.btnIngredientCancel);

        //set spinner and assign values to it
        s = findViewById(R.id.spinnerUnits);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        s.setAdapter(adapter);

        //set user variable and global ingredient, if given, and set fields
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (FirebaseUser) extras.get("user");
            if( extras.getSerializable("ingredient") != null) {
                ing = (Ingredient) extras.getSerializable("ingredient");
                setFields();
            }
        }

        //assign user id variable for ease
        uid = user.getUid();

        //add text fields to map and add to database collection of ingredients
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredient.put("name", txtName.getText().toString());
                ingredient.put("amount", Integer.parseInt(txtAmount.getText().toString()));
                ingredient.put("unit",s.getSelectedItem().toString());
                ingredient.put("uid", user.getUid());
                db.collection("Ingredient")
                        .add(ingredient);
                openHome();
            }


        });

        //buttons to open the home page
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHome();
            }
        });
    }

    //Function for setting the fields for ingredient passed in extras
    private void setFields() {
        txtName.setText(ing.getName());
        txtAmount.setText(ing.getAmount().toString());
        int spinnerPosition = adapter.getPosition(ing.getUnit());
        s.setSelection(spinnerPosition);

    }

    //opens home page
    public void openHome() {
        Intent intent = new Intent(this, Home.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

}
