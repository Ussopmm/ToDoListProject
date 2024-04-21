package io.ussopm.ToDoListService.repository;

import io.ussopm.ToDoListService.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    Boolean existsByName(String taskName);

    @Query(value = "select t from Task t where t.name ilike :filter")
    List<Task> findAllByNameLikeIgnoreCase(@Param("filter") String filter);
}
