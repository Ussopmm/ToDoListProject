package io.ussopm.ToDoListService.service;

import io.ussopm.ToDoListService.model.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TaskService {
    List<Task> getAllTasks();

    Task getTaskById(int taskId);

    ResponseEntity<HttpStatus> saveTask(String name, String description);

    ResponseEntity<HttpStatus> updateTaskById(int taskId, String name, String description);

    ResponseEntity<HttpStatus> deleteTaskById(int taskId);
}
