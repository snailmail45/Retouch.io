package com.evan.scanenhancer.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.evan.scanenhancer.data.model.Result;

@Database(entities = {Result.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ResultDao resultDao();

}
