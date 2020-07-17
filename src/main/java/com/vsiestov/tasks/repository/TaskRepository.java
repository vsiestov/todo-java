package com.vsiestov.tasks.repository;

import com.vsiestov.tasks.domain.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {
    Page<Task> findAllByUserIdEquals(long userId, Pageable pageable);
    Optional<Task> findByIdAndUserId(long id, long userId);
    void deleteTaskByIdAndUserId(long id, long userId);
}
