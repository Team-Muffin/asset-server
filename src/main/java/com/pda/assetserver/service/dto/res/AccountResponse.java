package com.pda.assetserver.service.dto.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private String accountType;
    private String name;
    private String corpName;
    private String logo;
    private Long cash;
    private Double returnRate;
}
