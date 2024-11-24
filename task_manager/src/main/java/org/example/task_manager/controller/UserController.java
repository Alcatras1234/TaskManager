package org.example.task_manager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.task_manager.dto.SingInRequest;
import org.example.task_manager.dto.SingUpRequest;
import org.example.task_manager.service.SessionService;
import org.example.task_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/auth")
@Tag(name = "UserController", description = "Контроллер для работы с пользователями")
public class UserController {
    private final UserService userService;
    private final SessionService sessionService;


    @Autowired
    public UserController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @Operation(summary = "Регистрация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PostMapping("/reg")
    public ResponseEntity<String> registerUser(@RequestBody @Valid SingUpRequest singUpRequest) {
        try {
            if (!userService.existsUsersByEmail(singUpRequest.getEmail())) {
                userService.saveUser(singUpRequest);
                return ResponseEntity.ok("Регистрация прошла успешно");
            }
            return ResponseEntity.badRequest().body("Пользователь с таким email уже существует");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }

    @Operation(summary = "Авторизация пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PostMapping("/login")
    public ResponseEntity<String> authorizationUser(@RequestBody @Valid SingInRequest singInRequest) {
        try {
            if (userService.existsUsersByEmail(singInRequest.getEmail())) {
                if (!userService.checkUserPassword(singInRequest)) {
                    return ResponseEntity.badRequest().body("Пароль не верный");
                }
                Map<String, String> jwt = sessionService.getJWT(singInRequest);
                return ResponseEntity.ok(jwt.toString());
            }
            return ResponseEntity.badRequest().body("Пользователь с таким email не найден");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body(ex.getMessage());
        }
    }


}
