package com.kun.broccoli.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kun.broccoli.model.cookbook.CookBook;

import java.util.List;

@Dao
public interface CookBookDao {
    @Insert
    public void insertCookBook(CookBook cookBook);

    @Update
    public void updateCookBook(CookBook cookBook);

    @Delete
    public void deleteCookBook(CookBook cookBook);

    @Query("select * from cookBook order by id asc")
    public List<CookBook> getAllCookBook();

    @Query("select * from cookBook where id = :id")
    public CookBook findCookBook(String id);
}
