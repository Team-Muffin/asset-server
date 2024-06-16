package com.pda.assetserver.controller;

import com.pda.assetserver.controller.annotations.SwaggerAuth;
import com.pda.assetserver.controller.annotations.UserInfo;
import com.pda.assetserver.controller.resolver.UserRequest;
import com.pda.assetserver.service.AssetService;
import com.pda.assetserver.service.dto.req.GetAssetInfoServiceRequest;
import com.pda.assetserver.service.dto.res.AssetInfoResponse;
import com.pda.assetserver.service.dto.res.AssetSummaryResponse;
import com.pda.assetserver.utils.api.ApiUtils;
import com.pda.assetserver.utils.api.GlobalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@Tag(name = "Asset", description = "유저 자산 관련 API 입니다")
@RestController
@RequestMapping("/assets")
@RequiredArgsConstructor
public class AssetController {
    private final AssetService assetService;

    @GetMapping
    @Operation(summary = "자산 정보 조회(계좌 연결)", description = "고객 자산 정보 조회 (만약 없을 시에는 create)")
    @SwaggerAuth
    @ApiResponse(responseCode = "200", description = "성공")
    public GlobalResponse<AssetInfoResponse> getUserAsset(@UserInfo UserRequest user,
              @Parameter(description = "ALL / ACCOUNT / CARD / PORTFOLIO / LOAN / FUND를 조합하여 보내세요")
              @RequestParam(value = "targets", defaultValue = "ALL") Set<String> targets) {
        return ApiUtils.success("자산 조회 성공", assetService.getAssetInfo(GetAssetInfoServiceRequest.builder()
                .frontId(user.getSocialIdFront())
                .backId(user.getSocialIdBack())
                .contact(user.getContact())
                .targets(targets)
            .build()));
    }

    @GetMapping("/summary")
    @Operation(summary = "보유 자산 목록 조회", description = "고객 보유 자산 목록 조회 (연결 이후 사용 가능)")
    @SwaggerAuth
    @ApiResponse(responseCode = "200", description = "성공")
    public GlobalResponse<List<AssetSummaryResponse>> getUserAsset(@UserInfo UserRequest user) {
        return ApiUtils.success("자산 조회 성공", assetService.getAssetSummary(user));
    }
}
