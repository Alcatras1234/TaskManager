package org.example.task_manager_work.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Изменение статуса задачи")
public class EditStatus {
    @Schema(description = "access token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzMyMjg4NDg4LCJleHAiOjE3MzI4OTMyODh9.dS8BYbGLnkWot3_93-80oxAuAEq5jDUTL0BVwzq2LwY")
    @NotBlank(message = "Токен не может быть пустым")
    private String accessToken;

    @Schema(description = "Executor", example = "1")
    @NotBlank(message = "id исполнителя не может быть пустым")
    private String executorId;

    @Schema(description = "Status", example = "Change this function")
    private String status;

    @Schema(description = "Id задачи", example = "1")
    @NotBlank(message = "id задачи не может быть пустым")
    private String taskId;
}
