package com.evan.scanenhancer.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.evan.scanenhancer.data.model.Result;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface ResultDao {
    @Insert
    Single<Long> insertUpscaleResult(Result result);

    @Query("SELECT * FROM recent_actions ORDER BY date_created DESC")
    Single<List<Result>> getRecentActions();
}
