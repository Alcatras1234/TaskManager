package org.example.task_manager_work.repository;

import org.example.task_manager_work.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsUsersByUserId(int id);

}
