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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

public class Ingredient extends AppCompatActivity {
    private Button confirm, remove, cancel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Map<String, Object> ingredient = new HashMap<>();

    EditText txtName, txtAmount;
    String uid;
    Spinner s;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (FirebaseUser) extras.get("user");
        }
        String[] arraySpinner = new String[]{
                "Ct", "g", "lb", "kg", "oz"
        };
        s = findViewById(R.id.spinnerUnits);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        s.setAdapter(adapter);

        uid = user.getUid();
        txtName = findViewById(R.id.txtName);
        txtAmount = findViewById(R.id.txtAmount);


        confirm = findViewById(R.id.btnIngredientConfirm);
        remove = findViewById(R.id.btnIngredientRemove);
        cancel = findViewById(R.id.btnIngredientCancel);
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

    public void openHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void writeNewIngredient(Map<String, Object> map) {

        db.collection("Ingredient")
                .add(map);
    }
}
