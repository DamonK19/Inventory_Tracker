package com.example.inventory_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Ingredient extends AppCompatActivity {
    private Button confirm, remove, cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        String[] arraySpinner = new String[] {
                "Ct", "g", "lb", "kg", "oz"
        };
        Spinner s = (Spinner) findViewById(R.id.spinnerUnits);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        confirm = findViewById(R.id.btnIngredientConfirm);
        remove = findViewById(R.id.btnIngredientRemove);
        cancel = findViewById(R.id.btnIngredientCancel);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
}
