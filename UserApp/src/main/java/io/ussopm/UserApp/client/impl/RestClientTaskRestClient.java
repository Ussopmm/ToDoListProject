package io.ussopm.UserApp.client.impl;

import io.ussopm.UserApp.client.TaskRestClient;
import io.ussopm.UserApp.controller.payload.NewTaskPayload;
import io.ussopm.UserApp.controller.payload.UpdateTaskPayload;
import io.ussopm.UserApp.exception.AlreadyExistsException;
import io.ussopm.UserApp.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class RestClientTaskRestClient implements TaskRestClient {

    private final RestClient restClient;
    private final static ParameterizedTypeReference<List<Task>> TASK_TYPE_REFERENCE =
            new ParameterizedTypeReference<List<Task>>() {};
    @Override
    public Optional<Task> getTaskById(int id) {
        try {
            return Optional.ofNullable(this.restClient
                    .get()
                    .uri("/todo/api/task/{id}",id)
                    .retrieve()
                    .body(Task.class));
        } catch (HttpClientErrorException.NotFound ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Task> getAllTasks(String filter) {
        return this.restClient
                .get()
                .uri("/todo/api/tasks?filter={filter}", filter)
                .retrieve()
                .body(TASK_TYPE_REFERENCE);
    }

    @Override
    public void createNewTask(String taskName, String description) {
        try {
            this.restClient
                    .post()
                    .uri("/todo/api/tasks/new")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new NewTaskPayload(taskName, description))
                    .retrieve()
                    .body(Task.class);
        } catch (HttpClientErrorException.BadRequest ex) {
            ProblemDetail problemDetail = ex.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void updateTask(int taskId, String taskName, String description) {
        try {
            this.restClient
                    .patch()
                    .uri("/todo/api/task/{id}/edit", taskId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new UpdateTaskPayload(taskName, description))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest ex) {
            ProblemDetail problemDetail = ex.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void deleteTask(int taskId) {
        try {
            this.restClient
                    .delete()
                    .uri("/todo/api/task/{id}/delete", taskId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound ex) {
            throw new NoSuchElementException(ex);
        }
    }

    @Override
    public void takingTaskForCustomer(int taskId, int customerId) {
        try {
            this.restClient
                    .post()
                    .uri("/todo/api/task/{id}/taking/{customerId}",taskId,customerId)
                    .body(taskId)
                    .body(customerId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest ex) {
            String response = ex.getResponseBodyAsString();
            String detail = response.split("\"detail\":\"")[1].split("\",\"instance\"")[0];
            throw new AlreadyExistsException(detail);
        }
    }

    @Override
    public void removingTaskFromCustomer(int taskId, int customerId) {
        this.restClient.post()
                    .uri("/todo/api/task/{id}/removing/{customerId}", taskId, customerId)
                    .body(taskId)
                    .body(customerId)
                    .retrieve()
                    .toBodilessEntity();
    }


    @Override
    public void markingTask(int taskId, int customerId) {
        this.restClient.post()
                .uri("/todo/api/task/{id}/markTask/{customerId}", taskId, customerId)
                .body(taskId)
                .body(customerId)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public void uncheckMark(int taskId, int customerId) {
        this.restClient.post()
                .uri("/todo/api/task/{id}/uncheckMark/{customerId}", taskId, customerId)
                .body(taskId)
                .body(customerId)
                .retrieve()
                .toBodilessEntity();
    }
}
