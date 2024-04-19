package io.ussopm.UserApp.client;

import io.ussopm.UserApp.dto.CustomerDTO;
import io.ussopm.UserApp.model.AuthResponse;

public interface AuthRestClient {
    AuthResponse login(String username, String password);

    AuthResponse registration(CustomerDTO customerDTO);
}
