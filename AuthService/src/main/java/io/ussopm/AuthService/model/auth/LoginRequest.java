package io.ussopm.AuthService.model.auth;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

    @NotBlank(message = "Field have to be not empty")
    @NotNull
    @Size(min = 3, max = 50, message = "Username should be between 3 and 50")
    private String username;

    @NotBlank(message = "Field have to be not empty")
    @NotNull
    @Size(min = 2, max = 100, message = "Password should be between 3 and 50")
    private String password;
}
