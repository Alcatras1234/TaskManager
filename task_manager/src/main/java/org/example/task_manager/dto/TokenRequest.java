package org.example.task_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на обновление access токена")
public class TokenRequest {
    @Schema(description = "refresh token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzMyMjg4NDg4LCJleHAiOjE3MzI4OTMyODh9.dS8BYbGLnkWot3_93-80oxAuAEq5jDUTL0BVwzq2LwY")
    @NotBlank(message = "Токен не может быть пустым")
    private String token;
}
