package com.pda.assetserver.controller.dto.req;

import com.pda.assetserver.utils.enums.ConsumptionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Schema(description = "소비 내역 만들기 Req")
public class MakeConsumptionRequest {
    private LocalDateTime dateTime;
    @NotBlank
    private String accountNumber;
    @Min(10)
    private Long amount;
    @NotBlank
    private String purchasePlace;
    private ConsumptionType consumptionType;
}
