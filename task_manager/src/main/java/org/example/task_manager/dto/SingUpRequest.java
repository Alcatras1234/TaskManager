package org.example.task_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию")
public class SingUpRequest {

    @Schema(description = "email", example = "davjf@gmail.com")
    @NotBlank(message = "Адрес электронной почты не может быть пустым")
    @Email(message = "Email должен быть в формате user@example.com")
    private String email;

    @Schema(description = "password", example = "VeryDiff!cultPassw@rd")
    @Size(min = 8, max = 255, message = "Пароль должен содержать от 8 до 255 символов")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

    @Schema(description = "Роль", example = "USER")
    @ValidRoleEnum(message = "Роль не входит в доступные")
    private String role;
}
