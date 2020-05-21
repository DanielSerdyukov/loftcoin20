package com.lofschool.loftcoin.data;

import androidx.annotation.WorkerThread;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
abstract class CoinsDao {

    @Query("SELECT * FROM RoomCoin")
    abstract Observable<List<RoomCoin>> fetchAll();

    @Query("SELECT * FROM RoomCoin ORDER BY price DESC")
    abstract Observable<List<RoomCoin>> fetchAllSortByPrice();

    @Query("SELECT * FROM RoomCoin ORDER BY rank ASC")
    abstract Observable<List<RoomCoin>> fetchAllSortByRank();

    @Query("SELECT * FROM RoomCoin WHERE id=:id")
    abstract Single<RoomCoin> fetchOne(long id);

    @WorkerThread
    @Query("SELECT COUNT(id) FROM RoomCoin")
    abstract int coinsCount();

    @Query("SELECT * FROM RoomCoin WHERE id NOT IN (:ids) ORDER BY rank ASC LIMIT 1")
    abstract Single<RoomCoin> nextPopularCoin(List<Integer> ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insert(List<RoomCoin> coins);

    @Query("SELECT * FROM RoomCoin ORDER BY rank ASC LIMIT :limit")
    abstract Observable<List<RoomCoin>> fetchTop(int limit);
}
