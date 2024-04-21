package io.ussopm.ToDoListService.controller;

import io.ussopm.ToDoListService.controller.payload.NewTaskPayload;
import io.ussopm.ToDoListService.dto.TaskDTO;
import io.ussopm.ToDoListService.exception.AlreadyExistsException;
import io.ussopm.ToDoListService.model.Task;
import io.ussopm.ToDoListService.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todo/api/tasks")
@RequiredArgsConstructor
public class TasksRestController {

    private final TaskService taskService;
    private final ModelMapper mapper;
    @GetMapping()
    public List<TaskDTO> getAllTasks(@RequestParam(name = "filter", required = false) String filter) {
        return this.taskService.getAllTasks(filter).stream().map(this::convertModelToDTO).collect(Collectors.toList());
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

    public TaskDTO convertModelToDTO(Task task) {
        return mapper.map(task, TaskDTO.class);
    }
}
