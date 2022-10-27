package com.kun.broccoli.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.kun.broccoli.model.cookbook.Material;

import java.util.List;

@Dao
public interface MaterialDao {
    @Insert
    public void insertMaterial(Material material);

    @Update
    public void updateMaterial(Material material);

    @Delete
    public void deleteMaterial(Material material);

    @Query("select * from material")
    public List<Material> getAllMaterial();

    @Query("select * from material where recipename = :recipename")
    public List<Material> findMaterial(String recipename);

}
