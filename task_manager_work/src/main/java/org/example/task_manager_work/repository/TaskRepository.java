package org.example.task_manager_work.repository;

import org.example.task_manager_work.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Task findTaskByTaskId(int taskid);

    Page<Task> findTaskByExecutorId(int executorId, Pageable pageable);

    Page<Task> findByExecutorIdAndStatus(int executorId, String status, Pageable pageable);

    Page<Task> findByDescriptionContaining(String description, Pageable pageable);
}
