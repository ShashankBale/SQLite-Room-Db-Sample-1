package com.asterixsolution.sqliteroomdbdemo.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.asterixsolution.sqliteroomdbdemo.db.entity.Student;
import com.asterixsolution.sqliteroomdbdemo.db.dao.StudentDao;

@Database(entities = {Student.class}, version = 1)
public abstract class AsterixSolnDatabase extends RoomDatabase {
    public abstract StudentDao getStudentDao();
}
