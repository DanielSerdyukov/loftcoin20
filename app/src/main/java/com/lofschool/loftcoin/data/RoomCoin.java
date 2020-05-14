package com.lofschool.loftcoin.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.auto.value.AutoValue;

@Entity
@AutoValue
abstract class RoomCoin implements Coin {

    static RoomCoin create(String name,
                           String symbol,
                           int rank,
                           double price,
                           double change24h,
                           String currencyCode,
                           int id) {
        return new AutoValue_RoomCoin(name, symbol, rank, price, change24h, currencyCode, id);
    }

    @Override
    @PrimaryKey
    @AutoValue.CopyAnnotations
    public abstract int id();

}
