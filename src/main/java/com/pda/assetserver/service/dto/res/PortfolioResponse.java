package com.pda.assetserver.service.dto.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PortfolioResponse {
    private String code;
    private String stockType;
    private String name;
    private String engName;
    private String dartCode;
    private int price;
    private Double dollar;
    private int quantity;
}
