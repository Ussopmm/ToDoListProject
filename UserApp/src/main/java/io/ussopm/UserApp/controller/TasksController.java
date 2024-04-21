package io.ussopm.UserApp.controller;

import io.ussopm.UserApp.client.impl.BadRequestException;
import io.ussopm.UserApp.client.TaskRestClient;
import io.ussopm.UserApp.controller.payload.NewTaskPayload;
import io.ussopm.UserApp.model.Customer;
import io.ussopm.UserApp.repository.CustomerRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/todo/tasks")
@RequiredArgsConstructor
public class TasksController {

    private final TaskRestClient taskRestClient;
    private final CustomerRepository customerRepository;

    @GetMapping()
    public String listOfTasks(Model model, @RequestParam(name="filter", required = false) String filter) {
        model.addAttribute("tasks", this.taskRestClient.getAllTasks(filter));
        model.addAttribute("filter", filter);
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
            return "new";
        }
    }

    @GetMapping("/home")
    public String customerPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getAuthorities().stream()
                .noneMatch(r -> r.getAuthority().equals("ROLE_ANONYMOUS"))) {
            String username = authentication.getName();
            Optional<Customer> customer = this.customerRepository.findCustomerByUsername(username);
            model.addAttribute("customer", customer);
            model.addAttribute("username", username);
            model.addAttribute("tasks", customer.get().getTasks());

        } else {
            model.addAttribute("message", "User page");
            model.addAttribute("loginUrl", "/auth/login");
        }
        return "user";
    }

}
