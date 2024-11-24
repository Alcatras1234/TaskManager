package org.example.task_manager.models;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "sessions")
@Tag(name = "Task", description = "Модель для работы с таблицой задач")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private int sessionId;

    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "userid", nullable = false)
    private User user;


    @Column(name = "access_token", length = 255)
    private String accessToken;

    @Column(name = "refresh_token", length = 255)
    private String refreshToken;

    @Column(name = "expired", length = 255)
    private Date expiredRefresh;

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpiredDateRefresh() {
        return expiredRefresh;
    }

    public void setExpiredDateRefresh(Date date) {
        this.expiredRefresh = date;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
