package com.kun.broccoli.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.kun.broccoli.model.cookbook.MakeProcess;

import java.util.List;

@Dao
public interface ProcessDao {
    @Insert
    public void insertProcess(MakeProcess process);

    @Update
    public void updateProcess(MakeProcess process);

    @Delete
    public void deleteProcess(MakeProcess process);

    @Query("select * from make_process")
    public List<MakeProcess> getAllProcess();

    @Query("select * from make_process where recipename = :recipename")
    public List<MakeProcess> findProcess(String recipename);
}
