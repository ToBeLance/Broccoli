package com.kun.broccoli.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kun.broccoli.model.cookbook.CookBook;
import com.kun.broccoli.model.cookbook.Material;
import com.kun.broccoli.model.cookbook.MakeProcess;
import com.kun.broccoli.model.cookbook.Recipe;


@Database(entities = {CookBook.class, Material.class, MakeProcess.class,Recipe.class},version = 2,exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "my_db";
    private static MyDatabase databaseInstance;
    public static synchronized MyDatabase getDatabaseInstance(Context context) {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(
                    context.getApplicationContext(),MyDatabase.class,DATABASE_NAME).build();
        }
        return databaseInstance;
    }
    public abstract CookBookDao cookBookDao();
    public abstract MaterialDao materialDao();
    public abstract ProcessDao processDao();
    public abstract RecipeDao recipeDao();
}
