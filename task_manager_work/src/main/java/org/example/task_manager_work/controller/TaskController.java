package org.example.task_manager_work.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.task_manager_work.dto.*;
import org.example.task_manager_work.handlerException.UnauthorizedException;
import org.example.task_manager_work.handlerException.UserNotFoundException;
import org.example.task_manager_work.model.Task;
import org.example.task_manager_work.services.JwtService;
import org.example.task_manager_work.services.TaskService;
import org.example.task_manager_work.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
@Tag(name = "TaskController", description = "Контроллер для работы с задачами")
public class TaskController {
    private final JwtService jwtService;

    private final TaskService taskService;

    private final UserService userService;

    @Autowired
    public TaskController(JwtService jwtService, TaskService taskService, UserService userService) {
        this.jwtService = jwtService;
        this.taskService = taskService;
        this.userService = userService;
    }

    @Operation(summary = "Создать задачу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })

    @PostMapping("/create")
    public ResponseEntity<String> createTask(@Valid @RequestBody TaskRequest taskRequest) throws UserNotFoundException, UnauthorizedException {
        String accessToken = jwtService.checkJWT(taskRequest.getAccessToken());

        String role = jwtService.getData(taskRequest.getAccessToken());

        if (!userService.checkRoleAdmin(role)) {
            return ResponseEntity.badRequest().body("Вы не являетесь админом и не сможете создать создать задачу");
        }


        taskService.safeTask(taskRequest);
        return ResponseEntity.ok("Задача записана успешно\n" +
                "accessToken: " + accessToken);

    }

    @Operation(summary = "Редактировать задачу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PutMapping("/editTask")
    public ResponseEntity<String> editTask(@Valid @RequestBody EditTaskRequest editTaskRequest) throws UnauthorizedException {

        String accessToken = jwtService.checkJWT(editTaskRequest.getAccessToken());

        String role = jwtService.getData(editTaskRequest.getAccessToken());

        if (!userService.checkRoleAdmin(role)) {
            return ResponseEntity.badRequest().body("Вы не являетесь админом и не сможете создать создать задачу");
        }


        taskService.editTask(editTaskRequest);

        return ResponseEntity.ok("Данные задачи: " + editTaskRequest.getTaskId()
                + " успешно изменены\n" +
                "accessToken: " + accessToken);
    }

    @Operation(summary = "Редактировать комментарий к задаче")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PutMapping("/editComment")
    public ResponseEntity<String> editComment(@Valid @RequestBody EditComment editComment) throws UnauthorizedException {
        String accessToken = jwtService.checkJWT(editComment.getAccessToken());

        String role = jwtService.getData(editComment.getAccessToken());

        if (!userService.checkRoleAdmin(role)) {
            return ResponseEntity.badRequest().body("Вы не являетесь админом и не сможете создать создать задачу");
        }

        taskService.editComment(editComment);

        return ResponseEntity.ok("Данные задачи: " + editComment.getTaskId()
                + " успешно изменены\n" +
                "accessToken: " + accessToken);
    }

    @Operation(summary = "Редактировать исполнителя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PutMapping("/editExecutor")
    public ResponseEntity<String> editExecutor(@Valid @RequestBody EditExecutor editExecutor) throws UnauthorizedException {
        String accessToken = jwtService.checkJWT(editExecutor.getAccessToken());

        String role = jwtService.getData(editExecutor.getAccessToken());

        if (!userService.checkRoleAdmin(role)) {
            return ResponseEntity.badRequest().body("Вы не являетесь админом и не сможете создать создать задачу");
        }

        taskService.editExecutor(editExecutor);

        return ResponseEntity.ok("Данные задачи: " + editExecutor.getTaskId()
                + " успешно изменены на "
                + " исполнитель " + editExecutor.getExecutorId() +
                "\naccessToken: " + accessToken);
    }

    @Operation(summary = "Редактирование комментария пользователем если он исполнитель")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PutMapping("/editCommentForUser")
    public ResponseEntity<String> editCommentForUser(@Valid @RequestBody EditCommentForUser editCommentForUser) throws UnauthorizedException {
        String accessToken = jwtService.checkJWT(editCommentForUser.getAccessToken());

        String role = jwtService.getData(editCommentForUser.getAccessToken());
        taskService.editCommentForUser(editCommentForUser);

        return ResponseEntity.ok("Данные задачи: " + editCommentForUser.getTaskId()
                + " успешно изменены\n" +
                "accessToken: " + accessToken);
    }

    @Operation(summary = "Редактирование статуса задачи если пользователь исполнитель")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PutMapping("/editStatusForUser")
    public ResponseEntity<String> editStatusForUser(@Valid @RequestBody EditStatus editStatus) throws UnauthorizedException {
        String accessToken = jwtService.checkJWT(editStatus.getAccessToken());

        taskService.editStatusForUser(editStatus);

        return ResponseEntity.ok("Данные задачи: " + editStatus.getTaskId()
                + " успешно изменены\n" +
                "accessToken: " + accessToken);
    }

    @Operation(summary = "Вывод задачи по фильтру")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @GetMapping("/filter")
    public ResponseEntity<Page<Task>> getTasks(
            @RequestParam int executorId,
            @RequestParam(required = false) String access_token,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status
    ) throws UnauthorizedException {
        String accessToken = jwtService.checkJWT(access_token);

        if (page < 1 || size <= 0 || executorId <= 0) {
            throw new IllegalArgumentException("Invalid pagination or executorId parameters");
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("taskId").ascending());
        Page<Task> tasks = (status != null && !status.isEmpty())
                ? taskService.getTasksByExecutorIdAndStatus(executorId, status, pageable)
                : taskService.getTasksByExecutorId(executorId, pageable);

        return ResponseEntity.ok(tasks);
    }


}
