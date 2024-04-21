package io.ussopm.ToDoListService.service.impl;

import io.ussopm.ToDoListService.exception.AlreadyExistsException;
import io.ussopm.ToDoListService.exception.AlreadyTakenTaskException;
import io.ussopm.ToDoListService.exception.MarkingTaskException;
import io.ussopm.ToDoListService.exception.NoCustomerAssignedException;
import io.ussopm.ToDoListService.model.Customer;
import io.ussopm.ToDoListService.model.Task;
import io.ussopm.ToDoListService.repository.CustomerRepository;
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
    private final CustomerRepository customerRepository;
    @Override
    public List<Task> getAllTasks(String filter) {
        if (filter != null && !filter.isBlank()) {
            return this.taskRepository.findAllByNameLikeIgnoreCase("%" + filter + "%");
        } else {
            return this.taskRepository.findAll();
        }
    }

    @Override
    public Task getTaskById(int taskId) {
        return this.taskRepository.findById(taskId).orElseThrow(() -> new NoSuchElementException("Task"));
    }

    @Override
    @Transactional
    public ResponseEntity<HttpStatus> saveTask(String name, String description) {
        if (this.taskRepository.existsByName(name)) {
            throw new AlreadyExistsException();
        } else {
            this.taskRepository.save(new Task(name, description, false));
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
            throw new NoSuchElementException("Task");
        } else {
            this.taskRepository.deleteById(taskId);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<HttpStatus> takingTask(int taskId, int customerId) {
        Task task = getTask(taskId);
        Customer customer = getCustomer(customerId);

        if (task.getCustomer() != null) {
            if (task.getCustomer() == customer) {
                throw new AlreadyExistsException(task.getName(), "you");
            } else if (task.getCustomer().getId() != customerId) {
                throw new AlreadyExistsException(task.getName(), task.getCustomer().getUsername());
            }
        } else {
            task.setCustomer(customer);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    @Transactional
    public ResponseEntity<HttpStatus> removingTask(int taskId, int customerId) {
        Task task = getTask(taskId);
        Customer customer = getCustomer(customerId);
        if (task.getCustomer() != null) {
            if (task.getCustomer() == customer) {
                task.setCustomer(null);
            } else {
                throw new AlreadyTakenTaskException(task.getName(), task.getCustomer().getUsername());
            }
        } else {
            throw new NoCustomerAssignedException("No assigned customer for task " + task.getName());
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    @Transactional
    public ResponseEntity<HttpStatus> markingTaskAsDone(int taskId, int customerId) {
        Task task = getTask(taskId);
        Customer customer = getCustomer(customerId);
        checkTask(task, customer);
        if (task.getTaskStatus().equals(true)) {
            throw new MarkingTaskException("Task is already in a marked state");
        } else {
            task.setTaskStatus(true);
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    @Transactional
    public ResponseEntity<HttpStatus> uncheckMarkFromTask(int taskId, int customerId) {
        Task task = getTask(taskId);
        Customer customer = getCustomer(customerId);
        checkTask(task, customer);
        if (task.getTaskStatus().equals(false)) {
            throw new MarkingTaskException("Task is already in an unchecked mark state");
        }else {
            task.setTaskStatus(false);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }



    private Customer getCustomer(int customerId) {
        return this.customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer"));
    }

    private Task getTask(int taskId) {
        return this.taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task"));
    }

    private void checkTask(Task task, Customer customer) {
        if (task.getCustomer() != customer) {
            throw new AlreadyExistsException(task.getName(), task.getCustomer().getUsername());
        }
    }
}
