package org.example.task_manager_work.services;

import jakarta.persistence.EntityNotFoundException;
import org.example.task_manager_work.dto.EditComment;
import org.example.task_manager_work.dto.EditCommentForUser;
import org.example.task_manager_work.dto.EditTaskRequest;
import org.example.task_manager_work.dto.TaskRequest;
import org.example.task_manager_work.handlerException.UserNotFoundException;
import org.example.task_manager_work.model.Task;
import org.example.task_manager_work.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class TaskServiceTest {

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Test
    void safeTask_UserExists_SavesTask() throws UserNotFoundException {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setExecutorId("1");
        taskRequest.setDescription("Test task");
        taskRequest.setComment("No comment");
        taskRequest.setStatus("NEW");

        Mockito.when(userService.existUserByUserId(1)).thenReturn(true);

        taskService.safeTask(taskRequest);

        Mockito.verify(taskRepository, Mockito.times(1)).save(any(Task.class));
    }

    @Test
    void safeTask_UserNotFound_ThrowsUserNotFoundException() {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setExecutorId("99");

        Mockito.when(userService.existUserByUserId(99)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> taskService.safeTask(taskRequest));
    }

    @Test
    void editTask_TaskExists_UpdatesTask() {
        EditTaskRequest editTaskRequest = new EditTaskRequest();
        editTaskRequest.setTaskId("1");
        editTaskRequest.setDescription("Updated description");

        Task task = new Task();
        task.setTaskId(1);
        task.setDescription("Old description");

        Mockito.when(taskRepository.findTaskByTaskId(1)).thenReturn(task);

        taskService.editTask(editTaskRequest);

        assertEquals("Updated description", task.getDescription());
        Mockito.verify(taskRepository, Mockito.times(1)).save(task);
    }

    @Test
    void editTask_TaskNotFound_ThrowsEntityNotFoundException() {
        EditTaskRequest editTaskRequest = new EditTaskRequest();
        editTaskRequest.setTaskId("99");

        Mockito.when(taskRepository.findTaskByTaskId(99)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> taskService.editTask(editTaskRequest));
    }

    @Test
    void editComment_TaskExists_UpdatesComment() {
        EditComment editComment = new EditComment();
        editComment.setTaskId("1");
        editComment.setComment("Updated comment");

        Task task = new Task();
        task.setTaskId(1);
        task.setComment("Old comment");

        Mockito.when(taskRepository.findTaskByTaskId(1)).thenReturn(task);

        taskService.editComment(editComment);

        assertEquals("Updated comment", task.getComment());
        Mockito.verify(taskRepository, Mockito.times(1)).save(task);
    }

    @Test
    void getTasksByExecutorId_ReturnsTasks() {
        int executorId = 1;
        Pageable pageable = Pageable.ofSize(10);
        Task task = new Task();
        task.setExecutorId(executorId);
        Page<Task> page = new PageImpl<>(Collections.singletonList(task));

        Mockito.when(taskRepository.findTaskByExecutorId(executorId, pageable)).thenReturn(page);

        Page<Task> result = taskService.getTasksByExecutorId(executorId, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(executorId, result.getContent().get(0).getExecutorId());
    }

    @Test
    void editCommentForUser_TaskAndUserMatch_UpdatesComment() {
        EditCommentForUser editCommentForUser = new EditCommentForUser();
        editCommentForUser.setTaskId("1");
        editCommentForUser.setExecutorId("1");
        editCommentForUser.setComment("Updated comment");

        Task task = new Task();
        task.setTaskId(1);
        task.setExecutorId(1);
        task.setComment("Old comment");

        Mockito.when(taskRepository.findTaskByTaskId(1)).thenReturn(task);

        taskService.editCommentForUser(editCommentForUser);

        assertEquals("Updated comment", task.getComment());
        Mockito.verify(taskRepository, Mockito.times(1)).save(task);
    }

    @Test
    void editCommentForUser_UserMismatch_ThrowsIllegalArgumentException() {
        EditCommentForUser editCommentForUser = new EditCommentForUser();
        editCommentForUser.setTaskId("1");
        editCommentForUser.setExecutorId("2");

        Task task = new Task();
        task.setTaskId(1);
        task.setExecutorId(1);

        Mockito.when(taskRepository.findTaskByTaskId(1)).thenReturn(task);

        assertThrows(IllegalArgumentException.class, () -> taskService.editCommentForUser(editCommentForUser));
    }
}
