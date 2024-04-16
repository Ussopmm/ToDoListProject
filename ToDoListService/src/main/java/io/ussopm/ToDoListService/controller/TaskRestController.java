package io.ussopm.ToDoListService.controller;

import io.ussopm.ToDoListService.controller.payload.UpdateTaskPayload;
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

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/todo/api/task/{id}")
@RequiredArgsConstructor
public class TaskRestController {

    private final TaskService taskService;

    @GetMapping()
    public Task taskById(@PathVariable("id") int taskId) {
        return this.taskService.getTaskById(taskId);
    }


    @PatchMapping("/edit")
    public ResponseEntity<HttpStatus> editTask(@RequestBody @Valid UpdateTaskPayload payload, @PathVariable("id") int taskId, BindingResult bindingResult) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            return this.taskService.updateTaskById(taskId, payload.name(), payload.description());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable("id") int taskId) {
        return this.taskService.deleteTaskById(taskId);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                        "Task with this id was not found"));
    }
}
