package com.pda.assetserver.service.dto.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AssetSummaryResponse {
    private String productType;
    private String name;
    private String corpName;
    private String thumbnail;
}
