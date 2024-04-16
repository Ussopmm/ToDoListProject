package io.ussopm.UserApp.controller;

import io.ussopm.UserApp.client.TaskRestClient;
import io.ussopm.UserApp.controller.payload.UpdateTaskPayload;
import io.ussopm.UserApp.model.Task;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import io.ussopm.UserApp.client.BadRequestException;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/todo/task/{taskId:\\d+}")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRestClient taskRestClient;

    @ModelAttribute("task")
    public Task task(@PathVariable("taskId") int taskId) {
        return this.taskRestClient.getTaskById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
    }
    @GetMapping()
    public String getTaskById() {
        return "task";
    }

    @GetMapping("/edit")
    public String editTaskPage() {
        return "edit";
    }

    @PostMapping("/edit")
    public String editTaskById(@ModelAttribute("task")Task task, UpdateTaskPayload payload, Model model, HttpServletResponse response) {
        try {
            this.taskRestClient.updateTask(task.id(), payload.name(), payload.description());
            return "redirect:/todo/task/" + task.id();
        } catch (BadRequestException ex) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("errors", ex.getErrors());
            model.addAttribute("payload", payload);
            return "edit";
        }
    }

    @PostMapping("/delete")
    public String deleteTaskById(@ModelAttribute("task") Task task) {
        this.taskRestClient.deleteTask(task.id());
        return "redirect:/todo/tasks";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException ex, Model model, HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", ex.getMessage());

        return "errors/error404";
    }
}
