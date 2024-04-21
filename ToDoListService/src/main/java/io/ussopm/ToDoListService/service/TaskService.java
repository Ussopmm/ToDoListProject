package io.ussopm.ToDoListService.service;

import io.ussopm.ToDoListService.model.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TaskService {
    List<Task> getAllTasks(String filter);

    Task getTaskById(int taskId);

    ResponseEntity<HttpStatus> saveTask(String name, String description);

    ResponseEntity<HttpStatus> updateTaskById(int taskId, String name, String description);

    ResponseEntity<HttpStatus> deleteTaskById(int taskId);
    ResponseEntity<HttpStatus> takingTask(int taskId, int customerId);
//

    ResponseEntity<HttpStatus> markingTaskAsDone(int taskId, int customerId);

    ResponseEntity<HttpStatus> uncheckMarkFromTask(int taskId, int customerId);

    ResponseEntity<HttpStatus> removingTask(int taskId, int customerId);
}
