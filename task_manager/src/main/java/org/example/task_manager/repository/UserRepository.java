package org.example.task_manager.repository;

import org.example.task_manager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUsersByEmail(String email);

    User findUserByEmail(String email);

}
