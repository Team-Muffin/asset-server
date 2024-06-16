package com.pda.assetserver.service.dto.res;

import com.pda.assetserver.utils.enums.ConsumptionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ConsumptionResponse {
    private Long id;
    private LocalDateTime dateTime;
    private String purchasePlace;
    private ConsumptionType consumptionType;
    private Long amount;
    private String accountNumber;
    private String accountName;
    private String corpName;
    private String corpImage;
}
