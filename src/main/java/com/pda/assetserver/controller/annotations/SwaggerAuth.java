package com.pda.assetserver.controller.annotations;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Parameters({
    @Parameter(
        name = "front-social-id",
        description = "고객 주민등록번호 앞자리",
        required = true,
        in = ParameterIn.HEADER,
        schema = @Schema(implementation = String.class),
        example = "000103"),
    @Parameter(
        name = "back-social-id",
        description = "고객 주민등록번호 뒷자리",
        required = true,
        in = ParameterIn.HEADER,
        schema = @Schema(implementation = String.class),
        example = "1234123"),
    @Parameter(
        name = "user-social-contact",
        description = "고객 전화번호",
        required = true,
        in = ParameterIn.HEADER,
        schema = @Schema(implementation = String.class),
        example = "01012341234"
    )
})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.METHOD)
public @interface SwaggerAuth {
}
