package com.example.inventory_tracker;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {
    String name;
    String instructions;
    String rid;
    List<Ingredient> lstIngredient;

    Recipe(String name, String instructions, List<Ingredient> lstIngredient) {
        this.name = name;
        this.instructions = instructions;
        this.lstIngredient = lstIngredient;
    }

    public List<Ingredient> getLstIngredient() {
        return lstIngredient;
    }

    public void setLstIngredient(List<Ingredient> lstIngredient) {
        this.lstIngredient = lstIngredient;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
