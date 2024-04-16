package io.ussopm.ToDoListService.controller;

import io.ussopm.ToDoListService.controller.payload.NewTaskPayload;
import io.ussopm.ToDoListService.exception.AlreadyExistsException;
import io.ussopm.ToDoListService.model.Task;
import io.ussopm.ToDoListService.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo/api/tasks")
@RequiredArgsConstructor
public class TasksRestController {

    private final TaskService taskService;
    @GetMapping()
    public List<Task> getAllTasks() {
        return this.taskService.getAllTasks();
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> newTask(@RequestBody @Valid NewTaskPayload newTaskPayload, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            return this.taskService.saveTask(newTaskPayload.name(), newTaskPayload.description());
        }
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleAlreadyExistsException(AlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                        "Task with this name already exists"));
    }

}
