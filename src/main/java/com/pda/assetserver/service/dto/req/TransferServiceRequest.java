package com.pda.assetserver.service.dto.req;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TransferServiceRequest {
    private String frontId;
    private String backId;
    private String contact;
    private String fromAccountNumber;
    private String toAccountNumber;
    private Long amount;
}
