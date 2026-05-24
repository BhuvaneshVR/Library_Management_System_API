package com.company.library.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDTO<T> {

    private LocalDateTime timestamp;
    private Integer status;
    private String message;
    private T data;
    private String path;

    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return ApiResponseDTO.<T>builder()
            .timestamp(LocalDateTime.now())
            .status(200)
            .message(message)
            .data(data)
            .build();
    }

    public static <T> ApiResponseDTO<T> success(String message) {
        return ApiResponseDTO.<T>builder()
            .timestamp(LocalDateTime.now())
            .status(200)
            .message(message)
            .build();
    }

    public static <T> ApiResponseDTO<T> error(Integer status, String message, String path) {
        return ApiResponseDTO.<T>builder()
            .timestamp(LocalDateTime.now())
            .status(status)
            .message(message)
            .path(path)
            .build();
    }
}
