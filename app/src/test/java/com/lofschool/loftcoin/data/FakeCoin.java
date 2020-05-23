package com.lofschool.loftcoin.data;

public class FakeCoin implements Coin {
    @Override
    public int id() {
        return 0;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public String symbol() {
        return null;
    }

    @Override
    public int rank() {
        return 0;
    }

    @Override
    public double price() {
        return 0;
    }

    @Override
    public double change24h() {
        return 0;
    }

    @Override
    public String currencyCode() {
        return null;
    }
}
