package com.pda.assetserver.service.dto.req;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ConsumptionServiceRequest {
    private LocalDate date;
    private String generateType;
    private Integer underAmount;
}
