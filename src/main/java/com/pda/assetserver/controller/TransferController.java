package com.pda.assetserver.controller;

import com.pda.assetserver.controller.annotations.SwaggerAuth;
import com.pda.assetserver.controller.annotations.UserInfo;
import com.pda.assetserver.controller.dto.req.TransferRequest;
import com.pda.assetserver.controller.resolver.UserRequest;
import com.pda.assetserver.service.TransferService;
import com.pda.assetserver.service.dto.req.TransferServiceRequest;
import com.pda.assetserver.utils.api.ApiUtils;
import com.pda.assetserver.utils.api.GlobalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Transfer", description = "송금 관련 API 입니다")
@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping
    @Operation(summary = "이체", description = "이체 API(출금 계좌는 본인 계좌만 가능 / 이체 != 소비)")
    @SwaggerAuth
    @ApiResponse(responseCode = "201", description = "성공")
    @ResponseStatus(HttpStatus.CREATED)
    public GlobalResponse<Void> transfer(@UserInfo UserRequest user, @Valid @RequestBody TransferRequest transferRequest) {
        transferService.transfer(TransferServiceRequest.builder()
                .frontId(user.getSocialIdFront())
                .backId(user.getSocialIdBack())
                .contact(user.getContact())
                .fromAccountNumber(transferRequest.getFromAccountNumber())
                .toAccountNumber(transferRequest.getToAccountNumber())
                .amount(transferRequest.getAmount())
            .build());
        return ApiUtils.success("이체 성공");
    }
}
