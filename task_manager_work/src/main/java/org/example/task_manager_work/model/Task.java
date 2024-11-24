package org.example.task_manager_work.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "task")
@Getter
@Setter
@Schema(description = "Сущность для работы с таблицой taks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "taskid")
    private Integer taskId;

    @Column(name = "description", nullable = false, length = 500)
    private String description;


    @Column(name = "executorid", nullable = false)
    private int executorId;

    @Column(name = "comment", length = 500)
    private String comment;

    @Column(name = "status", length = 50)
    private String status;
}
