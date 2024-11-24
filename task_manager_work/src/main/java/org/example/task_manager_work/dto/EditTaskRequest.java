package org.example.task_manager_work.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Данные для редактирования задачи")
public class EditTaskRequest {
    @Schema(description = "access token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzMyMjg4NDg4LCJleHAiOjE3MzI4OTMyODh9.dS8BYbGLnkWot3_93-80oxAuAEq5jDUTL0BVwzq2LwY")
    @NotBlank(message = "Токен не может быть пустым")
    private String accessToken;

    @Schema(description = "description", example = "Do microservise")
    private String description;

    @Schema(description = "Comments", example = "Change this function")
    private String comment;

    @Schema(description = "Status", example = "InWork")
    private String status;

    @Schema(description = "Executor Id", example = "1")
    private String executorId;

    @Schema(description = "Id задачи", example = "1")
    @NotBlank(message = "id задачи не может быть пустым")
    private String taskId;
}
