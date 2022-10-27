package com.evan.scanenhancer.data;

import com.evan.scanenhancer.data.model.Result;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class LocalRepository {

    private final AppDatabase appDatabase;

    @Inject
    public LocalRepository(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    public Single<Long> insertResult(Result result) {
        return appDatabase.resultDao().insertUpscaleResult(result);
    }

    public Single<List<Result>> getRecentActions() {
        return appDatabase.resultDao().getRecentActions();
    }
}
