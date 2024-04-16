package io.ussopm.UserApp.client;

import io.ussopm.UserApp.controller.payload.NewTaskPayload;
import io.ussopm.UserApp.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRestClient {

    Optional<Task> getTaskById(int id);

    List<Task> getAllTasks();

    Task createNewTask(String taskName, String description);

    void updateTask(int taskId, String taskName, String description);

    void deleteTask(int taskId);
}
