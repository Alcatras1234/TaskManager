package org.example.task_manager.repository;

import org.example.task_manager.models.Session;
import org.example.task_manager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Session getSessionsByUser(User user);

    Session getSessionsByRefreshToken(String refreshToken);
}
