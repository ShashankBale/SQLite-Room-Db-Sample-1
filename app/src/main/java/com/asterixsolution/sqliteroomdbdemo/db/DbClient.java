package com.asterixsolution.sqliteroomdbdemo.db;

import android.content.Context;

import androidx.room.Room;

public class DbClient {
    private static DbClient instance;
    private final String DB_NAME = "AsterixSolnDb";
    private AsterixSolnDatabase asDb;

    private DbClient(Context ctx) {
        asDb = Room.databaseBuilder(ctx, AsterixSolnDatabase.class, DB_NAME).build();
    }

    public static synchronized DbClient getInstance(Context ctx) {
        if (instance == null) {
            instance = new DbClient(ctx);
        }
        return instance;
    }

    public AsterixSolnDatabase getDb() {
        return asDb;
    }
}
