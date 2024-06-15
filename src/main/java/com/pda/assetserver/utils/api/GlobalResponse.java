package com.pda.assetserver.utils.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Schema(description = "Response Wrapper")
public class GlobalResponse<T> {
    @Schema(description = "성공 여부", example = "true")
    private boolean success;
    @Schema(description = "메시지", example = "조회 완료")
    private String message;
    private T data;
}
