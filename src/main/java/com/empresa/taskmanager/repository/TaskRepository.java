package com.empresa.taskmanager.repository;

import com.empresa.taskmanager.model.Priority;
import com.empresa.taskmanager.model.Task;
import com.empresa.taskmanager.model.TaskStatus;
import com.empresa.taskmanager.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser(User user);
    Optional<Task> findByIdAndUser(Long id, User user);
    Page<Task> findByUser(User user, Pageable pageable);
    List<Task> findByUserAndStatus(User user, TaskStatus status);
    List<Task> findByUserAndPriority(User user, Priority priority);
    List<Task> findByUserAndStatusAndPriority(User user, TaskStatus status, Priority priority);

    @Query("""
            SELECT t
            FROM Task t
            WHERE t.user = :user
            AND (
                LOWER(t.titulo) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR
                LOWER(t.descripcion) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
    """)
    List<Task> searchTasks(@Param("user") User user, @Param("keyword") String keyword);

}
