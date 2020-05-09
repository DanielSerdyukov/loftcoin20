package com.lofschool.loftcoin.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {
    RoomCoin.class
}, version = 1)
abstract class LoftDatabase extends RoomDatabase {
    abstract CoinsDao coins();
}
