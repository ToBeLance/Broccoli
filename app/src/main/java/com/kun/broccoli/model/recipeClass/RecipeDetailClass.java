package com.kun.broccoli.model.recipeClass;

public class RecipeDetailClass {
    private String classid;
    private String name;
    private String parentid;

    public String getClassid() {
        return classid;
    }

    public String getName() {
        return name;
    }

    public String getParentid() {
        return parentid;
    }

    @Override
    public String toString() {
        return "RecipeDetailClass{" +
                "classid='" + classid + '\'' +
                ", name='" + name + '\'' +
                ", parentid='" + parentid + '\'' +
                '}';
    }
}
