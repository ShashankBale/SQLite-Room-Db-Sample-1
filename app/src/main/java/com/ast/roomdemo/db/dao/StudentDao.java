package com.ast.roomdemo.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.ast.roomdemo.db.entity.Student;

import java.util.List;

@Dao
public interface StudentDao {

    @Insert
    void insert(Student student);

    @Query("SELECT * FROM Student")
    List<Student> getAll();

    @Delete
    void delete(Student student);

    @Update
    void update(Student student);

}
