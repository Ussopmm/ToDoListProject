package io.ussopm.ToDoListService.controller;

import io.ussopm.ToDoListService.controller.payload.UpdateTaskPayload;
import io.ussopm.ToDoListService.dto.TaskDTO;

import io.ussopm.ToDoListService.model.Task;
import io.ussopm.ToDoListService.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/todo/api/task/{id}")
@RequiredArgsConstructor
public class TaskRestController {

    private final TaskService taskService;
    private final ModelMapper mapper;

    @GetMapping()
    public TaskDTO taskById(@PathVariable("id") int taskId) {
        return convertModelToDTO(this.taskService.getTaskById(taskId));
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

    @PostMapping("/taking/{customerId}")
    public ResponseEntity<HttpStatus> takingTaskForCustomer(@PathVariable("id") int taskId, @PathVariable("customerId") int customerId) {
        return this.taskService.takingTask(taskId, customerId);
    }

    @PostMapping("/removing/{customerId}")
    public ResponseEntity<HttpStatus> removingTaskOfCustomer(@PathVariable("id") int taskId, @PathVariable("customerId") int customerId) {
        return this.taskService.removingTask(taskId, customerId);
    }

    @PostMapping("/markTask/{customerId}")
    public ResponseEntity<HttpStatus> markingTask(@PathVariable("id") int taskId, @PathVariable("customerId") int customerId) {
        return this.taskService.markingTaskAsDone(taskId, customerId);
    }

    @PostMapping("/uncheckMark/{customerId}")
    public ResponseEntity<HttpStatus> uncheckMark(@PathVariable("id") int taskId, @PathVariable("customerId") int customerId) {
        return this.taskService.uncheckMarkFromTask(taskId, customerId);
    }

    public TaskDTO convertModelToDTO(Task task) {
        return mapper.map(task, TaskDTO.class);
    }
}
