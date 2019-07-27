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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String toString() {
        return name + " " + amount.toString() + " " + unit;
    }

}
