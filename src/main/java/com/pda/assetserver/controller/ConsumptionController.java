package com.pda.assetserver.controller;

import com.pda.assetserver.controller.annotations.SwaggerAuth;
import com.pda.assetserver.controller.annotations.UserInfo;
import com.pda.assetserver.controller.dto.req.MakeConsumptionRequest;
import com.pda.assetserver.controller.resolver.UserRequest;
import com.pda.assetserver.service.ConsumptionService;
import com.pda.assetserver.service.dto.req.ConsumptionServiceRequest;
import com.pda.assetserver.service.dto.res.ConsumptionResponse;
import com.pda.assetserver.utils.api.ApiUtils;
import com.pda.assetserver.utils.api.GlobalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Consumption", description = "소비 관련 API 입니다")
@RestController
@RequestMapping("/consumptions")
@RequiredArgsConstructor
public class ConsumptionController {
    private final ConsumptionService consumptionService;

    @GetMapping
    @Operation(summary = "소비 내역 가져오기(생성)", description = "고객 소비 내역 가져오기 (만약 없을 시에는 create)")
    @SwaggerAuth
    @ApiResponse(responseCode = "200", description = "성공")
    public GlobalResponse<List<ConsumptionResponse>> getConsumption(@UserInfo UserRequest user,
                                                                    @Parameter(example = "2024-06-21") @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate date,
                                                                    @Parameter(example = "NORMAL", description = "NORMAL / UNDER / FLEX 가능") @RequestParam(value = "generate-option", required = false, defaultValue = "NORMAL") String generateOption,
                                                                    @Parameter(example = "5000") @RequestParam(value = "under-amount", required = false) Integer underAmount) {
        return ApiUtils.success("소비 가져오기 성공", consumptionService.getConsumption(user, ConsumptionServiceRequest.builder()
                .date(date)
                .generateType(generateOption)
                .underAmount(underAmount)
            .build()));
    }

    @PostMapping
    @Operation(summary = "소비 POST", description = "고객 소비 내역 만들기")
    @SwaggerAuth
    @ApiResponse(responseCode = "201", description = "성공")
    @ResponseStatus(HttpStatus.CREATED)
    public GlobalResponse<List<Void>> makeConsumption(@UserInfo UserRequest user, @Valid @RequestBody MakeConsumptionRequest request) {
        consumptionService.consumption(user, request);
        return ApiUtils.success("소비 만들기 성공");
    }
}
