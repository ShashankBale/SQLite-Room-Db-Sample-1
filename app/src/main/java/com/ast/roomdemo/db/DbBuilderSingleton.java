package com.ast.roomdemo.db;

import android.content.Context;

import androidx.room.Room;

public class DbBuilderSingleton {
    private static DbBuilderSingleton instance;
    private final String DB_NAME = "AsterixSolnDb";
    private AsterixSolnDatabase asDb;

    private DbBuilderSingleton(Context ctx) {
        asDb = Room.databaseBuilder(ctx, AsterixSolnDatabase.class, DB_NAME).build();
    }

    public static synchronized DbBuilderSingleton getInstance(Context ctx) {
        if (instance == null) {
            instance = new DbBuilderSingleton(ctx);
        }
        return instance;
    }

    public AsterixSolnDatabase getDb() {
        return asDb;
    }
}
