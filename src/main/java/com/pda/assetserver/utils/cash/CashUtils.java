package com.pda.assetserver.utils.cash;

import java.util.Random;

public class CashUtils {
    private static final Random random = new Random();

    // 10원 단위
    public static int generateCash(int digit) {
        assert(digit > 0);

        if (digit < 2)
            return 0;

        return (random.nextInt(9)+1) * (int) Math.pow(10, digit) + generateCash(digit-1);
    }

    public static int dollarToWon(double dollar) {
        int wonPerDollar = 1300;

        return (int) dollar * wonPerDollar;
    }
}
