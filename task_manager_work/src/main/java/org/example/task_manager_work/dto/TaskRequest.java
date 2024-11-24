package org.example.task_manager_work.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на обновление access токена")
public class TaskRequest {
    @Schema(description = "access token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzMyMjg4NDg4LCJleHAiOjE3MzI4OTMyODh9.dS8BYbGLnkWot3_93-80oxAuAEq5jDUTL0BVwzq2LwY")
    @NotBlank(message = "Токен не может быть пустым")
    private String accessToken;

    @Schema(description = "description", example = "Do microservise")
    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @Schema(description = "Comments", example = "Change this function")
    private String comment;

    @Schema(description = "Status", example = "InWork")
    @NotBlank(message = "Статус не может быть пустым")
    private String status;

    @Schema(description = "Executor", example = "1")
    @NotBlank(message = "id исполнителя не может быть пустым")
    private String executorId;
}
