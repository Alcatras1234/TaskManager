package org.example.task_manager_work.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.task_manager_work.enums.RoleEnum;
import org.hibernate.annotations.Subselect;

import java.io.Serializable;

@Entity
@Subselect("SELECT * FROM users")
@Schema(description = "Сущность для работы с таблицой users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Integer userId;

    @Getter
    @Setter
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Getter
    @Setter
    @Column(name = "hashpassword", nullable = false, unique = true, length = 255)
    private String hashPassword;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private RoleEnum role;

    public User() {
    }

    public User(int id, String email, String hashPassword, RoleEnum role) {
        this.userId = id;
        this.email = email;
        this.hashPassword = hashPassword;
        this.role = role;
    }


}
