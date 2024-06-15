package com.pda.assetserver.utils.api;

public class ApiUtils {
    public static <T> GlobalResponse<T> success(String message) {
        return GlobalResponse.<T>builder()
            .success(true)
            .message(message)
            .build();
    }

    public static <T> GlobalResponse<T> success(String message, T data) {
        return GlobalResponse.<T>builder()
            .success(true)
            .message(message)
            .data(data)
            .build();
    }

    public static <T> GlobalResponse<T> exception(String message) {
        return GlobalResponse.<T>builder()
            .success(true)
            .message(message)
            .build();
    }
}
