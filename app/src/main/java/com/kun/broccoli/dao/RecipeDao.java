package com.kun.broccoli.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kun.broccoli.model.cookbook.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert
    public void insertRecipe(Recipe recipe);

    @Update
    public void updateRecipe(Recipe recipe);

    @Delete
    public void deleteRecipe(Recipe recipe);

    @Query("select * from recipe")
    public List<Recipe> getAllRecipe();

    @Query("select * from recipe where name = :name")
    public Recipe findRecipe(String name);

}
