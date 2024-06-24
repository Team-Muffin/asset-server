package com.pda.assetserver.service.dto.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CardResponse {
    private Long id;
    private String corpName;
    private String cardNumber;
    private String name;
    private String image;
}
