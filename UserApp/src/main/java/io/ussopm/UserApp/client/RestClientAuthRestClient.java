package io.ussopm.UserApp.client;

import io.ussopm.UserApp.controller.payload.LoginRequest;
import io.ussopm.UserApp.dto.CustomerDTO;
import io.ussopm.UserApp.model.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@RequiredArgsConstructor
public class RestClientAuthRestClient implements AuthRestClient{
    private final RestClient restClient;
    @Override
    public AuthResponse login(String username, String password) {
            return this.restClient.post()
                    .uri("auth/api/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new LoginRequest(username, password))
                    .retrieve()
                    .body(AuthResponse.class);
    }

    @Override
    public AuthResponse registration(CustomerDTO customerDTO) {
        try {
            return this.restClient.post()
                    .uri("auth/api/registration")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(customerDTO)
                    .retrieve()
                    .body(AuthResponse.class);
        } catch (HttpClientErrorException.BadRequest ex) {
            ProblemDetail problemDetail = ex.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }
}
