package com.kun.broccoli.model.recipeClass;

import java.util.List;

public class RecipeClass {
    private String classid;
    private String name;
    private String parentid;
    private List<RecipeDetailClass> list;

    public String getClassid() {
        return classid;
    }

    public String getName() {
        return name;
    }

    public String getParentid() {
        return parentid;
    }

    public List<RecipeDetailClass> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "RecipeClass{" +
                "classid='" + classid + '\'' +
                ", name='" + name + '\'' +
                ", parentid='" + parentid + '\'' +
                ", list=" + list +
                '}';
    }
}
