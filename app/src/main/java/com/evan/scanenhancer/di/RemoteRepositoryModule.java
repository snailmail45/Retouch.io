package com.evan.scanenhancer.di;

import com.evan.scanenhancer.data.MainService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
public class RemoteRepositoryModule {

    @Provides
    @Singleton
    public MainService provideMainService(Retrofit retrofit){
        return retrofit.create(MainService.class);
    }
}
