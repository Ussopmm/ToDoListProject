package io.ussopm.UserApp.client;

import io.ussopm.UserApp.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRestClient {

    Optional<Task> getTaskById(int id);

    List<Task> getAllTasks(String filter);

    void createNewTask(String taskName, String description);

    void updateTask(int taskId, String taskName, String description);

    void deleteTask(int taskId);

    void takingTaskForCustomer(int taskId, int customerId);

    void removingTaskFromCustomer(int taskId, int customerId);

    void markingTask(int taskId, int customerId);

    void uncheckMark(int taskId, int customerId);

}
