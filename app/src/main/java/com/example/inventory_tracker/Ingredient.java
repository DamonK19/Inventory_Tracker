package com.example.inventory_tracker;

public class Ingredient {
    String name;
    String unit;
    Integer amount;
    String uid;

    Ingredient(String name, String unit, Integer amount, String uid) {
        this.name = name;
        this.unit = unit;
        this.amount = amount;
        this.uid = uid;
    }

    public String toString() {
        return name + " " + amount.toString() + " " + unit;
    }

}
