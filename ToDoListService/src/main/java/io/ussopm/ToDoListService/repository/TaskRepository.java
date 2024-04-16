package io.ussopm.ToDoListService.repository;

import io.ussopm.ToDoListService.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    Boolean existsByName(String taskName);
}
