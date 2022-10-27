package com.kun.broccoli.model.recipeClass;

import java.util.List;

public class RecipeClassResponseResult {
    private String status;
    private String msg;
    private List<RecipeClass> result;

    public List<RecipeClass> getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "RecipeClassResponseResult{" +
                "result=" + result +
                '}';
    }
}
