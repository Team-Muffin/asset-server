package com.pda.assetserver.controller.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "이체 Request")
public class TransferRequest {
    @NotBlank
    @Schema(description = "출금 계좌 번호(본인계좌)", example = "145-123123-123123")
    private String fromAccountNumber;
    @NotBlank
    @Schema(description = "목적지 계좌 번호", example = "777-123123-123123")
    private String toAccountNumber;
    @Min(value = 10, message = "송금액은 10원 이상이어야 합니다.")
    @Max(value = 100000000, message = "송금액은 100,000,000 이하이어야 합니다.")
    @Schema(description = "송금액", example = "1000")
    private Long amount;
}
