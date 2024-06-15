package com.pda.assetserver.service.dto.req;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GetAssetInfoServiceRequest {
    private String frontId;
    private String backId;
    private String contact;
    private Set<String> targets;
}
