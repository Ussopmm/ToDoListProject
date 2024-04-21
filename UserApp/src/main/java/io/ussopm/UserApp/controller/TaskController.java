package io.ussopm.UserApp.controller;

import io.ussopm.UserApp.client.TaskRestClient;
import io.ussopm.UserApp.controller.payload.UpdateTaskPayload;
import io.ussopm.UserApp.exception.AlreadyExistsException;
import io.ussopm.UserApp.model.Customer;
import io.ussopm.UserApp.model.Task;
import io.ussopm.UserApp.repository.CustomerRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import io.ussopm.UserApp.client.impl.BadRequestException;

import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/todo/task/{taskId:\\d+}")
@RequiredArgsConstructor
public class TaskController {

    private final TaskRestClient taskRestClient;
    private final CustomerRepository customerRepository;

    @ModelAttribute("task")
    public Task task(@PathVariable("taskId") int taskId) {
        return this.taskRestClient.getTaskById(taskId)
                .orElseThrow(NoSuchElementException::new);
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
            this.taskRestClient.updateTask(task.getId(), payload.name(), payload.description());
            return "redirect:/todo/task/" + task.getId();
        } catch (BadRequestException ex) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("errors", ex.getErrors());
            model.addAttribute("payload", payload);
            return "edit";
        }
    }

    @PostMapping("/delete")
    public String deleteTaskById(@ModelAttribute("task") Task task) {
        this.taskRestClient.deleteTask(task.getId());
        return "redirect:/todo/tasks";
    }

    @PostMapping("/taking")
    public String takingTaskForCustomer(@ModelAttribute("task") Task task, Model model, HttpServletResponse response) {
        try {
            Optional<Customer> customer = gettingCustomerFromAuthentication();
            this.taskRestClient.takingTaskForCustomer(task.getId(), customer.get().getId());
            return "redirect:/todo/tasks/home";
        } catch (AlreadyExistsException ex) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("errors", ex.getErrorMessage());
            return "task";
        }
    }

    @PostMapping("/removing")
    public String removingTaskFromCustomer(@ModelAttribute("task") Task task) {
        Optional<Customer> customer = gettingCustomerFromAuthentication();
        this.taskRestClient.removingTaskFromCustomer(task.getId(), customer.get().getId());
        return "redirect:/todo/tasks/home";
    }

    @PostMapping("/markingTask")
    public String markingTaskOfCustomer(@ModelAttribute("task") Task task) {
        Optional<Customer> customer = gettingCustomerFromAuthentication();
        this.taskRestClient.markingTask(task.getId(), customer.get().getId());
        return "redirect:/todo/tasks/home";
    }

    @PostMapping("/uncheckMark")
    public String uncheckMark(@ModelAttribute("task") Task task) {
        Optional<Customer> customer = gettingCustomerFromAuthentication();
        this.taskRestClient.uncheckMark(task.getId(), customer.get().getId());

        return "redirect:/todo/tasks/home";
    }

    private Optional<Customer> gettingCustomerFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return this.customerRepository.findCustomerByUsername(userDetails.getUsername());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException ex,HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return "errors/error404";
    }
}
