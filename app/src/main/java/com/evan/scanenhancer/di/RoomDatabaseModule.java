package com.evan.scanenhancer.di;

import android.content.Context;

import androidx.room.Room;

import com.evan.scanenhancer.data.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class RoomDatabaseModule {

    @Provides
    @Singleton
    AppDatabase provideAppDatabase(@ApplicationContext Context context){
        return Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "scan-enhancer-database")
                .fallbackToDestructiveMigration() //Clear database upon version upgrade
                .build();
    }
}
