package com.pda.assetserver.utils.cash;

import com.pda.assetserver.utils.enums.CASH_DIGIT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

class CashUtilsTest {
    @DisplayName("현금 생성기 테스트")
    @Test
    public void generateCashTest() {

        System.out.println(CashUtils.generateCash(CASH_DIGIT.HUNDRED_MILLION.getValue()));
    }

    @DisplayName("난수 생성 테스트")
    @Test
    public void randomDouble() {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            System.out.println(random.nextDouble(-80,0));
        }
    }
}