package io.ussopm.UserApp.controller;

import io.ussopm.UserApp.client.BadRequestException;
import io.ussopm.UserApp.client.TaskRestClient;
import io.ussopm.UserApp.controller.payload.NewTaskPayload;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/todo/tasks")
@RequiredArgsConstructor
public class TasksController {

    private final TaskRestClient taskRestClient;

    @GetMapping()
    public String listOfTasks(Model model) {
        model.addAttribute("tasks", this.taskRestClient.getAllTasks());
        return "list";
    }

    @GetMapping("/new")
    public String newTaskPage(@ModelAttribute("task") NewTaskPayload payload) {
        return "new";
    }
    @PostMapping("/new")
    public String createNewTask(@ModelAttribute("task")NewTaskPayload payload, Model model, HttpServletResponse response) {
        try {
            this.taskRestClient.createNewTask(payload.name(), payload.description());
            return "redirect:/todo/tasks";
        } catch (BadRequestException ex) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("errors", ex.getErrors());
            model.addAttribute("payload", payload);
            return "new";
        }
    }

}
