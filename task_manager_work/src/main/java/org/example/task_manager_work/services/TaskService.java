package org.example.task_manager_work.services;

import jakarta.persistence.EntityNotFoundException;
import org.example.task_manager_work.dto.*;
import org.example.task_manager_work.handlerException.UserNotFoundException;
import org.example.task_manager_work.model.Task;
import org.example.task_manager_work.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserService userService;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public void safeTask(TaskRequest taskRequest) throws UserNotFoundException {
        int executorId = Integer.parseInt(taskRequest.getExecutorId());
        if (!userService.existUserByUserId(executorId)) {
            throw new UserNotFoundException("Пользователь с таким ID не найден");
        }
        Task task = new Task();

        task.setExecutorId(executorId);
        task.setDescription(taskRequest.getDescription());
        task.setComment(taskRequest.getComment());
        task.setStatus(taskRequest.getStatus());

        taskRepository.save(task);
    }

    public void editTask(EditTaskRequest editTaskRequest) {
        int taskId = Integer.parseInt(editTaskRequest.getTaskId());
        Task task = taskRepository.findTaskByTaskId(taskId);

        if (task == null) {
            throw new EntityNotFoundException("Задача с id= " + taskId + " не найдена!");
        }


        if (editTaskRequest.getExecutorId() != null) {
            int executorId = Integer.parseInt(editTaskRequest.getExecutorId());
            if (!userService.existUserByUserId(executorId)) {
                throw new EntityNotFoundException("Пользователь с id= " + executorId + " не найден");
            }
            task.setExecutorId(executorId);
        }

        updateNonNullFields(editTaskRequest, task);

        taskRepository.save(task);
    }

    public void editComment(EditComment editComment) {
        int taskId = Integer.parseInt(editComment.getTaskId());
        Task task = taskRepository.findTaskByTaskId(taskId);

        if (task == null) {
            throw new EntityNotFoundException("Задача с id= " + taskId + " не найдена!");
        }


        updateNonNullFields(editComment, task);

        taskRepository.save(task);
    }

    public void editExecutor(EditExecutor editExecutor) {
        int taskId = Integer.parseInt(editExecutor.getTaskId());
        Task task = taskRepository.findTaskByTaskId(taskId);

        if (task == null) {
            throw new EntityNotFoundException("Задача с id= " + taskId + " не найдена!");
        }

        if (editExecutor.getExecutorId() != null) {
            int executorId = Integer.parseInt(editExecutor.getExecutorId());
            if (!userService.existUserByUserId(executorId)) {
                throw new EntityNotFoundException("Пользователь с id= " + executorId + " не найден");
            }
            task.setExecutorId(executorId);
        }

        updateNonNullFields(editExecutor, task);

        taskRepository.save(task);
    }

    public void editCommentForUser(EditCommentForUser editCommentForUser) {
        int taskId = Integer.parseInt(editCommentForUser.getTaskId());
        int executor = Integer.parseInt(editCommentForUser.getExecutorId());
        Task task = taskRepository.findTaskByTaskId(taskId);

        if (task == null) {
            throw new EntityNotFoundException("Задача с id= " + taskId + " не найдена!");
        }

        if (task.getExecutorId() != executor) {
            throw new IllegalArgumentException("Пользователь " + executor + " не является исполнителем");
        }

        updateNonNullFields(editCommentForUser, task);

        taskRepository.save(task);
    }

    public void editStatusForUser(EditStatus editStatus) {
        int taskId = Integer.parseInt(editStatus.getTaskId());
        int executor = Integer.parseInt(editStatus.getExecutorId());
        Task task = taskRepository.findTaskByTaskId(taskId);

        if (task == null) {
            throw new EntityNotFoundException("Задача с id= " + taskId + " не найдена!");
        }

        if (task.getExecutorId() != executor) {
            throw new IllegalArgumentException("Пользователь " + executor + " не является исполнителем");
        }

        updateNonNullFields(editStatus, task);

        taskRepository.save(task);
    }

    private void updateNonNullFields(Object source, Object target) {
        Field[] fields = source.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(source);
                if (value != null) {
                    Field targetField = ReflectionUtils.findField(target.getClass(), field.getName());
                    if (targetField != null) {
                        targetField.setAccessible(true);

                        if (!targetField.getType().isAssignableFrom(field.getType())) {
                            value = convertValue(value, targetField.getType());
                        }
                        targetField.set(target, value);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Ошибка обновления полей", e);
            }
        }
    }

    private Object convertValue(Object value, Class<?> targetType) {
        try {
            if (targetType == int.class || targetType == Integer.class) {
                return Integer.parseInt(value.toString());
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Невозможно преобразовать значение: " + value + " в " + targetType.getSimpleName(), e);
        }
        return value;
    }

    public Page<Task> getTasksByExecutorId(int executorId, Pageable pageable) {
        return taskRepository.findTaskByExecutorId(executorId, pageable);
    }

    public Page<Task> getTasksByExecutorIdAndStatus(int executorId, String status, Pageable pageable) {
        return taskRepository.findByExecutorIdAndStatus(executorId, status, pageable);
    }

}
