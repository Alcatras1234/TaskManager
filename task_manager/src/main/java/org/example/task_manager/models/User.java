package org.example.task_manager.models;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import org.example.task_manager.enums.RoleEnum;

import java.io.Serializable;

@Entity
@Table(name = "users")
@Schema(description = "Сущность для работы с таблицой users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Integer userId;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "hashpassword", nullable = false, unique = true, length = 255)
    private String hashPassword;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
}
