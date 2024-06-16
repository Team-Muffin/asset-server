package com.pda.assetserver.utils.exceptions;

import com.pda.assetserver.utils.api.ApiUtils;
import com.pda.assetserver.utils.api.GlobalResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GlobalResponse<Void> handleBadRequestException(final BadRequestException e) {
        return ApiUtils.exception(e.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public GlobalResponse<Void> handleConflictException(final ConflictException e) {
        return ApiUtils.exception(e.getMessage());
    }

    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public GlobalResponse<Void> handleInternalServerException(final InternalServerException e) {
        return ApiUtils.exception(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Hidden
    public GlobalResponse<Void> handleConflictException(final MethodArgumentNotValidException e) {
        return ApiUtils.exception(e.getBindingResult().getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .toList()
            .toString());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public GlobalResponse<Void> handleUnauthorizedException(final UnAuthorizedException e) {
        return ApiUtils.exception(e.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public GlobalResponse<Void> handleForbiddenException(final ForbiddenException e) {
        return ApiUtils.exception(e.getMessage());
    }
}
