package com.pda.assetserver.controller.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "고객 정보")
public class UserInfoDto {
    @Schema(description = "고객 이름", example = "김동원")
    private String name;
    @Schema(description = "고객 주민등록번호 앞자리", example = "011221")
    private String socialIdFront;
    @Schema(description = "고객 주민등록번호 뒷자리", example = "011221")
    private String socialIdBack;
    @Schema(description = "고객 전화번호", example = "01012341234")
    private String contact;
}
