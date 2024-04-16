package io.ussopm.ToDoListService.service.impl;

import io.ussopm.ToDoListService.exception.AlreadyExistsException;
import io.ussopm.ToDoListService.model.Task;
import io.ussopm.ToDoListService.repository.TaskRepository;
import io.ussopm.ToDoListService.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    @Override
    public List<Task> getAllTasks() {
        return this.taskRepository.findAll();
    }

    @Override
    public Task getTaskById(int taskId) {
        return this.taskRepository.findById(taskId).orElseThrow(NoSuchElementException::new);
    }

    @Override
    @Transactional
    public ResponseEntity<HttpStatus> saveTask(String name, String description) {
        if (this.taskRepository.existsByName(name)) {
            throw new AlreadyExistsException();
        } else {
            this.taskRepository.save(new Task(name, description));
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<HttpStatus> updateTaskById(int taskId, String name, String description) {
        this.taskRepository.findById(taskId).ifPresentOrElse(task -> {
            task.setName(name);
            task.setDescription(description);
        }, () -> {
            throw new NoSuchElementException();
        });

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<HttpStatus> deleteTaskById(int taskId) {
        if (!this.taskRepository.existsById(taskId)) {
            throw new NoSuchElementException();
        } else {
            this.taskRepository.deleteById(taskId);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
    }
}
