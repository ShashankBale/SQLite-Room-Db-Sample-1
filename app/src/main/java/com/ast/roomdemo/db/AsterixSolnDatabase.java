package com.ast.roomdemo.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.ast.roomdemo.db.entity.Student;
import com.ast.roomdemo.db.dao.StudentDao;

@Database(entities = {Student.class}, version = 1)
public abstract class AsterixSolnDatabase extends RoomDatabase {
    public abstract StudentDao getStudentDao();
}
