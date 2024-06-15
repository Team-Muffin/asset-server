package com.pda.assetserver.utils.enums;

public enum CASH_DIGIT {
    HUNDRED(2), // 백원
    THOUSAND(3),
    TEN_THOUSAND(4), // 만원
    HUNDRED_THOUSAND(5),
    MILLION(6), // 100만원
    TEN_MILLION(7),
    HUNDRED_MILLION(8); // 1억

    private int value;

    CASH_DIGIT(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
